package taixj.lianliankan.object;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoxj on 15-7-20.
 */
public class LinkInfo {
    private List<Point> list = new ArrayList<Point>();
    public List<Point> getLinkedPoints(){
        return list;
    }

    //�������ֱ������
    public LinkInfo(Point p1,Point p2){
        list.add(p1);
        list.add(p2);
    }
    //��һ��ת�۵�
    public LinkInfo(Point p1,Point p2,Point p3){
        list.add(p1);
        list.add(p2);
        list.add(p3);
    }
    //������ת�۵�
    public LinkInfo(Point p1,Point p2,Point p3,Point p4){
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
    }

}
