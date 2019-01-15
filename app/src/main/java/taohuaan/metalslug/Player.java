package taohuaan.metalslug;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;
import java.util.ArrayList;

import static taohuaan.metalslug.ViewManager.head;
import static taohuaan.metalslug.ViewManager.soundMap;

/**
 * author: Runzhi on 2018/12/18.
 *
 * Game player role.
 */

public class Player {

    /**
     * Constants defining a maximum health point(life value).
     */
    public static final int MAX_HP = 500;

    /**
     * Member variable defining names of a instance object.
     */
    private String name = null;
    /**
     * Member variable defining health point of a instance object.
     */
    private int hp = 0;

    /**
     * Member variable that storage bullets of player launches.
     */
    private final List<Bullet> bulletList = new ArrayList<>();

    /**
     * Member variable defining default x-coordinate and y-coordinate of Player's instance object.
     */
    public static int Y_DEFAULT  = 0;
    public static int X_DEFAULT  = 0;
    public static int Y_JUMP_MAX = 0;

    /**
     * Member variable defining x-coordinate and y-coordinate of player.
     */
    private int x = -1;
    private int y = -1;

    /**
     * Member variable defining index of animation frame, including leg and head.
     */
    private int indexLeg = 0;
    private int indexHead = 0;
    /**
     * Global variable storage times of calling drawAni() method(function).
     */
    private int drawCount = 0;

    /**
     * Constants defining player's stand, run, jump etc actions.
     */
    public static final int ACTION_STAND_RIGHT = 1;
    public static final int ACTION_STAND_LEFT  = 2;
    public static final int ACTION_RUN_RIGHT   = 3;
    public static final int ACTION_RUN_LEFT    = 4;
    public static final int ACTION_JUMP_RIGHT  = 5;
    public static final int ACTION_JUMP_LEFT   = 6;
    private int action                         = ACTION_STAND_RIGHT;

    /**
     * Constants defining player's three movements, including stand ,right and left shift.
     */
    public static final int MOVE_STAND = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT  = 2;
    private int move                   = MOVE_STAND;

    /**
     * Variable defining whether or not the player jumps
     */
    private boolean isJump = false;

    /**
     * Constants defining player's current direction.
     */
    public static final int DIR_RIGHT = 1;
    public static final int DIR_LEFT  = 2; //public static final int MOVE_LEFT  = 2;

    /**
     * Global variable to storage the current player's image, include head and leg.
     */
    private Bitmap currentHeadImg = null;
    private Bitmap currentLegImg  = null;
    /**
     * Global variable to storage positions of the current head's image
     */
    private int currentDrawX      = 0;
    private int currentDrawY      = 0;

    /**
     *Global variable to storage numbers of the current player's shoot animation frame,
     * when a bullet is added, a value of 6(be equal to the value of player's shoot animation frame)
     * is assigned to the leftShootTime variable,
     * when leftShootTime variable is less than or equal to zero, add the next bullet.
     */
    private int leftShootTime = 0;
    private static final int MAX_SHOOT_TIME = 6;

    /**
     * Special whether jump highest
     */
    private boolean isJumpMax = false;
    /**
     * Times of stay on the highest jump position
     */
    private int stopTimeMax = 0;



    /**
     * Construction
     */
    public Player(String name, int hp){
        this.name = name;
        this.hp   = hp;
    }


    /**
     * Initialize position of player and vertical maximum jump.
     */
    public void InitPosition(){

        x          = ViewManager.SCREEN_WIDTH * 15 / 100;
        //y          = ViewManager.SCREEN_HEIGHT * 45 / 100;
        y          = ViewManager.SCREEN_HEIGHT * 72 / 100;
        X_DEFAULT  = x;
        Y_DEFAULT  = y;
        Y_JUMP_MAX = ViewManager.SCREEN_HEIGHT * 50 / 100;

    }

    /**
     *Getting horizontal shift value of player.
     */
    public int getShift(){

        if(x <= 0 || y <= 0)
            InitPosition();
        return X_DEFAULT - x;

    }


