package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Point2D;
import javax.swing.JPanel;

/**
 * GraphPanel
 * @author Lasse
 * @version 05-03-2017
 */
public class GraphPanel extends JPanel
{
    private double xMin = 0, xMax = 10, yMin = 0, yMax = 10;
    private double scaleX, scaleY;
    private Point2D origo = new Point2D(0, 0);
    private final static int marginButtom = 10;
    
    private final HashMap<ArrayList<Point2D>, Color> polyLines = new HashMap<>();
    
    public GraphPanel(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;        
    }
    
    public void addPolyLine(ArrayList<Point2D> points, Color color) {
        if(polyLines.containsKey(points))
            System.err.println("Already contains line" + points.toString());
        else
            polyLines.put(points, color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        scaleX = getWidth() / (xMax - xMin);
        scaleY = getHeight() / (yMax - yMin);
        
        //Axis
        g.drawLine(0, getHeight() - marginButtom, getWidth(), getHeight() - marginButtom);
        for(double x = xMin; x < xMax; x += xMax / 10)
            g.drawString("" + x, (int) (x * scaleX), getHeight() - marginButtom);
        
        g.drawLine(0, 0, 0, getHeight() - marginButtom);        
        for(double y = yMax; y > yMin; y -= yMax / 10)
            g.drawString("" + (yMax - y), 0, (int) (y * scaleY));
        
        for (ArrayList<Point2D> polyLine : polyLines.keySet()) {
            System.out.println("Drawing poly line: " + polyLine.toString());
            g.setColor(polyLines.get(polyLine));
            
            Point2D oldP = null;
            for (Point2D p : polyLine) {
                if(oldP == null)
                    oldP = transform(p);
                else {
                    p = transform(p);
                    g.drawLine((int) oldP.getX(), (int) oldP.getY(), (int) p.getX(), (int) p.getY());
                    oldP = p;
                }
            }
        }
    }
    
    private Point2D transform(Point2D p) {
        return new Point2D(p.getX() * scaleX, getHeight() - marginButtom - p.getY() * scaleY);
    }
}