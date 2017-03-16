package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * ImageViewer
 * @author Lasse
 * @version 24-02-2017
 */
public class ImageViewer extends JFrame
{    
    public static enum CloseOperation {
        EXIT, DISPSE, DO_NOTHING;
    }
    
    public ImageViewer(String title, CloseOperation defaultCloseOperation, BufferedImage img) {
        int dco;
        switch (defaultCloseOperation) {
            default:
            case EXIT: dco = JFrame.EXIT_ON_CLOSE; break;
            case DISPSE: dco = JFrame.DISPOSE_ON_CLOSE; break;
            case DO_NOTHING: dco = JFrame.DO_NOTHING_ON_CLOSE; break;
        }
        
        setTitle(title);
        setDefaultCloseOperation(dco);
        setSize(img.getWidth() + 16, img.getHeight() + 39);
        
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
        });
    }
    
    public static void show(String title, CloseOperation defaultCloseOperation, BufferedImage img) {
        int dco;
        switch (defaultCloseOperation) {
            default:
            case EXIT: dco = JFrame.EXIT_ON_CLOSE; break;
            case DISPSE: dco = JFrame.DISPOSE_ON_CLOSE; break;
            case DO_NOTHING: dco = JFrame.DO_NOTHING_ON_CLOSE; break;
        }
        
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(dco);
        frame.setSize(img.getWidth(), img.getHeight());
        
        frame.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
        });
        
        frame.setVisible(true);
    }
}