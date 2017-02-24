package whiteBalance.tools;

import georegression.struct.shapes.EllipseRotated_F64;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.geometry.Point3D;

/**
 * Navigator
 * @author Lasse
 * @version 23-02-2017
 */
public class Navigator 
{
    
    public Point3D flyToPortal(EllipseRotated_F64 portal, Dimension imgDim) {
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
    
    public Point3D flyToPortal(EllipseRotated_F64 portal, BufferedImage img, boolean draw) {
        Point3D coords = flyToPortal(portal, new Dimension(img.getWidth(), img.getHeight()));
        
        if(draw) {
            Graphics2D g2 = img.createGraphics();
            g2.setStroke(new BasicStroke(2));
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            
            //Target
            g2.setColor(Color.GREEN);
            g2.drawRect((int) (portal.center.x - portal.b), (int) (portal.center.y - portal.a), (int) portal.b * 2, (int) portal.a * 2);
            g2.drawLine((int) portal.center.x, 0, (int) portal.center.x, img.getWidth());
            g2.drawLine(0, (int) portal.center.y, img.getWidth(), (int) portal.center.y);
            
            //Center
            g2.setColor(Color.RED);
            g2.drawLine(0, img.getHeight() / 2, img.getWidth(), img.getHeight() / 2);
            g2.drawLine(img.getWidth() / 2, 0, img.getWidth() / 2, img.getHeight());
            
            //Error
            g2.setColor(Color.ORANGE);
            g2.drawString("x_err=" + coords.getX(), (int) (img.getWidth() / 2 + coords.getX()) + 5, 45);
            g2.drawLine(img.getWidth() / 2, 50, (int) (img.getWidth() / 2 + coords.getX()), 50);
            g2.drawString("y_err=" + coords.getY(), 55, (int) (img.getHeight() / 2 + coords.getY()) - 5);
            g2.drawLine(50, img.getHeight() / 2, 50, (int) (img.getHeight() / 2 + coords.getY()));
            g2.drawString("z_err=" + coords.getZ(), 25, 25);
        }
        
        return coords;
    }
}