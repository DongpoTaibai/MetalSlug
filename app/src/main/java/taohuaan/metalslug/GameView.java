package taohuaan.metalslug;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;


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
     * Initial.
     *
     *@param step               useless
     * @return STAGE_LOGIN      jump to the next interface.
     */
    public int doInit(int step){

        ViewManager.loadResource();

        return STAGE_LOGIN;

    }


    /**
     * Login the game.
     *
     * @param step
     * @return STAGE_NO_CHANGE
     */
    public int doLogin(int step){

        return STAGE_NO_CHANGE;
    }


    /**
     * Game process.
     *
     * @param step
     * @return STAGE_GAME
     */
    public int doGame(int step){

        return STAGE_GAME;
    }


    /**
     * Game lose.
     *
     * @param step
     */

}
