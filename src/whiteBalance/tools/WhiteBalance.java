package whiteBalance.tools;

import java.awt.image.BufferedImage;

/**
 * WhiteBalance
 * @author Lasse
 * @version 10-02-2017
 */
public final class WhiteBalance 
{
    private int rgb;
    
    public WhiteBalance() {
        setColors(0, 0, 0);
    }
    
    public WhiteBalance(int red, int green, int blue) {
        setColors(red, green, blue);
    }
    
    public void setColors(int red, int green, int blue) {
        rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
    }
    
    public void colorImage(BufferedImage image) {
        int newRGB;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                newRGB = image.getRGB(x, y) + rgb;
                image.setRGB(x, y, newRGB);
            }
        }
    }
}