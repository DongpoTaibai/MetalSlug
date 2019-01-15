package taohuaan.metalslug;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Runzhi on 2018/12/10.
 * define a monster class, about relative attributes and methods
 */
public class Monster {

    /**
     * Define instant variable
     */
    public static final int TYPE_BOMB = 1;
    public static final int TYPE_FLY = 2;
    public static final int TYPE_MAN = 3;

    /**
     * Member variable defining monster type
     */
    private int type = TYPE_BOMB;

    /**
     * Member variable defining position of monster
     */
    private int x = 0;
    private int y = 0;

    /**
     * Member variable identifying whether the monster is dead or not
     */
    private boolean isDie = false;

    /**
     * Member variable defining starting and ending positions of game pictures.
     */
    private int startX = 0;
    private int startY = 0;
    private int endX = 0;
    private int endY = 0;

    /**
     *Member variable defining speed of animation refresh.
     */
    private int drawCount = 0;

    /**
     * Define a variable for the frame in which the current
     * animation is drawing.
     */
    private int drawIndex = 0;

    /**
     * Member variable that do not play the dead animation.
     */
    private int dieMaxDrawCount = Integer.MAX_VALUE;

    /**
     * List collection of bullets.
     */
    private List<Bullet> bulletList = new ArrayList<>();


    /**
     *  Constructor, initializing position according to monster type
     *
     *  @param type  value of monster type
     */
    public Monster(int type){

        this.type = type;
        if(TYPE_MAN == type || TYPE_BOMB == type)
            y = Player.Y_DEFAULT;
        else
            if(TYPE_FLY == type)
                y = ViewManager.SCREEN_HEIGHT * 50 /100 -
                        Util.randomIntRange((int)ViewManager.scale * 150);
        x = Util.randomIntRange(ViewManager.SCREEN_WIDTH >> 1);

    }


    /**
     *Judging whether the monster was hit by a bullet
     */
    public boolean isHurt(int x, int y){

        return x >= startX && x <= endX
                && y >= startY && y <= endY;
    }


