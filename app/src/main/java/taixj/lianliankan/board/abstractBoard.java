package taixj.lianliankan.board;

import java.util.List;

import taixj.lianliankan.GameConf;
import taixj.lianliankan.util.ImageUtil;
import taixj.lianliankan.view.Piece;
import taixj.lianliankan.view.PieceImage;

/**
 * Created by taoxj on 15-7-20.
 */
public abstract class AbstractBoard {
      protected abstract List<Piece> createPieces(GameConf conf,Piece[][] pieces);

     public Piece[][] create(GameConf conf){
         Piece[][] pieces = new Piece[conf.xSize][conf.ySize];
         List<Piece> notNullPieces = createPieces(conf,pieces);
         System.out.println(notNullPieces.size() + "-----" + conf.getContext());
         List<PieceImage> images = ImageUtil.getPlayImages(conf.getContext(),notNullPieces.size());
          int imageWidth = images.get(0).getImage().getWidth();
         int imageHeight = images.get(0).getImage().getHeight();

         for(int i=0;i<notNullPieces.size();i++){
             Piece p = notNullPieces.get(i);
             PieceImage image = images.get(i);
             p.setBeginX(p.getIndexX() * imageWidth + conf.beginImageX);
             p.setBeginY(p.getIndexY() * imageHeight + conf.beginImageY);
             p.setImage(image);
             pieces[p.getIndexX()][p.getIndexY()] = p;
         }

         return pieces;


     }

}
