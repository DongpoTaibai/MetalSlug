package taohuaan.metalslug;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.ViewGroup.LayoutParams;


/**
 * author: Runzhi  on 2018/12/5.
 *
 * Draw a game UI .
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private Context mainContext = null;
    private Canvas canvas = null;

    /**
     * Constants defining a player.
     */
    public static final Player player = new Player("孙悟空", Player.MAX_HP);

    /**
     * Constants that do not change the scene.
     */
    public static final int STAGE_NO_CHANGE = 0;

    /**
     * The current game stage.
     */
    private int gStage;

    /**
     * Game login scenario.
     */
    public static final int STAGE_LOGIN = 2;

    /**
     * Game initial scenario.
     */
    public static final int STAGE_INIT = 1;

    /**
     * Game scenario.
     */
    public static final int STAGE_GAME = 3;

    /**
     * Game lose scenario.
     */
    public static final int STAGE_LOSE = 4;

    /**
     * Game exit scenario.
     */
    public static final int STAGE_QUIT= 99;

    /**
     * Game error scenario.
     */
    public static final int STAGE_ERROR = 255;

    /**
     * Member variable defining a Paint class object.
     */
    private Paint paint = null;

    /**
     *Member variable defining a SurfaceHolder interface object.
     */
    private SurfaceHolder holder;

    /**
     * Constructor, initialize member variable and game screen.
     */
    public GameView(Context context, int stage){

        super(context);
        mainContext = context;
        gStage = stage;

        paint = new Paint();
        paint.setAntiAlias(true);
        holder = getHolder();
        holder.addCallback(this);

        setKeepScreenOn(true);
        setFocusable(true);

        ViewManager.initScreen(MainActivity.windowWidth, MainActivity.windowHeight);

    }



    public void setMainContext(Context context){
        this.mainContext = context;
    }

    public Context getMainContext(){
        return mainContext;
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public void setgStage(int stage){
        this.gStage = stage;
    }

    public int getgStage(){
        return gStage;
    }

    public void setPaint(Paint paint){
        this.paint = paint;
    }

    public Paint getPaint(){
        return paint;
    }

    public void setSurfaceHolder(SurfaceHolder holder){
        this.holder = holder;
    }

    public SurfaceHolder getSurfaceHolder(){
        return holder;
    }



    /**
     *Select a game scenario.
     *
     * @param stage
     *              a game scenario
     * @param step
     *              a next scenario
     * @return nextStage
     */
    public int doStage(int stage, int step){

        int nextStage;
        switch(stage){
            case STAGE_INIT:
                nextStage = doInit(step);
                break;
            case STAGE_LOGIN:
                nextStage = doLogin(step);
                break;
            case STAGE_GAME:
                nextStage = doGame(step);
                break;
            case STAGE_LOSE:
                nextStage = doLose(step);
                break;
            default:
                nextStage = STAGE_ERROR;
        }
        return nextStage;

    }


    /**
     * Special the whole game
     *
     *@param step               useless
     *@return STAGE_LOGIN      jump to the next interface.
     */
    public int doInit(int step){

        ViewManager.loadResource();

        return STAGE_LOGIN;

    }


    /**
     * User interface of sign in to(opposite sign up to) the game.
     *
     * @param step  the current game scene
     * @return STAGE_NO_CHANGE
     */
    private RelativeLayout loginView;
    public int doLogin(int step){

        //RelativeLayout loginView = null;
        switch(step){
            case INIT:
                player.setHp(Player.MAX_HP);//爲什麽在這裡呢？？
                if(loginView == null) {
                    loginView = new RelativeLayout(mainContext);
                    loginView.setBackgroundResource(R.drawable.game_back);

                    Button start = new Button(mainContext);
                    start.setBackgroundResource(R.drawable.button_selector);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);

                    loginView.addView(start, params);
                    start.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stageList.add(STAGE_GAME);
                        }
                    });
                    setViewHandler.sendMessage(setViewHandler.obtainMessage(0, loginView));
                }
                break;

            case CLEAN:
                if(loginView != null){
                    delViewHandler.sendMessage(delViewHandler.obtainMessage(0, loginView));
                    loginView = null;
                }
                break;

            case LOGIC:
                break;
            case PAINT:
                break;
        }
        return STAGE_NO_CHANGE;

    }



    /**
     * First step, initial constant value.
     */
    public static final int INIT  = 1;
    /**
     * Second step, logic constant value.
     */
    public static final int LOGIC = 2;
    /**
     * Third step, a constant value cleaning the user interface.
     */
    public static final int CLEAN = 3;
    /**
     * Fourth step, a constant value drawing game interface.
     */
    public static final int PAINT     = 4;
    public static final int ID_LEFT   = 90000;
    private static final int ID_FIRE  = ID_LEFT + 1;
    private RelativeLayout gameLayout = null;

