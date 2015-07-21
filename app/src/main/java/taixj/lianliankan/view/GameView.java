package taixj.lianliankan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.jar.Attributes;

import taixj.lianliankan.GameConf;
import taixj.lianliankan.object.LinkInfo;
import taixj.lianliankan.service.GameService;
import taixj.lianliankan.service.GameServiceImpl;
import taixj.lianliankan.util.ImageUtil;

/**
 * Created by taoxj on 15-7-20.
 */
public class GameView extends View {
    //游戏逻辑的实现类
    private GameService service;
    private LinkInfo linkInfo;
    private Paint paint;
    private Piece selectImage;
    private Bitmap selectPic;

    public GameConf getConf() {
        return conf;
    }

    public void setConf(GameConf conf) {
        this.conf = conf;
    }

    private GameConf conf ;

    public GameView(Context context, AttributeSet attributes) {
        super(context,attributes);
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(3);
        this.selectPic = ImageUtil.getSelectImage(context);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.service == null) {
            return;
        }
        Piece[][] pieces = service.getPieces();
        if (pieces != null) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != null) {
                        Piece piece = pieces[i][j];
                        canvas.drawBitmap(piece.getImage().getImage(), piece.getBeginX(), piece.getBeginY(), null);
                    }
                }
            }
        }
        if (this.linkInfo != null) {
            drawLine(this.linkInfo, canvas);
            this.linkInfo = null;
        }
        //画选中标识的图片
        if(this.selectImage != null){
            canvas.drawBitmap(this.selectPic,this.selectImage.getBeginX(),this.selectImage.getBeginY(),null);
        }
    }
    //画连接线
    public void drawLine(LinkInfo info, Canvas canvas) {
        List<Point> points = info.getLinkedPoints();
        for (int i = 0; i < points.size() - 1; i++) {
            Point current = points.get(i);
            Point nextPoint = points.get(i + 1);
            canvas.drawLine(current.x, current.y, nextPoint.x, nextPoint.y
                    , this.paint);
        }
    }

    public void setSelectedImage(Piece piece) {
        this.selectImage = piece;
    }

    public void startGame() {
        this.service = new GameServiceImpl(conf);
        this.service.start();
        this.postInvalidate();
    }


    public LinkInfo getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    public GameService getService() {
        return service;
    }

    public void setService(GameService service) {
        this.service = service;
    }
}