    /**
     * Getting current direction
     *
     * @return  direction's constants
     */
    public int getDir(){

        if(action % 2 == 1)
            return DIR_RIGHT;
        return DIR_LEFT;

    }


    /**
     * Updating position of bullet, left shift bullets.
     *
     * @param shift     value of shift
     */
    public void updateBulletShift(int shift){

        for(Bullet bullet : bulletList){
            if(bullet == null)
                continue;
            bullet.setX(bullet.getX() - shift);
        }

    }


    public List<Bullet> getBulletList(){
        return bulletList;
    }


    /**
     * Judging if the player has been bombed.
     *
     * @param startX    x-coordinate of monster bitmap's left/top corner
     * @param startY    y-coordinate of monster bitmap's left/top corner
     * @param endX      x-coordinate of monster bitmap's right/bottom corner
     * @param endY      y-coordinate of monster bitmap's right/bottom corner
     */
    public boolean isHurt(int startX, int startY, int endX, int endY){

        //if(startX <= getX() <= endX && startY <= getY() <= endY)

        if(currentHeadImg == null || currentLegImg == null)
            return false;

        //int playerStartX = X_DEFAULT - ((currentHeadImg.getWidth() - currentLegImg.getWidth())/2);
        //int playerStartY = Y_DEFAULT - currentLegImg.getHeight() - currentHeadImg.getHeight();
        int playerStartX = currentDrawX;
        int playerStartY = currentDrawY;
        int playerEndX   = playerStartX + currentHeadImg.getWidth();
        int playerEndY   = playerStartY + currentHeadImg.getHeight() + currentLegImg.getHeight();
       //return getX() >= startX && getX() <= endX&& getY() >= startY && getY() <= endY;

        //return startX >= playerStartX && startX <= playerEndX && startY >= playerStartY && startY <= playerEndY ||
//        return startX >= playerStartX && startX <= playerEndX ||
//                (endX >= playerStartX && endX <= playerEndX) && (startY >= playerStartY && startY <= playerEndY)
//                || endY >= playerStartY && endY <= playerEndY;
        return (((startX >= playerStartX && startX <= playerEndX) || (endX >= playerStartX && endX <= playerEndX))
                && ((startY >= playerStartY && startY <= playerEndY) || (endY >= playerStartY && endY <= playerEndY)));

    }


    /**
     * Drawing player, include various action and pass relative array
     * of bitmap, direction of player .eg parameters to drawAni() function.
     *
     * @param canvas  canvas to draw picture on background screen
     */

    public  void draw(Canvas canvas){

        switch(action){
            case ACTION_STAND_RIGHT:
                drawAni(canvas, ViewManager.legStandImage, ViewManager.headStandImage, DIR_RIGHT);
                break;
            case ACTION_STAND_LEFT:
                drawAni(canvas, ViewManager.legStandImage, ViewManager.headStandImage, DIR_LEFT);
                break;
            case ACTION_RUN_RIGHT:
                drawAni(canvas, ViewManager.legRunImage, ViewManager.headRunImage, DIR_RIGHT);
                break;
            case ACTION_RUN_LEFT:
                drawAni(canvas, ViewManager.legRunImage, ViewManager.headRunImage, DIR_LEFT);
                break;
            case ACTION_JUMP_RIGHT:
                drawAni(canvas, ViewManager.legJumpImage, ViewManager.headJumpImage, DIR_RIGHT);
                break;
            case ACTION_JUMP_LEFT:
                drawAni(canvas, ViewManager.legJumpImage, ViewManager.headJumpImage, DIR_LEFT);
                break;
            default:
                break;
        }

    }


