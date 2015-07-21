package taixj.lianliankan.board;

import java.util.ArrayList;
import java.util.List;

import taixj.lianliankan.GameConf;
import taixj.lianliankan.view.Piece;

/**
 * Created by taoxj on 15-7-20.
 */
public class FullBoard extends AbstractBoard {

    @Override
    public List<Piece> createPieces(GameConf conf, Piece[][] pieces) {
        List<Piece> list = new ArrayList<Piece>();
        for(int i=0;i<pieces.length;i++){
            for(int j=0;j<pieces[i].length;j++){
                Piece p = new Piece(i,j);
                list.add(p);
            }
        }
        return list;
    }
}
