package navigation.tools;

import java.util.ArrayList;
import java.util.Map;
import javafx.geometry.Point2D;
import org.bridj.util.Pair;

/**
 * DistanceMeaure
 * @author Lasse
 * @version 06-03-2017
 */
public class DistanceMeaure
{
    /**
     * Computes the distance from the camera to an object by knowing the objects real height.<br>
     * The distance is found by the equation <code>distanceToObject=(c * realHeight * imageHeight) / objectHeight</code>
     * @param imageHeight
     *      Height of image in pixels
     * @param objectHeight
     *      Height of the object in the picture in pixels
     * @param realHeight
     *      The real life height of the object in milimeters
     * @param cameraConstant
     *      Constant that expres the configuration of the camera lense (c=focalLength/heightOfSensor)
     * @return
     *      Distance to object in mm
     */
    public static double getDistanceToObject(double imageHeight, double objectHeight, double realHeight, double cameraConstant) {
        return (cameraConstant * realHeight * imageHeight) / objectHeight;
    }
    
    /**
     * Get camera constant c used to compute distance to objects in pictures.<br>
     * The camera constant is an expression for <code>c=f/sh</code> where f is the cameras focal length and sh is the height of the cameras sensor.<br>
     * The constant is calculated with the equation <code>c=(distanceToObject * objectHeight)/(imageHeight * realHeight)</code>
     * @param imageHeight
     *      Height of image in pixels
     * @param objectHeight
     *      Height of the object in the picture in pixels
     * @param realHeight
     *      The real life height of the object in milimeters
     * @param distanceToObject
     *      The real life distance to the object in milimeters
     * @return
     *      Cameraconstant
     */
    public static double getCameraConstant(double imageHeight, double objectHeight, double realHeight, double distanceToObject) {
        return (distanceToObject * objectHeight) / (imageHeight * realHeight);
    }
    
    /**
     * Compute the height of the cameras sensor in mm
     * @param imageHeight
     *      Height of image in pixels
     * @param objectHeight
     *      Height of the object in the picture in pixels
     * @param realHeight
     *      The real life height of the object in milimeters
     * @param distanceToObject
     *      The real life distance to the object in milimeters
     * @param focalLength
     *      Focal length of the camera in mm
     * @return
     *      Height of the cameras sensor in mm
     */
    public static double getSensorHeigth(double imageHeight, double objectHeight, double realHeight, double distanceToObject, double focalLength) {
        return (focalLength * imageHeight * realHeight) / (distanceToObject * objectHeight);
    }
    
    /**
     * Compute possible focal length and sensor height combinations
     * @param focalLengthMin
     *      Minimum focal length in mm
     * @param focalLengthMax
     *      Maximum focal length in mm
     * @param increment
     *      Increment between focal lengths
     * @param imageHeight
     *      The real life height of the object in milimeters
     * @param objectHeight
     *      Height of the object in the picture in pixels
     * @param realHeight
     *      The real life height of the object in milimeters
     * @param distanceToObject
     *      The real life distance to the object in milimeters
     * @return
     *      A list of resulting focal length and sensor height sets
     */
    public static ArrayList<Point2D> getFocalLenghtSensorHeightPoints(double focalLengthMin, double focalLengthMax, double increment, double imageHeight, double objectHeight, double realHeight, double distanceToObject) {
        ArrayList<Point2D> focalLengthSensorHeightList = new ArrayList<>();
        
        for(double f = focalLengthMin; f < focalLengthMax; f += increment)
            focalLengthSensorHeightList.add(new Point2D(f, getSensorHeigth(f, imageHeight, objectHeight, distanceToObject, realHeight)));
        
        return focalLengthSensorHeightList;
    }
    
    public static Map.Entry findBestResult(int size, ArrayList<Point2D>... lists) {
        //Check list sizes
        for (ArrayList<Point2D> list : lists)
            if(size != list.size())
                throw new IndexOutOfBoundsException("Lists must have eawual sizes");
        
        Map.Entry<Integer, Double> bestResult = new Pair<>(-1, 1.0 * Integer.MAX_VALUE);
        double oldSum = Integer.MIN_VALUE, sum, sumDiff, diffTot;
        for (int i = 0; i < size; i++) {
            sumDiff = 0;
            sum = 0;
            for (ArrayList<Point2D> list : lists) {
                sum += list.get(i).getX() + list.get(i).getY();
                if(oldSum != Integer.MIN_VALUE) {
                    sumDiff += oldSum - sum;
                }
                oldSum = sum;
            }
            diffTot = sumDiff / (lists.length - 1);
            if(Math.abs(diffTot) < Math.abs(bestResult.getValue()))
                bestResult = new Pair<>(i, diffTot);
        }
        
        return bestResult;
    }
}