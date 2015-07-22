package taixj.lianliankan.service;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import taixj.lianliankan.GameConf;
import taixj.lianliankan.board.FullBoard;
import taixj.lianliankan.object.LinkInfo;
import taixj.lianliankan.view.Piece;

/**
 * Created by taoxj on 15-7-21.
 */
public class GameServiceImpl implements GameService {
    Piece[][] pieces;
    GameConf conf;

    public GameServiceImpl(GameConf conf) {
        this.conf = conf;
    }

    @Override
    public Piece[][] getPieces() {
        return this.pieces;
    }

    @Override
    public void start() {
        FullBoard board = new FullBoard();
        pieces = board.create(conf);
    }

    @Override
    public Piece findPiece(float touchX, float touchY) {
        int relativeX = (int) (touchX - GameConf.beginImageX);
        int relativeY = (int) (touchY - GameConf.beginImageY);
        if (relativeX < 0 || relativeY < 0) {
            return null;
        }
        int indexX = getIndex(relativeX, GameConf.WIDTH);
        int indexY = getIndex(relativeY, GameConf.HEIGHT);
        if (indexX < 0 || indexY < 0 || indexX >= GameConf.xSize || indexY >= GameConf.ySize) {
            return null;
        }
        return this.pieces[indexX][indexY];
    }

    @Override
    public LinkInfo link(Piece p1, Piece p2) {
        //同一个位置的图片
        if (p1.equals(p2)) {
            return null;
        }
        if (!p1.isSameImage(p2)) {
            return null;
        }
        if (p2.getIndexX() < p1.getIndexX()) {
            return link(p2, p1);
        }
        Point point1 = p1.getCenter();
        Point point2 = p2.getCenter();
        //同一行
        if (p1.getIndexY() == p2.getIndexY()) {
            if (!isXBlock(point1, point2, GameConf.WIDTH)) {
                return new LinkInfo(point1, point2);
            }
        }
        //同一列
        if (p1.getIndexX() == p2.getIndexX()) {
            if (!isYBlock(point1, point2, GameConf.HEIGHT)) {
                return new LinkInfo(point1, point2);
            }
        }
        //一个转折点
        Point cornerPoint = getCornerPoint(point1, point2, GameConf.WIDTH, GameConf.HEIGHT);
        if (cornerPoint != null) {
            return new LinkInfo(point1, cornerPoint, point2);
        }
        //两个转折点
        Map<Point, Point> turns = getLinkPoints(point1, point2, GameConf.WIDTH, GameConf.HEIGHT);
        if (turns.size() != 0) {
            return getShortTurns(point1, point2, turns, getDistance(point1, point2));
        }
        return null;
    }

    @Override
    public boolean hasPieces() {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                if (pieces[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getIndex(int distance, int per) {
        int index = -1;
        if (distance % per == 0) {
            index = distance / per - 1;
        } else if (distance % per != 0) {
            index = distance / per;
        }
        return index;
    }

    //X方向是否有障碍
    public boolean isXBlock(Point p1, Point p2, int size) {
        if(p2.x < p1.x){
            return isXBlock(p2,p1,size);
        }
        for(int i = p1.x + size;i < p2.x;i = i + size){
           if(hasPiece(i,p1.y)){
               return true;
           }
        }
        return false;
    }

    //Y方向是否有障碍
    public boolean isYBlock(Point p1, Point p2, int size) {
        if(p2.y < p1.y){
            return isXBlock(p2,p1,size);
        }
        for(int i = p1.y + size;i < p2.y;i = i + size){
            if(hasPiece(p1.x,i)){
                return true;
            }
        }
        return false;
    }


    public Point getWrapPoint(List<Point> channel1,List<Point> channel2){
        for(int i=0;i<channel1.size();i++){
            Point p1 = channel1.get(i);
            for(int j =0;j<channel2.size();j++){
                Point p2 = channel2.get(j);
                if(p1.equals(p2)){
                    return p1;
                }
            }
        }
        return null;
    }
    public Point getCornerPoint(Point p1, Point p2, int xSize, int ySize) {

        return null;
    }

    public Map<Point, Point> getLinkPoints(Point p1, Point p2, int xSize, int ySize) {
        return null;
    }

    public LinkInfo getShortTurns(Point p1, Point p2, Map<Point, Point> turns, int distance) {
        return null;
    }

    public int getDistance(Point p1, Point p2) {
        return -1;
    }

    //返回point的左边通道
    public List<Point> getLeftChannel(Point p, int min, int width) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.x - width; i >= min; ) {
            if (hasPiece(i, p.y)) {
                return result;
            }
            i = i - width;
            result.add(new Point(i,p.y));

        }
        return result;
    }

    //返回point的左边通道
    public List<Point> getRightChannel(Point p, int max, int width) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.x + width; i <= max; ) {
            if (hasPiece(i, p.y)) {
                return result;
            }
            i = i + width;
            result.add(new Point(i,p.y));

        }
        return result;
    }

    //返回point的上边通道
    public List<Point> getUpChannel(Point p, int min, int height) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.y - height; i >= min; ) {
            if (hasPiece(p.x,i)) {
                return result;
            }
            i = i - height;
            result.add(new Point(p.x,i));

        }
        return result;
    }


    //返回point的下边通道
    public List<Point> getDownChannel(Point p, int max, int height) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.y + height; i <= max; ) {
            if (hasPiece( p.x,i)) {
                return result;
            }
            i = i + height;
            result.add(new Point(p.x,i));

        }
        return result;
    }


    //某个点是否有图片
    public boolean hasPiece(int x, int y) {
        if (findPiece((float) x, (float) y) == null)
        {
            return false;
        }
        return true;
    }

}