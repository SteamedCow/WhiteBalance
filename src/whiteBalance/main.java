package whiteBalance;

import whiteBalance.tools.WhiteBalance;
import boofcv.gui.image.ShowImages;
import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.image.BufferedImage;
import javafx.geometry.Point3D;
import whiteBalance.exceptions.DetectionException;
import whiteBalance.gui.ImageViewer;
import whiteBalance.tools.Calibrator;
import whiteBalance.tools.ImageLoader;
import whiteBalance.tools.Measure;
import whiteBalance.tools.Navigator;

/**
 * main
 * @author Lasse
 * @version 10-02-2017
 */
public class main
{
    public static void main(String[] args) throws DetectionException {
        int run = 0;
        boolean calibrate = false;
        int minSize = 3, imgMaxSize = 1000;
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
                case 2: fileName = ".jpg"; break;
                case 3: fileName = ".jpg"; break;
                case 4: fileName = ".jpg"; break;
                case 5: fileName = ".jpg"; break;
                case 6: fileName = ".jpg"; break;
                case 7: fileName = ".jpg"; break;
                case 8: fileName = ".jpg"; break;
            }
            
            BufferedImage img = ImageLoader.load(filePath + fileName, 900);
            Measure ms = new Measure(img);
            
            EllipseRotated_F64 portal = ms.findMaxEllipse(true);
            
            Point3D coordsToPortal = new Navigator().flyToPortal(portal, img, true);
            System.out.println(coordsToPortal);
            ShowImages.showWindow(ms.getBinary(), "Binary", true);
            new ImageViewer("Navigator", ImageViewer.CloseOperation.EXIT, img).show();
        }
    }
}