package taohuaan.metalslug;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.Window;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class MainActivity extends Activity {

    //定義主佈局內的容器
    public static FrameLayout mainLayout = null;
    //定義佈局內UI組件的參數
    public static FrameLayout.LayoutParams mainLP = null;
    //定義成員變量記錄遊戲窗口的寬度和高度
    public static int windowWidth; //public static int windowWidth = 0;
    public static int windowHeight;
    //定義一個資源管理類的變量
    public static Resources res = null;
    //定義遊戲窗口的遊戲界面
    private GameView gameView = null;
    //定義媒體播放類的變量
    private MediaPlayer player = null;
    //定義自身類對象
    public static MainActivity mainActivity = null;

//    private MainActivity mainActivity = null;
//    public MainActivity getMainActivity(){
//        return MainActivity;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        mainActivity = this;

        //設置全屏顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //獲取手機屏幕顯示的寬度和高度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        windowWidth = metrics.widthPixels;
        windowHeight = metrics.heightPixels;

        //Soft input mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        res = getResources();

        setContentView(R.layout.main);
        //把遊戲界面加載到主佈局中
        //gameView = new GameView(this, GameView.STAGE_INIT);
        gameView   = new GameView(this.getApplicationContext(), GameView.STAGE_INIT);
        mainLayout = (FrameLayout)findViewById(R.id.mainLayout);
        mainLP = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        mainLayout.addView(gameView, mainLP);

        //播放背景音樂
        player = MediaPlayer.create(this, R.raw.background);
        player.setLooping(true);
        player.start();

    }


    //當主界面暫停，要顯示其他的應用界面時，暫停播放背景音樂
    @Override
    public void onPause(){
        super.onPause();
        if(player != null && player.isPlaying())
            player.pause();
        Log.d("MainActivity onPause()", "test success");
    }


    //當遊戲主界面重新恢復啟動時，開始播放背景音樂
    @Override
    public void onResume(){
        super.onResume();
        if(player != null && !player.isPlaying())
            player.start();
    }

}
