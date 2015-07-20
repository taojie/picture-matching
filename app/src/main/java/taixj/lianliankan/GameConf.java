package taixj.lianliankan;

import android.content.Context;

/**
 * Created by taoxj on 15-7-20.
 */
public class GameConf {
    private Context ctx ;
    public static int xSize = 10;
    public static int ySize = 10;
    public static int beginImageX = 100;
    public static int beginImageY = 100;

    GameConf(Context ctx){
        this.ctx = ctx;
    }
    public Context getContext(){
       return this.ctx;
    }
}
