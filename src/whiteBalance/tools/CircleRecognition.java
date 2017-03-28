package whiteBalance.tools;

import georegression.struct.point.Point2D_I32;
import java.util.List;
import whiteBalance.tools.geom.DFMPoint2D_I32;

/**
 * CircleRecognition
 * @author Lasse
 * @version 28-03-2017
 */
public class CircleRecognition 
{
    public DFMPoint2D_I32 findCenter(List<Point2D_I32> contour) {
        DFMPoint2D_I32 sum = new DFMPoint2D_I32();
        for (Point2D_I32 p : contour)
            sum.addSave(p);
        
        return sum.divide(contour.size());
    }
    
    public double findAverageDistance(List<Point2D_I32> contour, Point2D_I32 center) {
        double radius = 0;
        for (Point2D_I32 p : contour)
            radius += p.distance(center);
        
        return radius / contour.size();
    }
    
    public double findAverageError(List<Point2D_I32> contour, Point2D_I32 center, double radius) {
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
}