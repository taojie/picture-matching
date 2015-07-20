package taixj.lianliankan.view;


/**
 * Created by taoxj on 15-7-20.
 * 保存每个图片的位置信息
 */
public class Piece {
    private PieceImage image;
    private int beginX;  //图片左上角开始坐标
    private int beginY;
    private int indexX;  //图片在二维数组中的坐标
    private int indexY;


    public Piece(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public boolean isSameImage(Piece other){
        if(image == null){
            if(other.image != null){
                return false;
            }
        }
        return this.getImage().getImageId() == other.getImage().getImageId();
    }

    public PieceImage getImage() {
        return image;
    }

    public void setImage(PieceImage image) {
        this.image = image;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }
}
