/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Formatter;
import java.util.Objects;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Ayela Chughtai
 */
class Board {

    /**
     * Default number of moves for each side that results in a draw.
     */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /**
     * Pattern describing a valid square designator (cr).
     */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /**
     * A Board whose initial contents are taken from INITIALCONTENTS
     * and in which the player playing TURN is to move. The resulting
     * Board has
     * get(col, row) == INITIALCONTENTS[row][col]
     * Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     * <p>
     * CAUTION: The natural written notation for arrays initializers puts
     * the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /**
     * A new board in the standard initial position.
     */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /**
     * A Board whose initial contents and state are copied from
     * BOARD.
     */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /**
     * Set my state to CONTENTS with SIDE to move.
     */
    void initialize(Piece[][] contents, Piece side) {
        assert side != null && contents.length == 8
                && contents[0].length == 8;

        for (Square sq: ALL_SQUARES) {
            set(sq, contents[sq.row()][sq.col()]);
        }
        _moves.clear();
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        _winnerKnown = false;
        _subsetsInitialized = false;
    }

    /**
     * Set me to the initial configuration.
     */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /**
     * Set my state to a copy of BOARD.
     */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        } else {
            for (Square sq: ALL_SQUARES) {
                _board[sq.index()] = board.get(sq);
            }
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        _subsetsInitialized = board._subsetsInitialized;
        _winnerKnown = false;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /**
     * Return the contents of the square at SQ.
     */
    Piece get(Square sq) {
        return _board[sq.index()];
    }
    /**
     * Returns white region sizes.
     */
    ArrayList<Integer> whiteRegionSizes() {
        return _whiteRegionSizes;
    }
    /**
     * Returns black region sizes.
     */
    ArrayList<Integer> blackRegionSizes() {
        return _blackRegionSizes;
    }

    /**
     * Set the square at SQ to V and set the side that is to move next
     * to NEXT, if NEXT is not null.
     */
    void set(Square sq, Piece v, Piece next) {
        assert sq != null;
        assert v != null;
        if (next == null) {
            _board[sq.index()] = v;
        } else {
            _board[sq.index()] = v;
            _turn = next;
        }
    }

    /**
     * Set the square at SQ to V, without modifying the side that
     * moves next.
     */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /**
     * Set limit on number of moves by each side that results in a tie to
     * LIMIT, where 2 * LIMIT > movesMade().
     */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /**
     * Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     * is false.
     */
    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        Piece to = _board[move.getTo().index()];
        set(move.getFrom(), EMP);
        if (to == _turn.opposite()) {
            _moves.add(0, move.captureMove());
        } else {
            _moves.add(0, move);
        }
        set(move.getTo(), _turn);
        _turn = _turn.opposite();
        _winnerKnown = false;
        _subsetsInitialized = false;
        if (winner() != null) {
            _winnerKnown = true;
        }
    }

    /**
     * Retract (unmake) one move, returning to the state immediately before
     * that move.  Requires that movesMade () > 0.
     */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(0);
        set(move.getFrom(), _turn.opposite());
        if (!move.isCapture()) {
            set(move.getTo(), EMP);
        } else {
            set(move.getTo(), _turn);
        }
        _turn =  _turn.opposite();
        _winnerKnown = false;
        _subsetsInitialized = false;
    }

    /**
     * Return the Piece representing who is next to move.
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return true iff FROM - TO is a legal move for the player currently on
     * move.
     */
    boolean isLegal(Square from, Square to) {
        if (from == to) {
            return false;
        }
        if (piecesOnLine(from, from.direction(to))
                != from.distance(to)) {
            return false;
        }
        if (blocked(from, to)) {
            return false;
        }
        if (_board[from.index()] == null) {
            return false;
        }
        if (_board[from.index()] != turn()) {
            return false;
        }
        if (_board[from.index()] == EMP) {
            return false;
        }
        int dir = from.direction(to);
        int steps = piecesOnLine(from, from.direction(to));
        if (from.moveDest(dir, steps) != null) {
            return get(Objects.requireNonNull(
                    from.moveDest(dir, steps))) == get(to);
        }
        return true;
    }

    /**
     * Return true iff MOVE is legal for the player currently on move.
     * The isCapture() property is ignored.
     */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /**
     * Return a sequence of all legal moves from this position.
     */
    List<Move> legalMoves() {
        LinkedList<Move> moves = new LinkedList<>();
        for (Square sq : ALL_SQUARES) {
            if (_board[sq.index()] == turn()
                    && legalMovesFromSquare(sq) != null) {
                for (Move move : legalMovesFromSquare(sq)) {
                    if (move != null) {
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Returns all legal moves from square.
     * @param sq square.
     */
    LinkedList<Move> legalMovesFromSquare(Square sq) {
        Move move;
        LinkedList<Move> result = new LinkedList<>();
        for (int dir = 0; dir < 8; dir++) {
            move = legalMoveInDirection(sq, dir);
            if (move != null && isLegal(move)) {
                result.add(move);
            }
        }
        return result;
    }

    /** Return legal move from this position
     * in direction dir.
     * @param sq square.
     * @param direction direction.
     * */
    Move legalMoveInDirection(Square sq, int direction) {
        Move move = null;
        Square to = sq.moveDest(direction,
                piecesOnLine(sq, direction));
        if (to != null && isLegal(sq, to)) {
            if (_board[to.index()].equals(
                            _board[sq.index()].opposite())) {
                move = Move.mv(sq, to, true);
            } else {
                move = Move.mv(sq, to, false);
            }
        }
        return move;
    }

    /** Return number of pieces on a line.
     * @param sq square.
     * @param direction direction.
     * @return int.
     * */
    int piecesOnLine(Square sq, int direction) {
        Integer[] piecesInDir = new Integer[8];
        for (int dir = 0; dir < 8; dir += 1) {
            piecesInDir[dir] = 0;
            for (int step = 1; step < 8; step++) {
                if (sq.moveDest(dir, step) != null) {
                    if (!_board[sq.moveDest(dir, step).index()].equals(EMP)) {
                        piecesInDir[dir] += 1;
                    }
                }
            }
        }
        if (direction == 0 || direction == 4) {
            return piecesInDir[0] + piecesInDir[4] + 1;
        } else if (direction == 1 || direction == 5) {
            return piecesInDir[1] + piecesInDir[5] + 1;
        } else if (direction == 2 || direction == 6) {
            return piecesInDir[2] + piecesInDir[6] + 1;
        } else if (direction == 3 || direction == 7) {
            return piecesInDir[3] + piecesInDir[7] + 1;
        } else {
            return 0;
        }
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (piecesContiguous(WP) && !piecesContiguous(BP)
                    && movesMade() <= _moveLimit) {
                _winner = WP;
            } else if (!piecesContiguous(WP) && piecesContiguous(BP)
                    && movesMade() <= _moveLimit) {
                _winner = BP;
            } else if (!piecesContiguous(WP) && !piecesContiguous(BP)
                    && movesMade() >= _moveLimit) {
                _winner = EMP;
            } else if (piecesContiguous(WP) && piecesContiguous(BP)
                    && movesMade() <= _moveLimit) {
                _winner = turn().opposite();
            } else {
                _winner = null;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        boolean blocked = false;
        int fromIndex = from.index();
        int toIndex = to.index();
        int direction = from.direction(to);
        for (Square cur = from.moveDest(direction, 1);
             cur != to; cur = cur.moveDest(direction, 1)) {
            assert cur != null;
            int currIndex = cur.index();
            if (_board[currIndex].abbrev().equals(
                    Objects.requireNonNull(_board[fromIndex].opposite())
                            .abbrev())) {
                blocked = true;
            }
        }
        if (_board[toIndex].abbrev().equals(_board[fromIndex].abbrev())) {
            blocked = true;
        }
        return blocked;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        assert get(sq) != null;
        assert p != null;
        assert visited[sq.row()][sq.col()] || !visited[sq.row()][sq.col()];
        if (p == EMP || get(sq) != p || visited[sq.row()][sq.col()]) {
            return 0;
        }
        if (get(sq) == p && !visited[sq.row()][sq.col()]) {
            visited[sq.row()][sq.col()] = true;
        }
        int output = 1;
        for (Square square: sq.adjacent()) {
            output += numContig(square, visited, p);
        }
        return output;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (Square sq: ALL_SQUARES) {
            Piece p = _board[sq.index()];
            if (!visited[sq.row()][sq.col()]) {
                int num = numContig(sq, visited, p);
                if (num > 0) {
                    if (p == WP) {
                        _whiteRegionSizes.add(num);
                    } else if (p == BP) {
                        _blackRegionSizes.add(num);
                    }
                }
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;
    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
