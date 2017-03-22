package whiteBalance.tools.geom;

import georegression.struct.point.Point2D_I32;

/**
 * DFMPoint2D_I32
 * @author SteamedCow
 * @version 16-03-2017
 */
public class DFMPoint2D_I32 extends Point2D_I32
{
    public DFMPoint2D_I32() {
        this.x = 0;
        this.y = 0;
    }
    
    public DFMPoint2D_I32(int x, int y) {
        super(x, y);
    }

    public DFMPoint2D_I32(Point2D_I32 orig) {
        super(orig);
    }
    
    public DFMPoint2D_I32 add(Point2D_I32 other) {
        return new DFMPoint2D_I32(this.x + other.x, this.y + other.y);
    }
    
    public DFMPoint2D_I32 addSave(Point2D_I32 other) {
        this.x += other.x;
        this.y += other.y;
        
        return this;
    }
    
    public DFMPoint2D_I32 divide(int n) {
        return new DFMPoint2D_I32(this.x/n, this.y/n);
    }
    
    public DFMPoint2D_I32 divideSave(int n) {
        this.x /= n;
        this.y /= n;
        return this;
    }
    
    public DFMPoint2D_I32 add(DFMPoint2D_I32 other) {
        return add((Point2D_I32) other);
    }
    
    public DFMPoint2D_I32 addSave(DFMPoint2D_I32 other) {
        return addSave((Point2D_I32) other);
    }
}