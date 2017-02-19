package whiteBalance.exceptions;

/**
 * DetectionException
 * @author Lasse
 * @version 10-02-2017
 */
public class DetectionException extends Exception
{
    public DetectionException(String message) {
        super("Could not detect shapes: " + message);
    }
}