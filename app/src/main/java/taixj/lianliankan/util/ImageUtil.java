package taixj.lianliankan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import taixj.lianliankan.R;
import taixj.lianliankan.view.PieceImage;

/**
 * Created by taoxj on 15-7-20.
 */
public class ImageUtil {
    private static List<Integer> imageValues = getImageValues();

    //获取所有的图片
    public static List<Integer> getImageValues() {
        List<Integer> list = new ArrayList<Integer>();
        Field[] fields = R.mipmap.class.getFields();
        try {
            for (Field f : fields)
                if (f.getName().startsWith("p_")) {
                    list.add(f.getInt(R.drawable.class));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取被选中的图片
    public static Bitmap getSelectImage(Context ctx) {
        Bitmap image = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.selected);
        return image;
    }

    public static List<Integer> getRandomImages(List<Integer> values, int size) {
        List<Integer> list = new ArrayList<Integer>();
        Random random = new Random();  //创建随机数生成器
        try{
        for (int i = 0; i < size; i++) {
            int r = random.nextInt(values.size());
            list.add(values.get(r));
        }}catch(Exception e){
            return list;
        }
        return list;

    }

    public static List<Integer> getPlayValues(int size){
        if(size%2 != 0){
            size =+ 1;
        }
        List<Integer> result = getRandomImages(imageValues,size/2);
        result.addAll(result); //增加一倍
        Collections.shuffle(result);  //随机洗牌
        return result;

    }
    public static List<PieceImage> getPlayImages(Context ctx, int size) {
        List<PieceImage> result = new ArrayList<PieceImage>();
        List<Integer> resultID = getPlayValues(size);
        for(int i=0 ;i<size;i++){
            int value = resultID.get(i);
            Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), value);
            PieceImage image = new PieceImage(bitmap,value);
            result.add(image);
        }
        return result;
    }
}
