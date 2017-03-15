package whiteBalance.tools;

import boofcv.gui.image.ShowImages;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.ddogleg.struct.FastQueue;
import whiteBalance.exceptions.DetectionException;
import whiteBalance.exceptions.Dialog;

/**
 * Calibrator
 * @author Lasse
 * @version 19-02-2017
 */
public class Calibrator 
{
    private final Measure measure;
    private boolean verbose = true;
    
    public Calibrator(BufferedImage image, boolean verbose) {
        this.verbose = verbose;
        measure = new Measure(image, verbose);
    }
    
    public Calibrator(String filePath, boolean verbose) {
        this.verbose = verbose;
        measure = new Measure(filePath, verbose);
    }
    
    public Calibrator(String filePath, int imageMaxSize, boolean verbose) {
        this.verbose = verbose;
        measure = new Measure(filePath, imageMaxSize, verbose);
    }
    
    public Integer[] calibrate(int ellipseMinSize) throws DetectionException {
        if(verbose)
            System.out.println("White balance calibration started.. verbose=" + verbose);
        
        //Find ellipses (over ellipseMinSize)
        if(verbose)
            System.out.println("Finding ellipses..");
        FastQueue<EllipseRotated_F64> ellipses = measure.findEllipses(ellipseMinSize, true);
        if(verbose)
            System.out.println("    Found " + ellipses.size + " items: " + Arrays.toString(ellipses.getData()));
        
        //Finding corners
        if(verbose)
            System.out.println("Finding corners..");
        if(ellipses.size == 4) { //4 corner success
            Point2D_F64[] corners = measure.computeCorners(ellipses);
            if(verbose)
                System.out.println("    Corners: " + Arrays.toString(corners));
            if(!Dialog.confirm("Confirm Corners", "Are the four cicles marked?", Dialog.PLAIN_MESSAGE, measure.getImage())) {
                Dialog.info("Failed To Calibrate", "The image was not sufficient enough to calibrate\nPlease try again", Dialog.ERROR_MESSAGE);
                if(verbose)
                    System.out.println("Calibration canceled!");
                return null;
            }
            else {
                //Computing color offset
                if(verbose)
                    System.out.println("Computing color offset..");
                Integer[] offset = measure.computeColorOffset(corners, false);
                if(!Dialog.confirm("Confirm Samples", "Are the marked squares within the color squares?", Dialog.PLAIN_MESSAGE, measure.getImage())) {
                    Dialog.info("Failed To Calibrate", "The image was not sufficient enough to calibrate\nPlease try again", Dialog.ERROR_MESSAGE);
                    if(verbose)
                        System.out.println("Calibration canceled!");
                    return null;
                }
                else {
                    Dialog.info("Calibration Complete!", "The offset was computed to r=" + offset[0] + ", g=" + offset[1] + ", b=" + offset[2], Dialog.INFORMATION_MESSAGE);
                    if(verbose)
                        System.out.println("Calibration complete!");
                    return offset;
                }
            }
        }
        else {
            ShowImages.showWindow(measure.getImage(), "Corners Conflict", true);
            if(verbose)
                System.out.println("Corners Conflict.. Aborted");
            return null;
        }
    }
    
    public BufferedImage getImage() {
        return measure.getImage();
    }
    
    public GrayU8 getBinary() {
        return measure.getBinary();
    }
}