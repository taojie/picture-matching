package taixj.lianliankan.service;

import taixj.lianliankan.object.LinkInfo;
import taixj.lianliankan.view.Piece;

/**
 * Created by taoxj on 15-7-20.
 */
public interface GameService {
    public Piece[][] getPieces();
    void start();
    public boolean hasPieces();
    public LinkInfo link(Piece p1,Piece p2);
    public Piece findPiece(float touchX,float touchY);
}
