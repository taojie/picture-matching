package taixj.lianliankan.view;

import android.graphics.Bitmap;
/**
 * Created by taoxj on 15-7-20.
 * Õº∆¨–≈œ¢
 */
public class PieceImage {

    private int imageId;
    private Bitmap image;

    public PieceImage(Bitmap image,int id){
        this.image = image;
        this.imageId = id;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
