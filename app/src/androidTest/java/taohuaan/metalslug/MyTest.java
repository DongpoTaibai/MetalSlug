package taohuaan.metalslug;

import android.test.InstrumentationTestCase;

/**
 * 测试Android代码，即用到Android sdk的代码
 * Created by Runzhi on 2018/12/6.
 */

public class MyTest extends InstrumentationTestCase {

    public void testOnPause(){
        MainActivity mainActivity = new MainActivity();
        mainActivity.onPause();
        System.out.println("test MainActivity onPause()");
    }
}
