package whiteBalance.exceptions;

import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Dialog
 * @author Lasse
 * @version 13-02-2017
 */
public class Dialog 
{
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    
    public static void info(String title, String msg, int type) {
        Dialog.info(title, msg, type, null);
    }
    
    public static boolean confirm(String title, String msg, int type) {
        return Dialog.confirm(title, msg, type, null);
    }
    
    public static int multiOpt(String title, String msg, int type, String... buttons) {
        return Dialog.multiOpt(title, msg, type, null, buttons);
    }
    
    public static Object chooseObj(String title, String msg, int type, Object... options) {
        return Dialog.chooseObj(title, msg, type, null, options);
    }
    
    public static String input(String title, String msg, int type, String defaultInput) {
        return Dialog.input(title, msg, type, defaultInput, null);
    }
    
    public static void info(String title, String msg, int type, BufferedImage img) {
        if(img == null)
            JOptionPane.showMessageDialog(null, msg, title, type);
        else {
            JDialog dialog = new JOptionPane(new DialogPane(img, msg), type).createDialog(title);
            dialog.setVisible(true);
            dialog.dispose();
        }
    }
    
    public static boolean confirm(String title, String msg, int type, BufferedImage img) {
        if(img == null)
            return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, type) == 0;
        else {
            JOptionPane pane = new JOptionPane(new DialogPane(img, msg), type, JOptionPane.YES_NO_OPTION);
            JDialog dialog = pane.createDialog(title);
            dialog.setVisible(true);
            dialog.dispose();
            
            return (int) pane.getValue() == 0;
        }
    }
    
    public static int multiOpt(String title, String msg, int type, BufferedImage img, String... buttons) {
        return -1;
    }
    
    public static Object chooseObj(String title, String msg, int type, BufferedImage img, Object... options) {
        return null;
    }
    
    public static String input(String title, String msg, int type, String defaultInput, BufferedImage img) {
        return null;
    }
}