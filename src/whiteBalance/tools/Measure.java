package whiteBalance.tools;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.shapes.ellipse.BinaryEllipseDetector;
import boofcv.factory.shape.FactoryShapeDetector;
import boofcv.gui.feature.VisualizeShapes;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.ddogleg.struct.FastQueue;
import whiteBalance.data.WBData;
import whiteBalance.exceptions.DetectionException;
import whiteBalance.tools.geom.DFMPoint2D_I32;

/**
 * Measure
 * @author Lasse
 * @version 19-02-2017
 */
public class Measure
{
    private final BinaryEllipseDetector<GrayU8> detector;
    private BufferedImage image;
    private final Graphics2D g2;
    private GrayU8 filtered;
    
    public Measure(BufferedImage image) {
        this.detector = FactoryShapeDetector.ellipse(null, GrayU8.class);
        this.image = image;
        
        g2 = image.createGraphics();
    }
    
    public Measure(String filePath, int maxSize) {
        this(ImageLoader.load(filePath, maxSize));
    }
    
    public Measure(String filePath) {
        this(ImageLoader.load(filePath));
    }
    
    public FastQueue<EllipseRotated_F64> findEllipses(boolean draw, double threshhold) {
//        GrayU8 input = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);
        GrayU8 input = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);
        FastQueue<EllipseRotated_F64> found;
//        GrayU8 binary = new GrayU8(input.width, input.height);
        GrayU8 binary = new GrayU8(input.width,input.height);
        
        // reduce noise with some filtering
        filtered = BinaryImageOps.erode8(binary, 1, null);
        filtered = BinaryImageOps.dilate8(filtered, 1, null);
        
        // the mean pixel value is often a reasonable threshold when creating a binary image
//        int threshold = GThresholdImageOps.computeOtsu(input, 0, 255);
        
        // create a binary image by thresholding
//        ThresholdImageOps.threshold(input, filtered, threshold, true);
//        filtered = GThresholdImageOps.localSauvola(input, filtered, 5, 0.30f, true); //Sauvola
//        filtered = GThresholdImageOps.localBlockMinMax(input, binary, 5, 0.48, true, 5); //Block
        filtered = GThresholdImageOps.localGaussian(input, binary, 15, 0.4, true, null, null); //Gaussian
//        filtered = GThresholdImageOps.localSquare(input, binary, 75, .35, true, null, null); //Square
        
