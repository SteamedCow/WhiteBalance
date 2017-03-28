package whiteBalance;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.binary.VisualizeBinaryData;
import whiteBalance.tools.WhiteBalance;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.image.BufferedImage;
import whiteBalance.exceptions.DetectionException;
import gui.ImageViewer;
import whiteBalance.tools.Calibrator;
import whiteBalance.tools.ImageLoader;
import whiteBalance.tools.Measure;

/**
 * main
 * @author Lasse
 * @version 10-02-2017
 */
public class main
{
    public static void main(String[] args) throws DetectionException {
        int run = 4;
        boolean calibrate = false;
        int minSize = 3, imgMaxSize = 1000;
        double threshhold = 0.3; //0.165;
        String filePath = "D:\\Programmer\\NetBeans 8.0.2\\My Javas\\CDIO\\White Balance\\White Balance\\";
        
        String fileName;
        
        if(calibrate) {
            switch (run) {
                default:
                case 0: fileName = "WBColorChart - red.jpg"; break;
                case 1: fileName = ".jpg"; break;
                case 2: fileName = ".jpg"; break;
                case 3: fileName = ".jpg"; break;
                case 4: fileName = ".jpg"; break;
                case 5: fileName = ".jpg"; break;
                case 6: fileName = ".jpg"; break;
                case 7: fileName = ".jpg"; break;
                case 8: fileName = ".jpg"; break;
            }
            
            Calibrator calib = new Calibrator(filePath + fileName, imgMaxSize, true);
            Integer[] colorOffset = calib.calibrate(minSize);
            if(colorOffset != null) {
                WhiteBalance wb = new WhiteBalance(colorOffset[0], colorOffset[1], colorOffset[2]);
                wb.colorImage(calib.getImage());
                
                ShowImages.showWindow(calib.getBinary(), "Binary", true);
                ShowImages.showWindow(calib.getImage(), "White Balance", true);
            }
        }
        else {
            switch (run) {
                default:
                case 0: fileName = "20170224_121449.jpg"; break;
                case 1: fileName = "20170224_121451.jpg"; break;
                case 2: fileName = "20170315_091300.jpg"; break;
                case 3: fileName = "20170315_091305.jpg"; break;
                case 4: fileName = "20170315_091305 (redigeret).jpg"; break;
                case 5: fileName = "circle.jpg"; break;
                case 6: fileName = "ellipse.jpg"; break;
                case 7: fileName = "square.jpg"; break;
                case 8: fileName = "star.jpg"; break;
                case 9: fileName = "triangle.jpg"; break;
                case 10: fileName = "shapes.jpg"; break;
                case 11: fileName = "20170315_091300 (redigeret).jpg"; break;
            }
            
            BufferedImage img = ImageLoader.load(filePath + fileName, 900);
            Measure ms = new Measure(img);
            
            EllipseRotated_F64 portal = ms.findMaxEllipse(true, threshhold);
            
//            Point3D coordsToPortal = new Navigator().flyToPortal(portal, img, true);
//            System.out.println(coordsToPortal);
            ShowImages.showWindow(ms.getBinary(), "Binary", true);
            new ImageViewer("Navigator", ImageViewer.CloseOperation.EXIT, img).show();
            
//            // example in which global thresholding works best
//            threshold(UtilIO.pathExample(filePath + fileName));
//            // example in which adaptive/local thresholding works best
//            threshold(UtilIO.pathExample(filePath + fileName));
//            // hand written text with non-uniform stained background
//            threshold(UtilIO.pathExample(filePath + fileName));
        }
    }
    
    public static void threshold( String imageName ) {
        BufferedImage image = UtilImageIO.loadImage(imageName);
        
        // convert into a usable format
        GrayF32 input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
        GrayU8 binary = new GrayU8(input.width,input.height);
        
        // Display multiple images in the same window
        ListDisplayPanel gui = new ListDisplayPanel();
        
        // Global Methods
        GThresholdImageOps.threshold(input, binary, ImageStatistics.mean(input), true);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Mean");
        GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeOtsu(input, 0, 255), true);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Otsu");
        GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeEntropy(input, 0, 255), true);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Entropy");
        
        // Local method
        GThresholdImageOps.localSquare(input, binary, 28, 1.0, true, null, null);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Square");
        GThresholdImageOps.localBlockMinMax(input, binary, 10, 1.0, true, 15 );
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Block Min-Max");
        GThresholdImageOps.localGaussian(input, binary, 42, 1.0, true, null, null);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Gaussian");
        GThresholdImageOps.localSauvola(input, binary, 5, 0.30f, true);
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Sauvola");
        
        // Sauvola is tuned for text image.  Change radius to make it run better in others.
        
        // Show the image image for reference
        gui.addImage(ConvertBufferedImage.convertTo(input,null),"Input Image");
        
        String fileName =  imageName.substring(imageName.lastIndexOf('/')+1);
        ShowImages.showWindow(gui,fileName);
}

}
