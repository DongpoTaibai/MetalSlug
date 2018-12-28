package taohuaan.metalslug;

import java.util.Random;

/**
 * author: Runzhi on 2018/12/7.
 * calculator a random integer value
 */

public class Util {

    public static Random random = new Random();

    //calculator a random int at [0, a)
    public static int randomIntRange(int range){
        if(0 == range)
            return 0;
        else
            return Math.abs(random.nextInt() % range);
    }
}