    /**
     *Drawing an action's animations of player according to leg and head's picture, and direction.
     * First, draw leg, the draw head, finally, draw bullets and head on background screen.
     *
     * @param canvas    canvas of background screen
     * @param legArr    leg's action picture
     * @param headArr   head's action picture
     * @param dir       direction of player
     */
    public void drawAni(Canvas canvas, Bitmap[] legArr, Bitmap[] headArr, int dir){

        if(canvas == null)
            return;
        if(legArr == null || headArr == null)
            return;

        indexLeg    = indexLeg % legArr.length;
        Bitmap leg  = legArr[indexLeg];
        if(leg == null || leg.isRecycled())
            return;
        int drawX = X_DEFAULT;
        int drawY = y - leg.getHeight();
        int trans = (dir == DIR_RIGHT ? Graphics.TRANS_MIRROR : Graphics.TRANS_NONE);

        Graphics.drawMatrixImage(canvas, leg, 0, 0, leg.getWidth(), leg.getHeight(),
                                    trans, drawX, drawY, 0, Graphics.TIMES_SCALE);
        currentLegImg = leg;


        if(leftShootTime > 0){
            headArr = ViewManager.headShootImage;
            leftShootTime--;
        }
        indexHead     = indexHead % headArr.length;
        Bitmap head   = headArr[indexHead];
        if(head == null || head.isRecycled())
            return;
//        drawX = drawX - ((head.getWidth() - leg.getWidth()) >> 1);
        drawX = drawX -((head.getWidth() - leg.getWidth())/2);
        if(action == ACTION_STAND_LEFT)
            drawX += (int)(6 * ViewManager.scale);
        drawY = drawY - head.getHeight() + (int)(10 * ViewManager.scale);

        Graphics.drawMatrixImage(canvas, head, 0, 0, head.getWidth(), head.getHeight(),
                                    trans, drawX, drawY, 0, Graphics.TIMES_SCALE);
        currentHeadImg = head;
        currentDrawX   = drawX;
        currentDrawY   = drawY;

        drawCount++;
        if(drawCount >= 4){
            drawCount = 0;
            indexHead++;
            indexLeg++;
        }

        drawBullet(canvas);
        drawHead(canvas);

    }



    /**
     * Drawing bullet of players
     *
     * @param canvas   canvas of background screen
     */
    public void drawBullet(Canvas canvas){

        if(canvas == null)
            return;

        Bullet bullet;
        List<Bullet> delList = new ArrayList<>();
        for(int i = 0; i < bulletList.size(); i++){
            bullet = bulletList.get(i);
            if(bullet == null)
                continue;
            if(bullet.getX() < 0 || bullet.getX() > ViewManager.SCREEN_WIDTH)//if(bullet.getX()<0 || bullet.getX()>ViewManager.SCREEN_WIDTH
                // || bullet.getY()<0 || bullet.getY()>ViewManager.SCREEN_HEIGHT )
                delList.add(bullet);
        }
        bulletList.removeAll(delList);

        Bitmap img;
        for(int i = 0; i < bulletList.size(); i++){
            bullet = bulletList.get(i);
            if(bullet == null)
                continue;
            img = bullet.getBitmap();
            if(img == null)
                continue;
            bullet.move();
            Graphics.drawMatrixImage(canvas, img, 0, 0, img.getWidth(), img.getHeight(),
                    bullet.getDir()==Player.DIR_RIGHT? Graphics.TRANS_NONE : Graphics.TRANS_MIRROR,
                    bullet.getX(), bullet.getY(),0, Graphics.TIMES_SCALE);
        }

    }



