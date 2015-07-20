package taixj.lianliankan.service;

import taixj.lianliankan.view.Piece;

/**
 * Created by taoxj on 15-7-20.
 */
public interface GameService {
    public Piece[][] getPieces();
    void start();
}
