package taohuaan.metalslug;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * author:  Runzhi on 2018/12/11.
 * Draw game interface.
 */

public class Graphics {

    /**
     * The constants defining bitmap transfers
     */
    public static final int TRANS_NONE = 0;
    //public static final int TRANS_MIRROR_ROT180 = 1;
    //public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR        = 2;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_MIRROR_ROT90  = 7;
    public static final int TRANS_ROT180        = 3;
    public static final int TRANS_ROT270        = 6;
    public static final int TRANS_ROT90         = 5;

    /**
     * The constants defining scaling gradient
     */
    public static final float INTERVAL_SCALE = 0.05f;
    public static final int   TIMES_SCALE    = 20;

    /**
     * Rectangle of interception area on the source bitmap
     */
    //public static final Rect src = new Rect();
    private static final Rect src = new Rect();
    /**
     * Rectangle of target area(destination area) on the canvas
     */
    private static final Rect dst = new Rect();

    /**
     * Rectangle of the source bitmap
     */
    public static final RectF srcRect = new RectF();

    /**
     * Array of coordinate of rectangle on the source bitmap
     */
    public static final float[] pts = new float[8];


    /**
     * Draw a bitmap on canvas, including transform bitmap and don't.
     *
     * @param canvas        screen background
     * @param src           source bitmap
     * @param srcX          x-coordinate of starting point on the source bitmap
     * @param srcY          y-coordinate of staring point on the source bitmap
     * @param width         width of target area on the source bitmap
     * @param height        height of target area on the source bitmap
     * @param trans         bitmap of transfers method
     * @param drawX         x-coordinate of canvas(screen background)
     * @param drawY         y-coordinate of canvas
     * @param degree        degree of transfers
     * @param scale         value of scale
     */
    public synchronized static void drawMatrixImage(Canvas canvas, Bitmap src, int srcX, int srcY, int width, int height,
                                        int trans, int drawX, int drawY, int degree, int scale){

        if(canvas == null)
            return;
        if(src == null || src.isRecycled())
            return;
//        if(srcX + width >= src.getWidth() || srcY + height >= src.getHeight()){
//            width  = src.getWidth() - srcX;
//            height = src.getHeight() - srcY;
//        }
        int srcWidth  = src.getWidth();
        int srcHeight = src.getHeight();
        if(srcX + width > srcWidth)
            width = srcWidth - srcX;
        if(srcY + height > srcHeight)
            height = srcHeight - srcY;
        if(width <= 0 || height <= 0)
            return;

        int scaleX = scale;
        int scaleY = scale;
        int rotate = 0;
        switch(trans){
            case TRANS_MIRROR:
                scaleX = -scale;
                break;
            case TRANS_MIRROR_ROT180:
                scaleX = -scale;
                rotate = 180;
                break;
            case TRANS_MIRROR_ROT270:
                scaleX = -scale;
                rotate = 270;
                break;
            case TRANS_MIRROR_ROT90:
                scaleX = -scale;
                rotate = 90;
                break;
            case TRANS_ROT90:
                rotate = 90;
                break;
            case TRANS_ROT180:
                rotate = 180;
                break;
            case TRANS_ROT270:
                rotate = 270;
                break;
            default:
                break;
        }
        if(scale == TIMES_SCALE && rotate == 0 && degree == 0)
            drawImage(canvas, src, srcX, srcY, width, height, drawX, drawY);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleX * INTERVAL_SCALE, scaleY * INTERVAL_SCALE);
        matrix.postRotate(rotate);
        matrix.postRotate(degree);
        srcRect.set(srcX, srcY, srcX + width, srcY + height);
        matrix.mapRect(srcRect);
        matrix.postTranslate(drawX - srcRect.left, drawY - srcRect.top);

        pts[0] = srcX;
        pts[1] = srcY;
        pts[2] = srcX + width;
        pts[3] = srcY;
        pts[4] = srcX + width;
        pts[5] = srcY + height;
        pts[6] = srcX;
        pts[7] = srcY + height;
        matrix.mapPoints(pts);
        canvas.save();

        Path path = new Path();
        path.reset();
        path.moveTo(pts[0], pts[1]);
        path.lineTo(pts[2], pts[3]);
        path.lineTo(pts[4], pts[5]);
        path.lineTo(pts[6], pts[7]);
        path.close();
        canvas.clipPath(path);
        canvas.drawBitmap(src,matrix, null);
        canvas.restore();

    }


    /**
     * Drawing image , only don't rotate and scale.
     *
     * @param c         canvas drawing image
     * @param image     the source bitmap
     * @param destX     x-coordinate of interception area on the canvas
     * @param destY     y-coordinate of interception area on the canvas
     * @param srcX      x-coordinate of starting point on the source bitmap
     * @param srcY      y-coordinate of starting point on the source bitmap
     * @param width     width of destination on the source bitmap
     * @param height    height of target area on the source bitmap
     */
    public synchronized static void drawImage(Canvas c, Bitmap image, int destX, int destY,
                                              int srcX, int srcY, int width, int height){

        if(c == null)
            return;
        if(image == null || image.isRecycled())
            return;
        if(srcX == 0 && srcY == 0 && image.getWidth() <= width && image.getHeight() <= height){
            c.drawBitmap(image, destX, destY, null);
            return;
        }

        src.left  = srcX;
        src.top   = srcY;
        src.right = srcX + width;
        src.bottom= srcY + height;

        dst.left  = destX;
        dst.top   = destY;
        dst.right = destX + width;
        dst.bottom= destY + height;

        c.drawBitmap(image, src, dst, null);

    }


    /**
     * Drawing border-string on canvas
     *
     * @param c             drawing a string on canvas
     * @param borderColor   border's color
     * @param textColor     color of string
     * @param text          drawing string
     * @param x             x-coordinate
     * @param y             y-coordinate
     * @param borderWidth   border's width
     * @param mPaint        instance object of Paint class
     */
    public static void drawBorderString(Canvas c, int borderColor, int textColor,
                                        String text, int x, int y,
                                        int borderWidth, Paint mPaint){

        //Setting mPaint
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(Color.rgb((borderColor & 0xFF0000) >> 16, (borderColor & 0x00ff00) >> 8, (borderColor & 0x0000ff)));
        c.drawText(text, x, y, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.rgb((textColor & 0xFF0000) >> 16, (textColor & 0x00ff00) >> 8, (textColor & 0x0000ff)));
        c.drawText(text, x, y, mPaint);

    }


    /**
     *Scaling pictures proportionally, return a new bitmap
     *
     * @param   img       source pictures
     * @param   newWidth  generate new width
     * @param   newHeight generate new height
     *
     * @return  bitmap    scaled pictures
     */
    public static Bitmap scale(Bitmap img, float newWidth, float newHeight){

        if(img == null)
            return null;
        float width = img.getWidth();
        float height = img.getHeight();
        if(width == 0 || height == 0 || newWidth == 0 || newHeight == 0)
            return null;

        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / width , newHeight / height);
        try{
            return Bitmap.createBitmap(img, 0, 0, (int)width, (int)height, matrix, true);
        }catch(Exception e){
            return null;
        }

    }


}
