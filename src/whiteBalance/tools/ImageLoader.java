package whiteBalance.tools;

import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
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
}