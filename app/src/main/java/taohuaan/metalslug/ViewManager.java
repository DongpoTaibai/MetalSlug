package taohuaan.metalslug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.SoundPool;
import java.util.HashMap;
import android.media.AudioAttributes;
import android.annotation.TargetApi;
import android.content.res.Resources;
import java.io.InputStream;
import android.graphics.Canvas;


/**
 * Created by Runzhi on 2018/12/10.
 * loading images and audio resource
 */

public class ViewManager {

    /**
     * SoundPool object to invoke relative method
     */
    public static SoundPool soundPool = null;

    /**
     * Map collection to manager all audio resource.
     */
    public static HashMap<Integer, Integer> soundMap = new HashMap<>();

    /**
     * Screen metrics
     */
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    /**
     * Bitmap animation frame array of alive and dead monster.
     */
    public static Bitmap[] bombImage   = null;
    public static Bitmap[] bomb2Image  = null;  //animation frames of bomb dies
    public static Bitmap[] flyImage    = null;
    public static Bitmap[] flyDieImage = null;
    public static Bitmap[] manImage    = null;
    public static Bitmap[] manDieImage = null;

    /**
     * Image of map
     */
    public static Bitmap map = null;

    /**
     * Head animation frames of player stands.
     */
    public static Bitmap[] headStandImage = null;
    /**
     * Leg animation frames of player stands.
     */
    public static Bitmap[] legStandImage = null;

    /**
     * Head and leg animation frames of player runs.
     */
    public static Bitmap[] legRunImage  = null;
    public static Bitmap[] headRunImage = null;

    /**
     * Head and leg animation frames of player jumps.
     */
    public static Bitmap[] headJumpImage = null;
    public static Bitmap[] legJumpImage  = null;

    /**
     * Head animation frames of player shoots.
     */
    public static Bitmap[] headShootImage = null;

    /**
     * Image of player to draw.
     */
    public static Bitmap head = null;

    /**
     * Member variable storage bitmaps resource of bullet.
     */
    public static Bitmap[] bulletImage = null;

    /**
     * values of bitmap scale.
     */
    public static float scale = 1f;


    /**
     * Initial game screen with parameters.
     *
     * @param width     screen detail width
     * @param height    screen detail height
     */
    public static void initScreen(int width, int height){

        SCREEN_WIDTH = (short)width;
        SCREEN_HEIGHT= (short)height;//SCREEN_HEIGHT= height;

    }


