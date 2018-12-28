package taohuaan.metalslug;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;
import java.util.ArrayList;

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
     * Constants defining player's current direction.
     */
    public static final int DIR_RIGHT = 1;
    public static final int DIR_LEFT  = 2; //public static final int MOVE_LEFT  = 2;


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
        y          = ViewManager.SCREEN_HEIGHT * 45 / 100;
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

       return getX() >= startX && getX() <= endX
               && getY() >= startY && getY() <= endY;
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
        int drawXLeg = x;
        int drawYLeg = y - leg.getHeight();
        int trans    = (dir == DIR_RIGHT ? Graphics.TRANS_MIRROR : Graphics.TRANS_NONE);

        Graphics.drawMatrixImage(canvas, leg, 0, 0, leg.getWidth(), leg.getHeight(),
                                    trans, drawXLeg, drawYLeg, 0, Graphics.TIMES_SCALE);

        indexHead     = indexHead % headArr.length;
        Bitmap head   = headArr[indexHead];
        if(head == null || head.isRecycled())
            return;
        int drawXHead = x;
        int drawYHead = y - head.getHeight();

        Graphics.drawMatrixImage(canvas, head, 0, 0, head.getWidth(), head.getHeight(),
                                    trans, drawXHead, drawYHead, 0, Graphics.TIMES_SCALE);

        drawCount++;
        if(drawCount >= 4){

        }

    }



    /**
     * Drawing bullet of players
     *
     * @param canvas   canvas of background screen
     */
    public static void drawBullet(Canvas canvas){

    }


    /**
     * Drawing head of games on left/top of background screen
     *
     * @param canvas canvas of background screen
     */
    public static void drawHead(Canvas canvas){

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

}
