/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import static loa.Piece.*;

/** An automated Player.
 *  @author Ayela Chughtai
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        _prevRegionSizeWP = 0;
        _prevRegionSizeBP = 0;
    }

    @Override
    String getMove() {
        Move choice;
        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.gameOver()) {
            return heuristic(board);
        }
        int bestScore = 0;
        if (sense == 1) {
            bestScore = -10;
            for (Move move: board.legalMoves()) {
                board.makeMove(move);
                int score = findMove(board, depth - 1,
                        false, sense, alpha, beta);
                board.retract();
                alpha = Math.max(score, alpha);
                if (score >= bestScore) {
                    bestScore = score;
                    if (saveMove) {
                        _foundMove = move;
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
        } else if (sense == -1) {
            bestScore = 10;
            for (Move move: board.legalMoves()) {
                board.makeMove(move);
                int score = findMove(board, depth - 1,
                        false, sense, alpha, beta);
                board.retract();
                beta = Math.min(score, beta);
                if (score <= bestScore) {
                    bestScore = score;
                    if (saveMove) {
                        _foundMove = move;
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return bestScore;
    }

    /** Returns heuristic value.
     * @param board is current board.
     * */
    private int heuristic(Board board) {
        if (board.winner() == EMP) {
            return 0;
        } else if (board.winner() == WP) {
            return  WINNING_VALUE;
        } else if (board.winner() == BP) {
            return -WINNING_VALUE;
        }
        int totalScore = (board.getRegionSizes(BP).size())
                - (board.getRegionSizes(WP).size());
        if (_prevRegionSizeWP != 0
                && board.getRegionSizes(WP).size() < _prevRegionSizeWP) {
            totalScore += 1;
        } else if (board.getRegionSizes(WP).size() > _prevRegionSizeWP) {
            totalScore -= 1;
        }
        if (_prevRegionSizeBP != 0
                && board.getRegionSizes(BP).size() < _prevRegionSizeBP) {
            totalScore -= 1;
        } else if (board.getRegionSizes(BP).size() > _prevRegionSizeBP) {
            totalScore += 1;
        }
        if (board.getRegionSizes(BP).size()
                > board.getRegionSizes(WP).size()) {
            totalScore += 1;
        } else {
            totalScore -= 1;
        }
        if (_foundMove != null && _foundMove.isCapture()) {
            totalScore += 1;
        }
        _prevRegionSizeWP = board.getRegionSizes(WP).size();
        _prevRegionSizeBP = board.getRegionSizes(BP).size();
        return totalScore;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 2;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

    /** Previous region size. */
    private int _prevRegionSizeWP;
    /** Previous region size. */
    private int _prevRegionSizeBP;

}