    /**
     * Loading image, audio resources.
     */
    @TargetApi(21)
    public static void loadResource(){

       AudioAttributes attr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
        soundPool           = new SoundPool.Builder().setAudioAttributes(attr)
                            .setMaxStreams(10).build();
        soundMap.put(1, soundPool.load(MainActivity.mainActivity, R.raw.shot, 1));
        soundMap.put(2, soundPool.load(MainActivity.mainActivity, R.raw.bomb, 1));
        soundMap.put(3, soundPool.load(MainActivity.mainActivity, R.raw.oh,   1));

        Bitmap temp = createBitmapByID(MainActivity.res, R.drawable.map);
        if(temp != null && !temp.isRecycled()){
            float height = temp.getHeight();
            if(height != SCREEN_HEIGHT && SCREEN_HEIGHT != 0){
                float scale = SCREEN_HEIGHT / height;
                map         = Graphics.scale(temp, temp.getWidth() * scale, height * scale);
                //map.recycle();
                temp.recycle();
            }else{
                map = temp;
            }
        }

        head             = createBitmapByID(MainActivity.res, R.drawable.head, scale);

        legStandImage    = new Bitmap[1];
        legStandImage[0] = createBitmapByID(MainActivity.res, R.drawable.leg_stand, scale);

        headStandImage    = new Bitmap[3];
        headStandImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_stand_1, scale);
        headStandImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_stand_2, scale);
        headStandImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_stand_3, scale);

        headJumpImage     = new Bitmap[5];
        headJumpImage[0]  = createBitmapByID(MainActivity.res, R.drawable.head_jump_1, scale);
        headJumpImage[1]  = createBitmapByID(MainActivity.res, R.drawable.head_jump_2, scale);
        headJumpImage[2]  = createBitmapByID(MainActivity.res, R.drawable.head_jump_3, scale);
        headJumpImage[3]  = createBitmapByID(MainActivity.res, R.drawable.head_jump_4, scale);
        headJumpImage[4]  = createBitmapByID(MainActivity.res, R.drawable.head_jump_5, scale);
        legJumpImage      = new Bitmap[5];
        legJumpImage[0]   = createBitmapByID(MainActivity.res, R.drawable.leg_jum_1, scale);
        legJumpImage[1]   = createBitmapByID(MainActivity.res, R.drawable.leg_jum_2, scale);
        legJumpImage[2]   = createBitmapByID(MainActivity.res, R.drawable.leg_jum_3, scale);
        legJumpImage[3]   = createBitmapByID(MainActivity.res, R.drawable.leg_jum_4, scale);
        legJumpImage[4]   = createBitmapByID(MainActivity.res, R.drawable.leg_jum_5, scale);

        headRunImage      = new Bitmap[3];
        headRunImage[0]   = createBitmapByID(MainActivity.res, R.drawable.head_run_1, scale);
        headRunImage[1]   = createBitmapByID(MainActivity.res, R.drawable.head_run_2, scale);
        headRunImage[2]   = createBitmapByID(MainActivity.res, R.drawable.head_run_3, scale);
        legRunImage       = new Bitmap[3];
        legRunImage[0]    = createBitmapByID(MainActivity.res, R.drawable.leg_run_1, scale);
        legRunImage[1]    = createBitmapByID(MainActivity.res, R.drawable.leg_run_2, scale);
        legRunImage[2]    = createBitmapByID(MainActivity.res, R.drawable.leg_run_3, scale);

        headShootImage    = new Bitmap[6];
        headShootImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_1, scale);
        headShootImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_2, scale);
        headShootImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_3, scale);
        headShootImage[3] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_4, scale);
        headShootImage[4] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_5, scale);
        headShootImage[5] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_6, scale);

        bulletImage = new Bitmap[4];
        bulletImage[0] = createBitmapByID(MainActivity.res, R.drawable.bullet_1, scale);
        bulletImage[1] = createBitmapByID(MainActivity.res, R.drawable.bullet_2, scale);
        bulletImage[2] = createBitmapByID(MainActivity.res, R.drawable.bullet_3, scale);
        bulletImage[3] = createBitmapByID(MainActivity.res, R.drawable.bullet_4, scale);

        bombImage = new Bitmap[2];
        bombImage[0] = createBitmapByID(MainActivity.res, R.drawable.bomb_1, scale);
        bombImage[1] = createBitmapByID(MainActivity.res, R.drawable.bomb_2, scale);

        bomb2Image = new Bitmap[13];
        bomb2Image[0] = createBitmapByID(MainActivity.res, R.drawable.bomb2_1, scale);
        bomb2Image[1] = createBitmapByID(MainActivity.res, R.drawable.bomb2_2, scale);
        bomb2Image[2] = createBitmapByID(MainActivity.res, R.drawable.bomb2_3, scale);
        bomb2Image[3] = createBitmapByID(MainActivity.res, R.drawable.bomb2_4, scale);
        bomb2Image[4] = createBitmapByID(MainActivity.res, R.drawable.bomb2_5, scale);
        bomb2Image[5] = createBitmapByID(MainActivity.res, R.drawable.bomb2_6, scale);
        bomb2Image[6] = createBitmapByID(MainActivity.res, R.drawable.bomb2_7, scale);
        bomb2Image[7] = createBitmapByID(MainActivity.res, R.drawable.bomb2_8, scale);
        bomb2Image[8] = createBitmapByID(MainActivity.res, R.drawable.bomb2_9, scale);
        bomb2Image[9] = createBitmapByID(MainActivity.res, R.drawable.bomb2_10, scale);
        bomb2Image[10] = createBitmapByID(MainActivity.res, R.drawable.bomb2_11, scale);
        bomb2Image[11] = createBitmapByID(MainActivity.res, R.drawable.bomb2_12, scale);
        bomb2Image[12] = createBitmapByID(MainActivity.res, R.drawable.bomb2_13, scale);

        flyImage = new Bitmap[6];
        flyImage[0] = createBitmapByID(MainActivity.res, R.drawable.fly_1, scale);
        flyImage[1] = createBitmapByID(MainActivity.res, R.drawable.fly_2, scale);
        flyImage[2] = createBitmapByID(MainActivity.res, R.drawable.fly_3, scale);
        flyImage[3] = createBitmapByID(MainActivity.res, R.drawable.fly_4, scale);
        flyImage[4] = createBitmapByID(MainActivity.res, R.drawable.fly_5, scale);
        flyImage[5] = createBitmapByID(MainActivity.res, R.drawable.fly_6, scale);

        flyDieImage = new Bitmap[13];
        flyDieImage[0] = createBitmapByID(MainActivity.res, R.drawable.fly_die_1, scale);
        flyDieImage[1] = createBitmapByID(MainActivity.res, R.drawable.fly_die_2, scale);
        flyDieImage[2] = createBitmapByID(MainActivity.res, R.drawable.fly_die_3, scale);
        flyDieImage[3] = createBitmapByID(MainActivity.res, R.drawable.fly_die_4, scale);
        flyDieImage[4] = createBitmapByID(MainActivity.res, R.drawable.fly_die_5, scale);
        flyDieImage[5] = createBitmapByID(MainActivity.res, R.drawable.fly_die_6, scale);
        flyDieImage[6] = createBitmapByID(MainActivity.res, R.drawable.fly_die_7, scale);
        flyDieImage[7] = createBitmapByID(MainActivity.res, R.drawable.fly_die_8, scale);
        flyDieImage[8] = createBitmapByID(MainActivity.res, R.drawable.fly_die_9, scale);
        flyDieImage[9] = createBitmapByID(MainActivity.res, R.drawable.fly_die_10, scale);

        manImage = new Bitmap[3];
        manImage[0] = createBitmapByID(MainActivity.res, R.drawable.man_1, scale);
        manImage[1] = createBitmapByID(MainActivity.res, R.drawable.man_2, scale);
        manImage[2] = createBitmapByID(MainActivity.res, R.drawable.man_3, scale);

        manDieImage = new Bitmap[13];
        manDieImage[0] = createBitmapByID(MainActivity.res, R.drawable.man_die_1, scale);
        manDieImage[1] = createBitmapByID(MainActivity.res, R.drawable.man_die_2, scale);
        manDieImage[2] = createBitmapByID(MainActivity.res, R.drawable.man_die_3, scale);
        manDieImage[3] = createBitmapByID(MainActivity.res, R.drawable.man_die_4, scale);
        manDieImage[4] = createBitmapByID(MainActivity.res, R.drawable.man_die_5, scale);

    }


    /**
     * Creating a bitmap by ID of image and scale's proportion.
     *
     * @param  res      Resources class object
     * @param  resID    ID of resource
     * @param  scale    scale's proportion
     *
     * @return bitmap   original picture
     */
    public static Bitmap createBitmapByID(Resources res, int resID, float scale){

        try{
            InputStream is = res.openRawResource(resID);
            Bitmap bitmap  = BitmapFactory.decodeStream(is, null, null);
            if(bitmap == null || bitmap.isRecycled())
                return null;

            if(scale <= 0 || scale == 1f)
                return bitmap;

            float width      = bitmap.getWidth() * scale;
            float height     = bitmap.getHeight() * scale;
            Bitmap newBitmap = Graphics.scale(bitmap, width, height);
            if(!(newBitmap.isRecycled()) && newBitmap != bitmap)
                newBitmap.recycle();
            return newBitmap;
        }catch(Exception e){
            return null;
        }
    }


    /**
     * Creating a bitmap by ID of image.
     *
     * @param   res   Resources class object
     * @param   resID ID of resource
     * @return  bitmap original picture
     */
    public static Bitmap createBitmapByID(Resources res, int resID){

        try{
            InputStream is = res.openRawResource(resID);
            return BitmapFactory.decodeStream(is, null, null);
        }catch(Exception e){
            return null;
        }

    }


    /**
     * Drawing whole game view, include background map, player and monster.
     *
     * @param    canvas   canvas to draw image
     */
    public static void drawGame(Canvas canvas){

        if(canvas == null)
            return;

        if(map != null && !map.isRecycled()){
            int width = map.getWidth() + GameView.player.getShift();
            Graphics.drawImage(canvas, map, 0, 0, -GameView.player.getShift(), 0, width, map.getHeight());
            int totalWidth = width;
            while(totalWidth <  ViewManager.SCREEN_WIDTH){
                int drawWidth = ViewManager.SCREEN_WIDTH - totalWidth;
                int mapWidth  = map.getWidth();
                if(mapWidth < drawWidth)
                    drawWidth = mapWidth;
                Graphics.drawImage(canvas, map, totalWidth, 0, 0, 0, drawWidth, map.getHeight());
                totalWidth += drawWidth;
            }
        }

        GameView.player.draw(canvas);

        MonsterManager.drawMonster(canvas);

    }


    /**
     * Cleaning the screen with black
     *
     * @param c  canvas on the mobile display screen
     */
    public static void clearScreen(Canvas c){
        c.drawColor(Color.BLACK);
    }


}
