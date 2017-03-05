package whiteBalance;

import java.util.ArrayList;
import java.util.Map.Entry;
import javafx.geometry.Point2D;
import navigation.tools.DistanceMeaure;
import org.bridj.util.Pair;

/**
 * CameraFocalLength
 * @author Lasse
 * @version 05-03-2017
 */
public class CameraFocalLength 
{
    public static void main(String[] args) {
//        double min = 10, max = 500, incr = 1;
        
        double c1 = DistanceMeaure.getCameraConstant(495, 243, 300, 1000);
        double c2 = DistanceMeaure.getCameraConstant(495, 204, 500, 2000);
        double c3 = DistanceMeaure.getCameraConstant(495, 270, 1000, 3000);
        
        System.out.println(c1 + ", " + c2 + ", " + c3 + " -> " + (c1 + c2 + c3) / 3);
        
//        ArrayList<Point2D> l_m1 = dm_m1.getFocalLenghtSensorHeightPoints(min, max, incr);
//        ArrayList<Point2D> l_m2 = dm_m2.getFocalLenghtSensorHeightPoints(min, max, incr);
//        ArrayList<Point2D> l_m3 = dm_m3.getFocalLenghtSensorHeightPoints(min, max, incr);
        
//        GraphPanel graph = new GraphPanel(min, max, 6, 300);
//        graph.addPolyLine(l_m1, Color.RED);
//        graph.addPolyLine(l_m2, Color.BLUE);
//        graph.addPolyLine(l_m3, Color.GREEN);
        
//        Entry best = findBestResult(l_m1.size(), l_m1, l_m2, l_m3);
//        System.out.println("Best: " + best);
//        
//        double best1 = l_m1.get((int) best.getKey() - 10).getY();
//        double best2 = l_m2.get((int) best.getKey() - 10).getY();
//        double best3 = l_m3.get((int) best.getKey() - 10).getY();
        
//        System.out.println(best1 + ", " + best2 + ", " + best3 + " -> " + (best1+best2+best3) / 3);
        
//        JFrame frame = new JFrame("Graph");
//        frame.setSize(700, 450);
//        frame.setContentPane(graph);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
    }
}