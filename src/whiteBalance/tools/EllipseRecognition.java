package whiteBalance.tools;

import georegression.struct.point.Point2D_I32;
import java.util.List;
import whiteBalance.tools.geom.DFMPoint2D_I32;

/**
 * EllipseRecognition
 * @author Lasse
 * @version 28-03-2017
 */
public class EllipseRecognition 
{
    public DFMPoint2D_I32[] findCenters(List<Point2D_I32> contour) {
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
        
        DFMPoint2D_I32[] result = new DFMPoint2D_I32[3];
        result[0] = centers[0];
        if(centers[1].distance(centers[0]) + centers[2].distance(centers[0]) > centers[3].distance(centers[0]) + centers[4].distance(centers[0])) {
            result[1] = centers[1];
            result[2] = centers[2];
        }
        else {
            result[1] = centers[3];
            result[2] = centers[4];
        }
        
        return result;
    }

    public double findAngle(Point2D_I32[] centers) {
        double theta1 = -Math.atan2(centers[1].y-centers[0].y, centers[1].x-centers[0].x);
        double theta2 = -Math.atan2(centers[0].y-centers[2].y, centers[0].x-centers[2].x);
        
        return Math.toDegrees((theta1 + theta2) / 2);
    }

    public double[] findMinorMajor(List<Point2D_I32> external, Point2D_I32 center, double angle) {
        throw new UnsupportedOperationException("Not supported yet.");
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
}