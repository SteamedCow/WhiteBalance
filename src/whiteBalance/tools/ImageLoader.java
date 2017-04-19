package whiteBalance.tools;

import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * ImageLoader
 * @author Lasse
 * @version 19-02-2017
 */
public class ImageLoader
{
    public static BufferedImage load(String filePath, int maxSize) {
        BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample(filePath));
        
        if(maxSize > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            
            int scale = 1;
            if(width > maxSize || height > maxSize) {
                if(width > height)
                    scale = width / maxSize;
                else if(height >= width)
                    scale = height / maxSize;
                
                width /= scale;
                height /= scale;
                image = resize(image, width, height);
            }
        }
        return image;
    }
    
    public static BufferedImage load(String filepath) {
        return load(filepath, -1);
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return dimg;
    }
    
    public static BufferedImage colorFilter(BufferedImage image) {
        Color col;
        float hsb[];
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                col = new Color(image.getRGB(x, y));
                hsb = new float[3];
                Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), hsb);
                
                float deg = hsb[0] * 360;
                if (hsb[1] > 0.2 && hsb[2] < 0.8 && hsb[2] > 0.1)
                    if ((deg >=   0 && deg <  20) || (deg >= 330 && deg < 360))
                        image.setRGB(x, y, 0);
                    else
                        image.setRGB(x, y, 16777214);
                else
                    image.setRGB(x, y, 16777214);
            }
        }
        
        return image;
    }
    
    public static int setColors(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        
        return rgb;
    }
}