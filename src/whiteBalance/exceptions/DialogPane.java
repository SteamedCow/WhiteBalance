package whiteBalance.exceptions;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * DialogPane
 * @author Lasse
 * @version 16-02-2017
 */
public class DialogPane extends JPanel
{
    private final BufferedImage img;
    private final String msg;
    private boolean boot = true;
    private int msgWidth;
    
    public DialogPane(BufferedImage img, String message) {
        this.img = img;
        this.msg = message;
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if(boot) {
            g.setFont(new Font("ariel", Font.BOLD, 20));
            msgWidth = g.getFontMetrics().stringWidth(msg);
            g.setColor(Color.blue);
            boot = false;
        }
        
        g.drawImage(img, 0, 0, null);
        g.drawString(msg, (getWidth() - msgWidth)/2, getHeight());
    }
}