package whiteBalance.tools;

import boofcv.gui.feature.VisualizeShapes;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import whiteBalance.tools.geom.DFMPoint2D_I32;

/**
 * EllipseRecognition
 * @author Lasse
 * @version 28-03-2017
 */
public class EllipseRecognition 
{
    public Point2D_F64[] findCenters(List<Point2D_I32> contour) {
        DFMPoint2D_I32[] centers = new DFMPoint2D_I32[5];
        
        //Find center
        DFMPoint2D_I32 sum = new DFMPoint2D_I32();
        for (Point2D_I32 p : contour)
            sum.addSave(p);
        
        centers[0] = sum.divide(contour.size());
        
        //Find foci
        DFMPoint2D_I32 sumUpperHorizontal = new DFMPoint2D_I32();
        DFMPoint2D_I32 sumLowerHorizontal = new DFMPoint2D_I32();
        int pointsUpperHorizontal = 0, pointsLowerHorizontal = 0;
        
        DFMPoint2D_I32 sumUpperVertical = new DFMPoint2D_I32();
        DFMPoint2D_I32 sumLowerVertical = new DFMPoint2D_I32();
        int pointsUpperVertical = 0, pointsLowerVertical = 0;
        for (Point2D_I32 p : contour) {
            //horizontal
            if(p.y > centers[0].y) {
                sumUpperHorizontal.addSave(p);
                pointsUpperHorizontal++;
            }
            else {
                sumLowerHorizontal.addSave(p);
                pointsLowerHorizontal++;
            }
            
            //Vertical
            if(p.x > centers[0].x) {
                sumUpperVertical.addSave(p);
                pointsUpperVertical++;
            }
            else {
                sumLowerVertical.addSave(p);
                pointsLowerVertical++;
            }
        }
        
        if(pointsUpperHorizontal > 0)
            centers[1] = sumUpperHorizontal.divide(pointsUpperHorizontal);
        else
            centers[1] = centers[0];
        if(pointsUpperHorizontal > 0)
            centers[2] = sumLowerHorizontal.divide(pointsLowerHorizontal);
        else
            centers[2] = centers[0];
        
        if(pointsUpperVertical > 0)
            centers[3] = sumUpperVertical.divide(pointsUpperVertical);
        else
            centers[3] = centers[0];
        if(pointsUpperVertical > 0)
            centers[4] = sumLowerVertical.divide(pointsLowerVertical);
        else
            centers[4] = centers[0];
        
        Point2D_F64[] result = new Point2D_F64[3];
        result[0] = new Point2D_F64(centers[0].x, centers[0].y);
        if(centers[1].distance(centers[0]) + centers[2].distance(centers[0]) > centers[3].distance(centers[0]) + centers[4].distance(centers[0])) {
            result[1] = new Point2D_F64(centers[1].x, centers[1].y);
            result[2] = new Point2D_F64(centers[2].x, centers[2].y);
        }
        else {
            result[1] = new Point2D_F64(centers[3].x, centers[3].y);
            result[2] = new Point2D_F64(centers[4].x, centers[4].y);
        }
        
        return result;
    }

    public double findAngle(Point2D_F64[] centers, boolean degrees) {
        double theta1 = angleBetween(centers[0], centers[1], degrees);
        double theta2 = angleBetween(centers[2], centers[0], degrees);
        
        return (theta1 + theta2) / 2;
    }

    public EllipseRotated_F64 findEllipse(List<Point2D_I32> contour, Point2D_F64 center, double majorAngle) {
        Point2D_I32 c = new Point2D_I32((int) center.x, (int) center.y);
        double majorAng = majorAngle;
        double minorAng = majorAngle + 90;
        double major = 0, minor = 0;
        
        double angle;
        int majPoints = 0, minPoints = 0;
        for (Point2D_I32 p : contour) {
            angle = angleBetween(c, p, true);
            //Major
            if((angle > majorAng - 5 && angle < majorAng + 5) || (angle > majorAng + 175 && angle < majorAng + 185)) {
                major += p.distance(c);
                majPoints++;
            }
            //Minor
            else if((angle > minorAng - 5 && angle < minorAng + 5) || (angle > minorAng + 175 && angle < minorAng + 185)) {
                minor += p.distance(c);
                minPoints++;
            }
        }
        
        major /= majPoints;
        minor /= minPoints;
        
        return new EllipseRotated_F64(center, (float) major, (float) minor, (float) -Math.toRadians(majorAng));
    }
    
    public void drawEllipse(EllipseRotated_F64 ellipse, float stroke, Color color, Graphics2D g) {
        g.setStroke(new BasicStroke(stroke));
        g.setColor(color);
        VisualizeShapes.drawEllipse(ellipse, g);
    }

    public double findAverageError(List<Point2D_I32> external, Point2D_I32 center, double[] axis, double angle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public double findAverageError(List<Point2D_I32> contour, Point2D_I32 center, double minor, double major) {
        double err = -1;
        
        return err;
//        Point2D_I32 expP1, expP2;
//        double err, sumErr = 0, expY1, expY2;
//        
//        for (Point2D_I32 p : contour) {
//            expY1 = Math.sqrt(radius*radius - p.x*p.x + 2*center.x*p.x - center.x*center.x) + center.y;
//            expY2 = -(Math.sqrt(radius*radius - p.x*p.x + 2*center.x*p.x - center.x*center.x) - center.y);
//            expP1 = new Point2D_I32(p.x, (int) expY1);
//            expP2 = new Point2D_I32(p.x, (int) expY2);
//            
//            if(p.distance(expP1) < p.distance(expP2))
//                err = p.distance(expP1);
//            else
//                err = p.distance(expP2);
//            
//            sumErr += err;
//        }
//        return sumErr / contour.size();
    }
    
    private double angleBetween(Point2D_F64 p1, Point2D_F64 p2, boolean degrees) {
        return angleBetween(new Point2D_I32((int) p1.x, (int) p1.y), new Point2D_I32((int) p2.x, (int) p2.y), degrees);
    }
    
    private double angleBetween(Point2D_I32 p1, Point2D_I32 p2, boolean degrees) {
        double angle = -Math.atan2(p2.y-p1.y, p2.x-p1.x);
        
        if(degrees)
            return Math.toDegrees(angle);
        else
            return angle;
    }
}