//    /**
//     * Global variable to storage already loaded scene.
//     */
//    public  List<Integer> stageList = Collections.synchronizedList(
//            new ArrayList<Integer>()
//    );
    /**
     * Instant variable to storage already loaded scene
     */
    public static final List<Integer> stageList = Collections.synchronizedList(
            new ArrayList<Integer>());

    /**
     * Global variable to handle message of adding view to the game UI.
     */
    public Handler setViewHandler = new Handler(){
        public void handleMessage(Message msg){
            RelativeLayout layout = (RelativeLayout)msg.obj;
            if(layout != null)
                MainActivity.mainLayout.addView(layout);
        }
    };
    /**
     * Global variable to handle message of deleting view on the game UI.
     */
    public Handler delViewHandler = new Handler(){
        public void handleMessage(Message msg){
            RelativeLayout layout = (RelativeLayout)msg.obj;
            if(layout != null)
                MainActivity.mainLayout.removeView(layout);
        }
    };

    /**
     * Game process.
     *
     * @param step  the current game step
     * @return STAGE_GAME
     */
    public int doGame(int step){

        switch(step){
            case INIT:
                if(gameLayout == null){
                    gameLayout = new RelativeLayout(mainContext);

                    //Add left movement buttons to the game UI
                    Button btn = new Button(mainContext);
                    btn.setId(ID_LEFT);
                    btn.setBackground(getResources().getDrawable(R.drawable.left));

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins((int) ViewManager.scale * 20, 0, 0, (int) ViewManager.scale * 5);

                    gameLayout.addView(btn, params);
                    btn.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    player.setMove(Player.MOVE_LEFT);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    //player.setMove(Player.ACTION_STAND_LEFT);
                                    player.setMove(Player.MOVE_STAND);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                            }
//                            return true; ??
                            return false;
                        }
                    });

                    //Add right movement button to the game UI.
                    btn = new Button(mainContext);//
                    btn.setBackground(getResources().getDrawable(R.drawable.right));

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.RIGHT_OF, ID_LEFT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins((int)ViewManager.scale*20, 0, 0, (int)ViewManager.scale*5);

                    gameLayout.addView(btn, params);
                    btn.setOnTouchListener(new OnTouchListener(){
                        @Override
                        public boolean onTouch(View v, MotionEvent event){
                            switch(event.getAction()){
                                case MotionEvent.ACTION_DOWN:
                                    player.setMove(Player.MOVE_RIGHT);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    player.setMove(Player.MOVE_STAND);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                            }
                            //return true;
                            return false;
                        }
                    });

                    //Add shoot button to the game UI.
                    btn = new Button(mainContext);
                    btn.setId(ID_FIRE);
                    btn.setBackground(getResources().getDrawable(R.drawable.fire));

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, (int)ViewManager.scale*20, (int)ViewManager.scale*10);

                    gameLayout.addView(btn, params);
                    btn.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if(player.getLeftShootTime() <= 0)
                                player.addBullet();
                        }
                    });

                    //Add jump button to the game UI.
                    btn = new Button(mainContext);
                    btn.setBackground(getResources().getDrawable(R.drawable.jump));

                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.LEFT_OF, ID_FIRE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, (int)ViewManager.scale*20, (int)ViewManager.scale*10);

                    gameLayout.addView(btn, params);
                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            player.setJump(true);
                        }
                    });

                    setViewHandler.sendMessage(setViewHandler.obtainMessage(0, gameLayout));
                }
                break;

            case LOGIC:
                MonsterManager.generateMonster();
                MonsterManager.checkMonster();
                player.logic();
                if(player.isDie())
                    stageList.add(STAGE_LOSE);
                Log.d("GameView ", "doGame,LOGIC");
                break;

            case CLEAN:
                if(gameLayout != null){
                    delViewHandler.sendMessage(delViewHandler.obtainMessage(0, gameLayout));
                    gameLayout = null;
                }
                break;

            case PAINT:
                ViewManager.clearScreen(canvas);
                ViewManager.drawGame(canvas);
                break;
        }
        return STAGE_NO_CHANGE;

    }


    /**
     * Special game lose interface and clear UI.
     *
     * @param step
     */
    private RelativeLayout loseView = null;
    public int doLose(int step){

        switch(step){
            case INIT:
               if(loseView == null){  //易漏寫
                   loseView = new RelativeLayout(mainContext);
                   loseView.setBackgroundResource(R.drawable.game_back);

                   Button revive = new Button(mainContext);
                   revive.setBackgroundResource(R.drawable.again);
                   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                   params.addRule(RelativeLayout.CENTER_IN_PARENT);

                   loseView.addView(revive, params);
                   revive.setOnClickListener(new OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           stageList.add(STAGE_GAME);
                           player.setHp(Player.MAX_HP);
                       }
                   });

                   setViewHandler.sendMessage(setViewHandler.obtainMessage(0, loseView));
               }
                break;

            case CLEAN:
                if(loseView != null){
                    delViewHandler.sendMessage(delViewHandler.obtainMessage(0, loseView));
                    loseView = null;
                }
                break;

            case LOGIC:
                break;
            case PAINT:
                break;
        }

        return STAGE_NO_CHANGE;

    }



    /**
     *Time of two scheduling pauses.
     */
    //private static final int TIME_STOP = 40;
    private static final int SLEEP_TIME = 40;
    /**
     * Minimum time of two scheduling pauses.
     */
    //private static final int MIN_STOP  = 5;
        private static final int MIN_SLEEP = 5;
    //public class GameThread extends Thread{
    public class GameThread extends Thread{

        public SurfaceHolder holder = null;
        public boolean needStop     = false;

        public GameThread(SurfaceHolder holder){
            this.holder = holder;
        }

        public void run(){

            long t1, t2;
            Looper.prepare();
            synchronized(holder){
                while(gStage != STAGE_QUIT && !needStop){
                    try {
                        stageLogic();
                        t1 = System.currentTimeMillis();
                        canvas = holder.lockCanvas();
                        if (canvas != null) {
                            doStage(gStage, PAINT);
                        }
                        t2 = System.currentTimeMillis();
                        //long paint = t2 - t1;
                        int paint = (int) t2 - (int) t1;
                        //int millis= SLEEP_TIME - paint;
                        long millis = SLEEP_TIME - paint;
//                    long paint  = t2 - t1;
//                    long millis = SLEEP_TIME - paint;
                        if (millis <= MIN_SLEEP)
                            millis = MIN_SLEEP;
                        sleep(millis);
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                            try{
                                if(canvas != null)
                                holder.unlockCanvasAndPost(canvas);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                    }
                }
            }
            Looper.loop();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * How to initialize the whole game.
     */
    public void stageLogic(){

        int newStage = doStage(gStage, LOGIC);
        if(newStage != STAGE_NO_CHANGE && newStage != gStage){
            doStage(gStage, CLEAN);
            //newStage = newStage & 0xFF;
            gStage = newStage & 0xFF;
            //doStage(newStage, INIT);
            doStage(gStage, INIT);
        }
        else if(stageList.size() > 0){
            newStage = STAGE_NO_CHANGE;
            synchronized(stageList) {
                newStage = stageList.get(0);
                stageList.remove(0);
            }
            if(newStage == STAGE_NO_CHANGE)
                    return;
            doStage(gStage, CLEAN);
//            newStage = gStage & 0xFF;
//            doStage(newStage, INIT);
            gStage = newStage & 0xFF;
            doStage(gStage, INIT);
        }

    }



    private GameThread thread = null;
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        //paint.setTextSize(15);
        if(thread != null)
            thread.needStop = true;

        thread = new GameThread(holder);
        thread.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }
}
