package taohuaan.metalslug;

import android.graphics.Bitmap;

/**
 * author: Runzhi on 2018/12/12.
 *
 * Bullet class
 */

public class Bullet {

    /**
     * Bullet bitmap x-coordinate and y-coordinate
     */
    private int x;//private int x = 0;
    private int y;

    /**
     *Direction of bullet launch
     */
    private int dir;

    /**
     * Member variable defining y-coordinate's accelerate.
     */
    private int yAccelerate = 0;

    /**
     * Checking if bullets is effective.
     */
    private boolean isEffect = false;

    /**
     * Constants of bullet type.
     */
    public static final int BULLET_TYPE_1 = 1;
    public static final int BULLET_TYPE_2 = 2;
    public static final int BULLET_TYPE_3 = 3;
    public static final int BULLET_TYPE_4 = 4;

    /**
     *Member variable defining bullet type.
     */
    private int type = 0;

    /**
     *Member method to assign and retrieve class's member attribute variable.
     */
    public int getDir(){
        return dir;
    }
    public void setDir(int dir){
        this.dir = dir;
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public boolean isEffect(){
        return isEffect;
    }
    public void setEffect(boolean ef){
        this.isEffect = ef;
    }
    public int getAccelerate(){
        return yAccelerate;
    }
    public void setyAccelerate(int sp){
        this.yAccelerate = sp;
    }
    public int getBulletType(){
        return type;
    }
    public void setBulletType(int ty){
        this.type = ty;
    }


    /**
     * Construction.
     *
     * @param bulletType    type of bullet
     * @param drawX         x-coordinate of bullet bitmap on canvas
     * @param drawY         y-coordinate of bullet bitmap on canvas
     * @param dir           direction of bullet launch
     */
    public Bullet(int bulletType, int drawX, int drawY, int dir){

        this.type = bulletType;
        this.x = drawX;
        this.y = drawY;
        this.dir = dir;

    }


    /**
     * Get a image of Bullet object.
     *
     * @return     instance object of Bitmap.
     */
    public Bitmap getBitmap(){

        switch(type){
            case BULLET_TYPE_1:
                return ViewManager.bulletImage[0];
            case BULLET_TYPE_2:
                return ViewManager.bulletImage[1];
            case BULLET_TYPE_3:
                return ViewManager.bulletImage[2];
            case BULLET_TYPE_4:
                return ViewManager.bulletImage[3];
            default:
                return null;
        }

    }


    /**
     * Return horizontal speeds of monster movement
     *
     * @return  int  speeds of movement
     */
    public int getXSpeed(){

        int dirBullet = dir == Player.DIR_RIGHT ? 1 : -1;
        switch(type){
            case BULLET_TYPE_1:
                return (int)(ViewManager.scale * 12 * dirBullet);
            case BULLET_TYPE_2:
                return (int)(ViewManager.scale * 8 * dirBullet);
            case BULLET_TYPE_3:
                return (int)(ViewManager.scale * 8 * dirBullet);
            case BULLET_TYPE_4:
                return (int)(ViewManager.scale * 8 * dirBullet);
            default:
                return (int)(ViewManager.scale * 8 * dirBullet);
        }

    }


    /**
     * Getting vertical speed of monster movement.
     *
     * @return  int  speeds of movement
     */
    public int getYSpeed(){

        if(yAccelerate != 0)
            return yAccelerate;
        switch(type){
            case BULLET_TYPE_1:
                return 0;
            case BULLET_TYPE_2:
                return 0;
            case BULLET_TYPE_3:
                return (int)(ViewManager.scale * 6);
            case BULLET_TYPE_4:
                return 0;
            default:
                return 0;
        }

    }


    /**
     * Moving bullets, include left shift and right shift.
     */
    public void move(){

        x += getXSpeed();
        y += getYSpeed();

    }


}