        // it takes in a grey scale image and binary image
        // the binary image is used to do a crude polygon fit, then the grey image is used to refine the lines
        // using a sub-pixel algorithm
        detector.process(input, filtered);
        List<Contour> contours = detector.getAllContours();
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLUE);
        
        boolean isCircle;
        for (Contour contour : contours) {
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke());
            VisualizeShapes.drawPolygon(contour.external, true, g2);
            //Bestem om dette er en oval!
            
            //Find centrum
            Point2D_I32 center = findCenter(contour.external);
            
            //Find avg radius, minor og major
            double radius = findAverageDistance(contour.external, center);
            
            //Find gennemsnitlig afvigelse fra cirkel/ellipse
            double avgErr = findAverageErrorCircle(contour.external, center, radius);
            
            //Determine if the group is a circle
            isCircle = avgErr / radius < threshhold;
            
            if(isCircle) {
                //Tegn svar
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(2));
                
                System.out.println("\nlist size = " + contour.external.size());
                System.out.println("avgError = " + avgErr);
                System.out.println("facError = " + avgErr / radius);
                
                System.out.println("Center = " + center);
                g2.fillOval(center.x-2, center.y-2, 4, 4);
                g2.drawString("Center", center.x, center.y + 1);
                
                System.out.println("Radius (avg) = " + radius);
                g2.drawLine(center.x, center.y, (int) (center.x + radius), center.y);
                g2.drawString("Radius", (int) (center.x + radius), center.y + 1);
                
                g2.drawOval((int) (center.x - radius), (int) (center.y - radius), (int) radius * 2, (int) radius * 2);
            }
        }
        
        // Find the contour around the shapes
        found = detector.getFoundEllipses();
        
        if(draw)
            drawEllipses(found, 2);
        
        return found;
    }
    
    private DFMPoint2D_I32 findCenter(List<Point2D_I32> contour) {
        DFMPoint2D_I32 sum = new DFMPoint2D_I32();
        for (Point2D_I32 p : contour)
            sum.addSave(p);
        
        return sum.divide(contour.size());
    }
    
    private double findAverageDistance(List<Point2D_I32> contour, Point2D_I32 center) {
        double radius = 0;
        for (Point2D_I32 p : contour)
            radius += p.distance(center);
        
        return radius / contour.size();
    }
    
    private double findAverageErrorCircle(List<Point2D_I32> contour, Point2D_I32 center, double radius) {
        Point2D_I32 expP1, expP2;
        double err, sumErr = 0, expY1, expY2;
        
        for (Point2D_I32 p : contour) {
            expY1 = Math.sqrt(radius*radius - p.x*p.x + 2*center.x*p.x - center.x*center.x) + center.y;
            expY2 = -(Math.sqrt(radius*radius - p.x*p.x + 2*center.x*p.x - center.x*center.x) - center.y);
            expP1 = new Point2D_I32(p.x, (int) expY1);
            expP2 = new Point2D_I32(p.x, (int) expY2);
            
            if(p.distance(expP1) < p.distance(expP2))
                err = p.distance(expP1);
            else
                err = p.distance(expP2);
            
            sumErr += err;
        }
        return sumErr / contour.size();
    }
    
    public FastQueue<EllipseRotated_F64> findEllipses(int minSize, boolean draw, double threshhold) {
        FastQueue<EllipseRotated_F64> found = findEllipses(false, threshhold);
        
        if(found.size > 4 && minSize > 0) {
            ArrayList<Integer> removeList = new ArrayList<>();
            int count = 0;
            EllipseRotated_F64 ellipse;
            for (int i = 0; i < found.size; i++) {
                ellipse = found.get(i);
                if(ellipse.a < minSize || ellipse.b < minSize)
                    removeList.add(i);
            }
            for (Integer removeIndex : removeList) {
                found.remove(removeIndex - count);
                count++;
            }
        }
        
        if(draw) {
            drawEllipses(found, 2);
        }
        return found;
    }
    
    public EllipseRotated_F64 findMaxEllipse(boolean draw, double threshhold) {
        FastQueue<EllipseRotated_F64> found = findEllipses(true, threshhold);
        
        EllipseRotated_F64 ellipse, portal = null;
        double size, maxSize = -1;
        for (int i = 0; i < found.size; i++) {
            ellipse = found.get(i);
            size = ellipse.a + ellipse.b;
            
            if(ellipse.a + ellipse.b > maxSize) {
                portal = ellipse;
                maxSize = size;
            }
        }
        
        if(draw) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.RED);
            
            if(portal != null)
                VisualizeShapes.drawEllipse(portal, g2);
        }
        
        return portal;
    }
    
    private void drawEllipses(FastQueue<EllipseRotated_F64> ellipses, float strokeWidth) {
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.setColor(Color.RED);
        
        for (int i = 0; i < ellipses.size; i++)
            VisualizeShapes.drawEllipse(ellipses.get(i), g2);
    }
    
    public Point2D_F64[] computeCorners(FastQueue<EllipseRotated_F64> ellipses) throws DetectionException {
        EllipseRotated_F64[] corners = new EllipseRotated_F64[4];
        Point2D_F64[] cornersCoords = new Point2D_F64[4];
        
        if(ellipses.size != 4)
            throw new DetectionException("Could not find all four calibration points. Found=" + ellipses.size);
        
        // Fit an ellipse to each external contour and draw the results
        Double[] score = {999999.9, 999999.9, 999999.9, 999999.9};
        double x, y;
        Point2D_F64 cornerCoord;
        EllipseRotated_F64 currentPoint;
        for (int i = 0; i < ellipses.size; i++) {
            currentPoint = ellipses.get(i);
            cornerCoord = currentPoint.getCenter();
            x = cornerCoord.x;
            y = cornerCoord.y;
            
            double toFirst = Math.sqrt(Math.pow((x-0), 2) + Math.pow((y-0), 2));
            if(toFirst < score[0]) {
                score[0] = toFirst;
                corners[0] = currentPoint;
            }
            double toSecond = Math.sqrt(Math.pow((x-image.getWidth()), 2) + Math.pow((y-0), 2));
            if(toSecond < score[1]) {
                score[1] = toSecond;
                corners[1] = currentPoint;
            }
            double toThird = Math.sqrt(Math.pow((x-0), 2) + Math.pow((y-image.getHeight()), 2));
            if(toThird < score[2]) {
                score[2] = toThird;
                corners[2] = currentPoint;
            }
            double toFourth = Math.sqrt(Math.pow((x-image.getWidth()), 2) + Math.pow((y-image.getHeight()), 2));
            if(toFourth < score[3]) {
                score[3] = toFourth;
                corners[3] = currentPoint;
            }
        }
        
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.GREEN);
        EllipseRotated_F64 caliPoint;
        for (int i = 0; i < corners.length; i++) {
            caliPoint = corners[i];
            
            cornersCoords[i] = caliPoint.getCenter();
            g2.drawString("p"+i, (int) caliPoint.getCenter().x, (int) caliPoint.getCenter().y);
            VisualizeShapes.drawEllipse(caliPoint, g2);
        }
        
        return cornersCoords;
    }
    
    public Integer[] computeColorOffset(Point2D_F64[] corners, boolean subtract) {
        //Compute half square dimension
        int strideX = (int) (corners[2].y - corners[0].y)/4;
        int strideY = (int) (corners[1].x - corners[0].x)/6;
        Dimension sampleDim = new Dimension(strideY/2, strideX/2);
        
        //Compute row and column placement
        Integer[] rowPlacement = new Integer[4];
        Integer[] columnPlacement = new Integer[6];
        
        for (int rowIndex = 0; rowIndex < rowPlacement.length; rowIndex++) {
            rowPlacement[rowIndex] = (int) corners[0].y + strideY * (rowIndex + 1) - strideY/2;
        }
        
        for (int columnIndex = 0; columnIndex < columnPlacement.length; columnIndex++) {
            columnPlacement[columnIndex] = (int) corners[0].x + strideX * (columnIndex + 1) - strideX/2;
        }
        
        //Compute sample coordinates
        Point[] samplePoints = new Point[24];
        for (int i=0, iy = 0, ix = 0; i < 24; iy++) {
            while(ix < 6) {
                samplePoints[i] = new Point(columnPlacement[ix], rowPlacement[iy]);
                ix++; i++;
            }
            ix = 0;
        }
        
        //Compute color difference
        Point sampleLocation;
        Color avgColor;
        int red = 0, blue = 0, green = 0;
        for (int si = 0; si < samplePoints.length; si++) {
            sampleLocation = samplePoints[si];
            avgColor = getAverageColor(image, sampleLocation.x - sampleDim.width/2, sampleLocation.y - sampleDim.height/2, sampleDim.width, sampleDim.height);
            red +=  WBData.wbColors[si].getRed() - avgColor.getRed();
            green +=  WBData.wbColors[si].getGreen() - avgColor.getGreen();
            blue += WBData.wbColors[si].getBlue() - avgColor.getBlue();
            g2.drawRect(sampleLocation.x - sampleDim.width/2, sampleLocation.y - sampleDim.height/2, sampleDim.width, sampleDim.height);
        }
        red /= 24;
        green /= 24;
        blue /= 24;
        
        if(!subtract) {
            if(red < 0) {
                green += Math.abs(red);
                blue += Math.abs(red);
                red = 0;
            }
            if(green <0) {
                red += Math.abs(red);
                blue += Math.abs(red);
                green = 0;
            }
            if(blue < 0) {
                red += Math.abs(blue);
                green += Math.abs(blue);
                blue = 0;
            }
        }
        
        return new Integer[]{red, green, blue};
    }
    
    private Color getAverageColor(BufferedImage bi, int x0, int y0, int w, int h) {
        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Color pixel = new Color(bi.getRGB(x, y));
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
            }
        }
        int num = w * h;
        int avgR = (int) (sumr / num);
        int avgG = (int) (sumg / num);
        int avgB = (int) (sumb / num);
        
        return new Color(avgR, avgG, avgB);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public GrayU8 getBinary() {
        return filtered;
    }
}