    /**
     * Call the corresponding method and pass the corresponding
     * parameters according to the type of monster.
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {

        if (null == canvas)
            return;
        switch (type) {
            case TYPE_BOMB:
                drawAni(canvas, isDie ? ViewManager.bomb2Image : ViewManager.bombImage);
                break;
            case TYPE_FLY:
                drawAni(canvas, isDie ? ViewManager.flyDieImage : ViewManager.flyImage);
                break;
            case TYPE_MAN:
                drawAni(canvas, isDie ? ViewManager.manDieImage : ViewManager.manImage);
                break;
            default:
                break;
        }

    }


    /**
     *Draw the animation according to the parameter.
     *
     * @param canvas
     * @param bitmapArray   the all animation frame
     */
    public void drawAni(Canvas canvas, Bitmap[] bitmapArray){

        if(canvas == null)
            return;
        if(bitmapArray == null)
            return;

        if(isDie && dieMaxDrawCount == Integer.MAX_VALUE){
            dieMaxDrawCount = bitmapArray.length;
        }

        drawIndex = drawIndex % bitmapArray.length;
        Bitmap bitmap = bitmapArray[drawIndex];
        if(bitmap == null || bitmap.isRecycled())
            return;

        int drawX = x;
        if(isDie){
            if(type == TYPE_BOMB)
                drawX = x - (int)(ViewManager.scale * 50);
            else if(type == TYPE_MAN)
                drawX = x + (int)(ViewManager.scale * 50);
        }
        int drawY = y - bitmap.getHeight();
        Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), Graphics.TRANS_NONE, drawX, drawY, 0, Graphics.TIMES_SCALE);

        startX = drawX;
        startY = drawY;
        endX = drawX + bitmap.getWidth();
        endY = drawY + bitmap.getHeight();
        drawCount++;

        if(drawCount >= (type == TYPE_MAN ? 6 : 4)){
            if(type == TYPE_MAN && drawIndex == 2)
                addBullet();
            else if(type == TYPE_FLY && drawIndex == bitmapArray.length - 1)
                addBullet();
            drawIndex++;
            drawCount = 0;
        }

        if(isDie)
            dieMaxDrawCount--;

        drawBullet(canvas);

    }


    /**
     * Bullet launch
     */
    public void addBullet(){

        int bulletType = getBulletType();
        if(0 >= bulletType)
            return;

        int drawX = x;
        int drawY;
        if(type == TYPE_FLY)
            drawY = y - (int)(ViewManager.scale * 30);
        else
            drawY = y - (int)(ViewManager.scale * 60);
        Bullet bullet = new Bullet(bulletType, drawX, drawY, Player.DIR_LEFT);
        bulletList.add(bullet);

    }


    /**
     * Select the type of bullet according to the type of monster, and return type.
     *
     * @return int
     *          zero, indicate that no bullet
     */
    public int getBulletType(){

        switch(type){
            case TYPE_BOMB:
                return 0;
            case TYPE_FLY:
                return Bullet.BULLET_TYPE_3;
            case TYPE_MAN:
                return Bullet.BULLET_TYPE_2;
            default:
                return 0;
        }

    }


    /**
     * Left shift all bullets, monsters, the same y coordinates and x coordinates minus distance of left shift.
     *
     * @param shift     a distance that the class of player right shift.
     */
    public void updateShift(int shift){

        x -= shift;
        for(Bullet bullet : bulletList){
            if(null == bullet)
                continue;

            bullet.setX(bullet.getX() - shift);
        }

    }


    /**
     * Draw all bullets, clear all invalid bullets and only draw other valid bullets.
     *
     * @param canvas
     */
    public void drawBullet(Canvas canvas){

        List<Bullet> deleteList = new ArrayList<>();
        Bullet bullet = null;
        for(int i = 0; i < bulletList.size(); i++){
            bullet = bulletList.get(i);
            if(null == bullet)
                continue;
            if(bullet.getX() < 0 || bullet.getX() > ViewManager.SCREEN_WIDTH)
                deleteList.add(bullet);
        }
        bulletList.removeAll(deleteList);

        Bitmap bitmap;
        for(int i = 0; i < bulletList.size(); i++){
            bullet = bulletList.get(i);
            if(null == bullet)
                continue;
            bitmap = bullet.getBitmap();
            if(null == bitmap)
                continue;
            bullet.move();
            Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    bullet.getDir() == Player.DIR_RIGHT ? Graphics.TRANS_MIRROR : Graphics.TRANS_NONE,
                    bullet.getX(), bullet.getY(), 0, Graphics.TIMES_SCALE);
        }

    }


    /**
     * When monsters's bullet hits players
     */
    public  void checkBullet(){

    }


    /**
     *Define member methods to assign and retrieve member attribute variables.
     */
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return type;
    }

    public void setX(int x){
        this.x = x;
    }
    public int getX(){
        return x;
    }

    public void setY(int y){
        this.y = y;
    }
    public int getY(){
        return y;
    }

    public void setStartX(int x){
        this.startX = x;
    }
    public int getStartX(){
        return startX;
    }

    public void setStartY(int y){
        this.startY = y;
    }
    public int getStartY(){
        return startY;
    }

    public void setEndX(int x){
        this.endX = x;
    }
    public int getEndX(){
        return endX;
    }

    public void setEndY(int y){
        this.endY = y;
    }
    public int getEndY(){
        return endY;
    }

    public void setDie(boolean isDie){
        this.isDie = isDie;
    }
    public boolean getDie(){
        return isDie;
    }

    public void setDrawIndex(int index){
        this.drawIndex = index;
    }
    public int getDrawIndex(){
        return drawIndex;
    }

    public void setDieMaxDrawCount(int count){
        this.dieMaxDrawCount = count;
    }
    public int getDieMaxDrawCount(){
        return dieMaxDrawCount;
    }


}
