package taixj.lianliankan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import taixj.lianliankan.object.LinkInfo;
import taixj.lianliankan.service.GameService;
import taixj.lianliankan.service.GameServiceImpl;
import taixj.lianliankan.view.GameView;
import taixj.lianliankan.view.Piece;

public class MainActivity extends ActionBarActivity {

    private GameConf conf;  // 游戏配置文件��
    private GameService service ; //��Ϸҵ���߼��ӿ�
    private GameView  gameView; //��Ϸ����
    private Button startButton; //��ʼ��ť
    private TextView timerText ; //��¼��Ϸ��ʣ��ʱ��
    private AlertDialog.Builder failDialog; //ʧ�ܵ�����
    private AlertDialog.Builder successDialog; //�ɹ�������
    private Timer timer =  new Timer(); //��ʱ��
    private int gameTime; //��Ϸʣ��ʱ��
    private boolean isPlaying;//�Ƿ�����Ϸ״̬
    private Vibrator vibrator; //�𶯴�����
    private Piece selected = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x123 :
                    timerText.setText("剩余时间：" + gameTime);
                    gameTime--;
                    if(gameTime < 0){
                        stopTimer();
                        isPlaying = false;
                        failDialog.show();
                        return;
                    }
                    break;
            }
         }
    };

    public void stopTimer(){
        this.timer.cancel();
        this.timer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        conf = new GameConf(MainActivity.this);
        gameView = (GameView) findViewById(R.id.gameView);
        startButton  = (Button) findViewById(R.id.startBtn);
        timerText = (TextView) findViewById(R.id.timeText);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        service = new GameServiceImpl(conf);
        gameView.setService(service);
        gameView.startGame();


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(GameConf.DEFAULT_TIME);
            }
        });



        successDialog = new AlertDialog.Builder(this).setTitle("Success").setMessage("游戏胜利，重新开始").setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(GameConf.DEFAULT_TIME);
            }
        });

        failDialog = new AlertDialog.Builder(this).setTitle("Fail").setMessage("游戏失败，重新开始").setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(GameConf.DEFAULT_TIME);
            }
        });

        this.gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    gameViewTouchDown(e);
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    gameViewTouchUp(e);
                }
                return true;
            }
        });


    }

    public void startGame(int time){

        if(timer != null){
            stopTimer();
        }
        this.gameTime = time;
        this.timer = new Timer();
        if(this.gameTime == GameConf.DEFAULT_TIME){
            this.gameView.startGame();
        }

        timer.schedule(new TimerTask() {   //定时器的使用
            @Override
            public void run() {
               handler.sendEmptyMessage(0x123);
            }
        },0,1000);
        this.selected = null;
    }

    public void gameViewTouchDown(MotionEvent e){
      Piece[][] pieces = service.getPieces();
        float touchX = e.getX();
        float touchY = e.getY();
        Piece current = service.findPiece(touchX,touchY);
      //  Log.e("lianliankan","current -----  X:" + current.getIndexX() + "; Y:" + current.getIndexY());
        if(current == null){
            return ;
        }
        gameView.setSelectedImage(current);
        if(this.selected == null){
            this.selected = current;
            this.gameView.postInvalidate();
            return;
        }
      //  Log.e("lianliankan","select ----- X:" + current.getIndexX() + "; Y:" + current.getIndexY());
        if(this.selected != null){
            Log.e("lianliankan","current -----  X:" + current.getIndexX() + "; Y:" + current.getIndexY());
            Log.e("lianliankan","select ----- X:" + this.selected.getIndexX() + "; Y:" + this.selected.getIndexY());
            LinkInfo linkInfo = this.service.link(this.selected,current);
            if(linkInfo == null){
                this.selected = current;
                gameView.postInvalidate();
            }else{
                handlerSuccessLink(linkInfo,this.selected,current,pieces);
            }
        }
    }

    public void handlerSuccessLink(LinkInfo link,Piece p1,Piece p2,Piece[][] pieces){
        this.gameView.setLinkInfo(link);
        this.gameView.setSelectedImage(null);
        this.gameView.postInvalidate();

        pieces[p1.getIndexX()][p1.getIndexY()] = null;
        pieces[p2.getIndexX()][p2.getIndexY()] = null;
        this.selected = null;

        this.vibrator.vibrate(100);
        if(! this.service.hasPieces()){
            this.successDialog.show();
            stopTimer();
            isPlaying = false;
        }
    }
    public void gameViewTouchUp(MotionEvent e){
        this.gameView.postInvalidate();

    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(isPlaying){
            startGame(gameTime);
        }
        super.onResume();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