    /**
     * Adding a bullet of player
     */
    public void addBullet(){

        int drawX = (getDir() == DIR_RIGHT? X_DEFAULT + (int)ViewManager.scale * 50 :
                                X_DEFAULT - (int)ViewManager.scale * 50);
        Bullet bullet = new Bullet(Bullet.BULLET_TYPE_1, drawX, y - (int)ViewManager.scale * 60,getDir());
        bulletList.add(bullet);

        leftShootTime = MAX_SHOOT_TIME;

        ViewManager.soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);

    }



    /**
     * Draw head-area of games on left/top of background screen
     *
     * @param canvas canvas of background screen
     */
    public void drawHead(Canvas canvas){

        if(canvas == null)
            return;

        if(ViewManager.head == null)
            return;
        Graphics.drawMatrixImage(canvas, ViewManager.head, 0, 0, head.getWidth(),
                head.getHeight(), Graphics.TRANS_MIRROR, 0, 0, 0, Graphics.TIMES_SCALE);

        int drawX   = head.getWidth();
        int drawY   = (int)ViewManager.scale * 20;
        Paint paint = new Paint();
        paint.setTextSize(30);
        Graphics.drawBorderString(canvas, 0x344, 0xff23, name, drawX, drawY, 3, paint);

        Graphics.drawBorderString(canvas, 0x344, 0xff23, "HP:"+hp, drawX, drawY*2, 3, paint);

    }


    /**
     * Player jumps and moves.
     *
     */
    public void logic(){

        if(!isJump()){
            move();
            return;
        }

        //If don't jump highest
        if(!isJumpMax){
            setAction(getDir() == Player.DIR_RIGHT? ACTION_JUMP_RIGHT: ACTION_JUMP_LEFT);
            setY(getY() - (int) ViewManager.scale * 8);
            if(getY() <= Y_JUMP_MAX)
                isJumpMax = true;
            setPlayerBulletYAccelerate(-(int)ViewManager.scale * 2);
        }
        else{
            stopTimeMax--;
            if(getStopTimeMax() <= 0){
                setY(getY() + (int)ViewManager.scale * 8);
                setPlayerBulletYAccelerate((int)ViewManager.scale * 2);
                if(getY() >= Y_DEFAULT) {
                    setY(Player.Y_DEFAULT);
                    isJump    = false;
                    isJumpMax = false;
                    setAction(ACTION_STAND_RIGHT);
                }
                else
                    setAction(getDir() == Player.DIR_RIGHT? ACTION_JUMP_RIGHT : ACTION_JUMP_LEFT); //???
            }
        }
        move();

    }


    /**
     * Judge that player is whether or not dead.
     *
     * @return  true  if be dead
     *          false if be alive
     */
    public boolean isDie(){

        return hp <= 0;

    }


    /**
     * Player moves positions on the game UI.
     */
    public void move(){

        if(move == MOVE_RIGHT){
            MonsterManager.updatePosition((int)ViewManager.scale*6);
            setX(getX() + (int)ViewManager.scale*6);
            if(!isJump())
                setAction(ACTION_RUN_RIGHT);
        }

        else if(move == MOVE_LEFT){
            if(getX() - (int)ViewManager.scale*6 < X_DEFAULT) //Player.X_DEFAULT
                MonsterManager.updatePosition(-(getX()-X_DEFAULT));
            else
                MonsterManager.updatePosition(-((int)ViewManager.scale*6));
            setX(getX() - (int)ViewManager.scale*6);
            if(!isJump())
                setAction(ACTION_RUN_LEFT);
        }

        else if(getAction() != ACTION_JUMP_LEFT && getAction() != ACTION_JUMP_RIGHT){
            if(!isJump())
                setAction(ACTION_STAND_RIGHT);
        }

    }


    /**
     * Modify bullet's accelerate speed of y-coordinate
     * @param accelerate  value of accelerate speed
     */
    public void setPlayerBulletYAccelerate(int accelerate){

        for(Bullet bullet : bulletList){
            if(bullet == null || bullet.getAccelerate() != 0)
                continue;
            bullet.setyAccelerate(accelerate);
        }

    }



    public void setHp(int hp){
        this.hp = hp;
    }
    public int getHp(){
        return hp;
    }
    public void setAction(int ac){
        this.action = ac;
    }
    public int getAction(){
        return action;
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
    public void setMove(int mo){
        this.move = mo;
    }
    public int getMove(){
        return move;
    }
    public void setCurrentHeadImg(Bitmap img){
        this.currentHeadImg = img;
    }
    public Bitmap getCurrentHeadImg(){
        return currentHeadImg;
    }
    public int getLeftShootTime(){
        return leftShootTime;
    }
    public boolean isJump(){
        return isJump;
    }
    public void setJump(boolean jp){
        this.isJump = jp;
    }
    public void setJumpMax(boolean isJumpMax){
        this.isJumpMax = isJumpMax;
        stopTimeMax    = 6;
    }
    public boolean getJumpMax(){
        return isJumpMax;
    }
    public int getStopTimeMax(){
        return stopTimeMax;
    }

}
