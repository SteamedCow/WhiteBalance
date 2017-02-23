package whiteBalance.tools;

import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.Dimension;
import javafx.geometry.Point3D;

/**
 * Navigator
 * @author Lasse
 * @version 23-02-2017
 */
public class Navigator 
{
    public static Point3D flyToPortal(EllipseRotated_F64 portal, Dimension imgDim) {
        double x_ideal = imgDim.width / 2.0;
        double y_ideal = imgDim.height / 2.0;
        double area_ideal = imgDim.width * imgDim.height * 0.3;
        
        double x_err = portal.center.x - x_ideal;
        double y_err = portal.center.y - y_ideal;
        double z_err = area_ideal - (portal.a * portal.b);
        
        /**
            z_err < 0 : means bot is too close and needs to slow down, Vz should be reduced
            z_err = 0 : keep the speed command the same, no change
            z_err > 0 : we need to get closer, Vz should increase

            x_err < 0 : means bot is to the right and needs to turn left(decreasing x), Vx should be reduced
            x_err = 0 : keep the speed in X the same, no change to Vx
            x_err > 0 : means bot is to the left and needs to turn right(increasing x), Vx should be increased
         */
        
        return new Point3D(x_err, y_err, z_err);
    }
}