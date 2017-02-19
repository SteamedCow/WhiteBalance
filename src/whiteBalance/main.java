package whiteBalance;

import whiteBalance.tools.WhiteBalance;
import boofcv.gui.image.ShowImages;
import whiteBalance.exceptions.DetectionException;
import whiteBalance.tools.Calibrator;

/**
 * main
 * @author Lasse
 * @version 10-02-2017
 */
public class main
{
    public static void main(String[] args) throws DetectionException {
        int run = 0;
        int minSize = 3, imgMaxSize = 1000;
        String filePath = "D:\\Programmer\\NetBeans 8.0.2\\My Javas\\CDIO\\White Balance\\White Balance\\";
        
        String fileName;
        switch (run) {
            default:
            case 0: fileName = "WBColorChart - red.jpg"; break;
            case 1: fileName = "20170210_182415.jpg"; break;
            case 2: fileName = "20170210_182447.jpg"; break;
            case 3: fileName = "20170210_182605.jpg"; break;
            case 4: fileName = "20170210_182429.jpg"; break;
            case 5: fileName = "20170210_182440.jpg"; break;
            case 6: fileName = "20170210_182442.jpg"; break;
            case 7: fileName = "20170210_182606.jpg"; break;
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
}