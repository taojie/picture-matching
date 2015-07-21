package taixj.lianliankan.service;

import taixj.lianliankan.GameConf;
import taixj.lianliankan.board.FullBoard;
import taixj.lianliankan.view.Piece;

/**
 * Created by taoxj on 15-7-21.
 */
public class GameServiceImpl implements GameService {
    Piece[][] pieces;
    GameConf conf;
    public GameServiceImpl(GameConf conf){
        this.conf = conf;
    }
     @Override
    public Piece[][] getPieces() {
        FullBoard board = new FullBoard();
         pieces = board.create(conf);
        return pieces;
    }

    @Override
    public void start() {

    }
}
