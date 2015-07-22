package taixj.lianliankan;

import android.content.Context;

/**
 * Created by taoxj on 15-7-20.
 */
public class GameConf {
    private Context ctx ;
    public static int xSize = 6;
    public static int ySize = 8;
    public static int beginImageX = 35;
    public static int beginImageY = 28;
    public static int DEFAULT_TIME = 100 ;
    public static int WIDTH = 72;
    public static int HEIGHT = 72;

    public GameConf(Context ctx){
        this.ctx = ctx;
    }
    public Context getContext(){
       return this.ctx;
    }
}
