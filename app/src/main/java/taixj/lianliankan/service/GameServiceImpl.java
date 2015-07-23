package taixj.lianliankan.service;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
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
        //ͬһ��λ�õ�ͼƬ
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
        //ͬһ��
        if (p1.getIndexY() == p2.getIndexY()) {
            if (!isXBlock(point1, point2, GameConf.WIDTH)) {
                return new LinkInfo(point1, point2);
            }
        }
        //ͬһ��
        if (p1.getIndexX() == p2.getIndexX()) {
            if (!isYBlock(point1, point2, GameConf.HEIGHT)) {
                return new LinkInfo(point1, point2);
            }
        }
        //һ��ת�۵�
        Point cornerPoint = getCornerPoint(point1, point2, GameConf.WIDTH, GameConf.HEIGHT);
        if (cornerPoint != null) {
            return new LinkInfo(point1, cornerPoint, point2);
        }
        //����ת�۵�
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

    //X�����Ƿ����ϰ�
    public boolean isXBlock(Point p1, Point p2, int size) {
        if(p2.x < p1.x){
            return isXBlock(p2, p1, size);
        }
        for(int i = p1.x + size;i < p2.x;i = i + size){
           if(hasPiece(i,p1.y)){
               return true;
           }
        }
        return false;
    }

    //Y�����Ƿ����ϰ�
    public boolean isYBlock(Point p1, Point p2, int size) {
        if(p2.y < p1.y){
            return isYBlock(p2, p1, size);
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
    //�ж�p2�Ƿ���p1�����Ͻ�
    public boolean isLeftUp(Point p1,Point p2){
        if(p2.x < p1.x && p2.y < p1.y){
            return true;
        }
        return false;
    }
    //�ж�p2�Ƿ���p1�����½�
    public boolean isLeftDown(Point p1,Point p2){
        if(p2.x < p1.x && p2.y > p1.y){
            return true;
        }
        return false;

    }//�ж�p2�Ƿ���p1�����Ͻ�
    public boolean isRightUp(Point p1,Point p2){
        if(p2.x > p1.x && p2.y < p1.y){
            return true;
        }
        return false;

    }//�ж�p2�Ƿ���p1�����½�
    public boolean isRightDown(Point p1,Point p2){
        if(p2.x > p1.x && p2.y > p1.y){
            return true;
        }
        return false;

    }



    public Point getCornerPoint(Point p1, Point p2, int xSize, int ySize) {
        if(isLeftDown(p1,p2) || isLeftUp(p1,p2)){
            return getCornerPoint(p1,p2,xSize,ySize);
        }
        //获取p1的通道ͨ��
        List<Point> p1UpChannel = getUpChannel(p1,p2.y,ySize);
        List<Point> p1DownChannel = getDownChannel(p1,p2.y,ySize);
        List<Point> p1RightChannel = getRightChannel(p1, p2.x, xSize);

        //获取p2的通道
        List<Point> p2UpChannel = getUpChannel(p2,p1.y,ySize);
        List<Point> p2DownChannel = getDownChannel(p2,p1.y,ySize);
        List<Point> p2LeftChannel = getLeftChannel(p2,p1.x,xSize);

        if(isRightUp(p1,p2)){
             Point point1 = getWrapPoint(p1RightChannel,p2DownChannel);
             Point point2 = getWrapPoint(p1UpChannel,p2LeftChannel);
             return point1 != null ? point1 : point2;
        }
        if(isRightDown(p1,p2)){
            Point point1 = getWrapPoint(p1RightChannel,p2UpChannel);
            Point point2 = getWrapPoint(p1DownChannel,p2LeftChannel);
            return point1 != null ? point1 : point2;
        }

        return null;
    }

    public Map<Point, Point> getLinkPoints(Point p1, Point p2, int xSize, int ySize) {
        Map<Point,Point> result = new HashMap<Point,Point>();

        //获取p1的通道ͨ��
        List<Point> p1UpChannel = getUpChannel(p1,p2.y,ySize);
        List<Point> p1DownChannel = getDownChannel(p1,p2.y,ySize);
        List<Point> p1RightChannel = getRightChannel(p1,p2.x,xSize);

        //获取p2的通道
        List<Point> p2UpChannel = getUpChannel(p2,p1.y,ySize);
        List<Point> p2DownChannel = getDownChannel(p2, p1.y, ySize);
        List<Point> p2LeftChannel = getLeftChannel(p2, p1.x, xSize);

        //获取board的最大宽高
        int maxWidth = (GameConf.xSize + 1)* xSize + GameConf.beginImageX;
        int maxHeight = (GameConf.ySize + 1 ) * ySize + GameConf.beginImageY;

        if(isLeftDown(p1,p2) || isLeftUp(p1,p2)){
            return getLinkPoints(p1,p2,xSize,ySize);
        }

        //p1 和 p2 位于同一行
        if(p1.y == p2.y){
            p1UpChannel = getUpChannel(p1,0,ySize);
            p2UpChannel = getUpChannel(p2,0,ySize);
            Map<Point,Point> upResult = getXLinkPoint(p1UpChannel, p2UpChannel, xSize);

            p1DownChannel = getDownChannel(p1, maxHeight, ySize);
            p2DownChannel = getDownChannel(p2, maxHeight, ySize);
            Map<Point,Point> downResult = getXLinkPoint(p1DownChannel,p2DownChannel,xSize);
            result.putAll(upResult);
            result.putAll(downResult);
          }
        //p1和 p2位于同一列
        if(p1.x == p2.x){
            p1RightChannel = getRightChannel(p1,maxWidth,xSize);
            List<Point> p2RightChannel = getRightChannel(p2,maxWidth,xSize);
            Map<Point,Point> rightResult = getYLinkPoint(p1RightChannel, p2RightChannel, ySize);
            List<Point> p1LeftChannel = getLeftChannel(p1, 0, xSize);
            p2LeftChannel = getLeftChannel(p2,0,xSize);
            Map<Point,Point> leftResult = getYLinkPoint(p1LeftChannel, p2LeftChannel, ySize);
            result.putAll(rightResult);
            result.putAll(leftResult);
        }
        //p2位于 p1 右上角
        if(isRightUp(p1,p2)){
            //p1向上p2向下的连线
            Map<Point,Point> udResult = getXLinkPoint(p1UpChannel, p2DownChannel, xSize);
            //p1向右 p2向左
            Map<Point,Point> rlResult = getYLinkPoint(p1RightChannel, p2LeftChannel, ySize);
            //p1向上 p2向上
            p1UpChannel = getUpChannel(p1,0,ySize);
            p2UpChannel = getUpChannel(p2,0,ySize);
            Map<Point,Point> uuResult = getXLinkPoint(p1UpChannel, p2UpChannel, xSize);
            //p1向下 p2向下
            p1DownChannel = getDownChannel(p1, maxHeight, ySize);
            p2DownChannel = getDownChannel(p2, maxHeight, ySize);
            Map<Point,Point> ddResult = getXLinkPoint(p1DownChannel, p2DownChannel, xSize);
            //p1向右 p2向右
            p1RightChannel = getRightChannel(p1, maxWidth, xSize);
            List<Point> p2RightChannel = getRightChannel(p2, maxWidth, xSize);
            Map<Point,Point> rrResult = getYLinkPoint(p1RightChannel, p2RightChannel, ySize);
            //p1向左 p2向左
            List<Point> p1LeftChannel = getLeftChannel(p1, 0, xSize);
            p2LeftChannel = getLeftChannel(p2,0,xSize);
            Map<Point,Point> llResult = getYLinkPoint(p1LeftChannel, p2LeftChannel, ySize);

            result.putAll(llResult);
            result.putAll(rrResult);
            result.putAll(ddResult);
            result.putAll(rlResult);
            result.putAll(uuResult);
            result.putAll(udResult);

        }
        //p2位于p1右下角
        if(isRightDown(p1,p2)){
            //p1向下p2向上的连线
            Map<Point,Point> udResult = getXLinkPoint(p1DownChannel, p2UpChannel, xSize);
            //p1向右 p2向左
            Map<Point,Point> rlResult = getYLinkPoint(p1RightChannel, p2LeftChannel, ySize);
            //p1向上 p2向上
            p1UpChannel = getUpChannel(p1,0,ySize);
            p2UpChannel = getUpChannel(p2,0,ySize);
            Map<Point,Point> uuResult = getXLinkPoint(p1UpChannel, p2UpChannel, xSize);
            //p1向下 p2向下
            p1DownChannel = getDownChannel(p1, maxHeight, ySize);
            p2DownChannel = getDownChannel(p2, maxHeight, ySize);
            Map<Point,Point> ddResult = getXLinkPoint(p1DownChannel, p2DownChannel, xSize);
            //p1向右 p2向右
            p1RightChannel = getRightChannel(p1, maxWidth, xSize);
            List<Point> p2RightChannel = getRightChannel(p2, maxWidth, xSize);
            Map<Point,Point> rrResult = getYLinkPoint(p1RightChannel, p2RightChannel, ySize);
            //p1向左 p2向左
            List<Point> p1LeftChannel = getLeftChannel(p1, 0, xSize);
            p2LeftChannel = getLeftChannel(p2,0,xSize);
            Map<Point,Point> llResult = getYLinkPoint(p1LeftChannel, p2LeftChannel, ySize);

            result.putAll(llResult);
            result.putAll(rrResult);
            result.putAll(ddResult);
            result.putAll(rlResult);
            result.putAll(uuResult);
            result.putAll(udResult);
        }


        return result;
    }

    public Map<Point,Point> getXLinkPoint(List<Point> p1,List<Point> p2,int width){
        Map<Point,Point> result = new HashMap<Point,Point>();
        for(int i=0;i<p1.size();i++){
            Point temp1 = p1.get(i);
            for(int j=0;j < p2.size();j++){
                Point temp2 = p2.get(j);
                if(temp1.y == temp2.y){
                    if(!isXBlock(temp1, temp2, width));
                    result.put(temp1,temp2);
                }
            }
        }
        return result;
    }
    public Map<Point,Point> getYLinkPoint(List<Point> p1,List<Point> p2,int height){
        Map<Point,Point> result = new HashMap<Point,Point>();
        for(int i=0;i<p1.size();i++){
            Point temp1 = p1.get(i);
            for(int j=0;j < p2.size();j++){
                Point temp2 = p2.get(j);
                if(temp1.x == temp2.x){
                    if(!isYBlock(temp1, temp2, height));
                    result.put(temp1,temp2);
                }
            }
        }
        return result;
    }

    public LinkInfo getShortTurns(Point p1, Point p2, Map<Point, Point> turns, int distance) {
        List<LinkInfo> links = new ArrayList<LinkInfo>();
        for(Point point1 : turns.keySet()){
            Point point2 = turns.get(point1);
            links.add(new LinkInfo(p1,point1,point2,p2));
        }
        return getShortCut(links,distance);
    }

    //获取最短的连线
    public LinkInfo getShortCut(List<LinkInfo> links,int distance){
        LinkInfo result = null;
        int temp = 0;
        for(int i=0;i<links.size();i++){
            LinkInfo info = links.get(i);
            int dis = countAll(info.getLinkedPoints());
            if( i == 0){
                temp = dis - distance ;
                result = info;
            }
            if((dis - distance) < temp){
                temp = dis - distance;
                result = info;
            }
        }
        return result;
    }

    //����point�����ͨ��
    public List<Point> getLeftChannel(Point p, int min, int width) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.x - width; i >= min; ) {
            if (hasPiece(i, p.y)) {
                return result;
            }
            result.add(new Point(i,p.y));
            i = i - width;
        }
        return result;
    }

    //����point�����ͨ��
    public List<Point> getRightChannel(Point p, int max, int width) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.x + width; i <= max; ) {
            if (hasPiece(i, p.y)) {
                return result;
            }
            result.add(new Point(i,p.y));
            i = i + width;

        }
        return result;
    }

    //����point���ϱ�ͨ��
    public List<Point> getUpChannel(Point p, int min, int height) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.y - height; i >= min; ) {
            if (hasPiece(p.x,i)) {
                return result;
            }
            result.add(new Point(p.x,i));
            i = i - height;
        }
        return result;
    }


    //����point���±�ͨ��
    public List<Point> getDownChannel(Point p, int max, int height) {
        List<Point> result = new ArrayList<Point>();
        for (int i = p.y + height; i <= max; ) {
            if (hasPiece( p.x,i)) {
                return result;
            }
            result.add(new Point(p.x,i));
            i = i + height;
        }
        return result;
    }


    //ĳ�����Ƿ���ͼƬ
    public boolean hasPiece(int x, int y) {
        if (findPiece((float) x, (float) y) == null)
        {
            return false;
        }
        return true;
    }

    //计算2点之间的距离
    public int getDistance (Point p1,Point p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y-p2.y);
    }

    //计算所有点的距离和
    public int countAll(List<Point> points){
        int result = 0;
        for(int i = 0 ;i < points.size() -1; i++){
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            result += getDistance(p1,p2);
        }
        return result;
    }
}