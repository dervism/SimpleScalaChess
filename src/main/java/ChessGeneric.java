
//import java.util.*;
//import java.io.*;
//

public class ChessGeneric {
//    private class SearchResult {
//        public float score; 
//        public List<Position> boardAfterTestMoves;
//        public SearchResult(float r, List<Position> list) {}
//    }
//    
//	public static final boolean DEBUG = false;
//
//    public static boolean PROGRAM = false;
//    public static boolean HUMAN = true;
//
//    /*
//     *                Search methods:
//     */
//
//    protected SearchResult alphaBeta(int depth, Position p, boolean player) {
//        return alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
//    }
//
//    protected SearchResult alphaBetaHelper(int depth, Position p,
//                                     boolean player, float alpha, float beta) {
//        if (Chess.DEBUG) System.out.println("alphaBetaHelper("+depth+","+p+","+alpha+","+beta+")");
//        if (reachedMaxDepth(p, depth)) {
//        	float score = positionEvaluation(p, player);
//        	if(Chess.DEBUG) System.out.println("score = " + score + " player: " + player);
//        	return new SearchResult(score, Arrays.asList(p));
//        }
//        List<Position> best = new ArrayList<Position>();
//        Position [] moves = possibleMoves(p, player);
//        for (int i=0; i<moves.length; i++) {
//        	SearchResult sr =
//        		  alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
//        	List<Position> v2 = sr.boardAfterTestMoves;
//            //  if (v2 == null || v2.size() < 1) continue;
//            //System.out.println("v2:" + v2);
//            if (v2.size() > 0) {
//            	float value = -sr.score;
//            	if (value > beta) {
//            		if(Chess.DEBUG) System.out.println(" ! ! ! value="+value+", beta="+beta);
//            		beta = value;
//            		best =  new ArrayList<Position>();
//            		best.add(moves[i]);
//            		boolean skip = true;
//            		for (Position scp : v2) {
//            			if (!skip) {
//            			    if (!scp.isEmpty()) {
//            			    	best.add(scp);
//            			    }
//            			}
//            			skip = false;
//            		}
//            	}
//            }
//            /**
//             * Use the alpha-beta cutoff test to abort search if we
//             * found a move that proves that the previous move in the
//             * move chain was dubious
//             */
//            if (beta >= alpha) {
//                break;
//            }
//        }
//        return new SearchResult(beta, best);
//    }
//    public void playGame(Position startingPosition, boolean humanPlayFirst) {
//        if (humanPlayFirst == false) {
//        	SearchResult sr = alphaBeta(0, startingPosition, PROGRAM);
//        	List<Position> v = sr.boardAfterTestMoves;
//            startingPosition = v.get(0);
//        }
//        while (true) {
//            printPosition(startingPosition);
//            if (wonPosition(startingPosition, PROGRAM)) {
//                System.out.println("Program won");
//                break;
//            }
//            if (wonPosition(startingPosition, HUMAN)) {
//                System.out.println("Human won");
//                break;
//            }
//            if (drawnPosition(startingPosition)) {
//                System.out.println("Drawn game");
//                break;
//            }
//            System.out.println("Your move:");
//            ChessMove move = createMove();
//            startingPosition = makeMove(startingPosition, HUMAN, move);
//            printPosition(startingPosition);
//            SearchResult sr = alphaBeta(0, startingPosition, PROGRAM);
//            List<Position> v = sr.boardAfterTestMoves;
//            for (Object o : v) {
//                System.out.println(" next element: " + o);
//            }
//            startingPosition = v.get(0);        
//        }
//    }
//
//    /**
//     *                        Chess specific code:
//     */
//    
//    
//    public boolean drawnPosition(Position p) {
//        /**
//         * We want to keep searching:
//         */
//        return false;
//    }
//
//    public boolean wonPosition(Position p, boolean player) {
//        // eventually, we should check for checkmates here...
//        return false;
//    }
//  
//    final public float positionEvaluation(Position pos, boolean player) {
//        int [] b = pos.board;
//        float ret = 0.0f;
//        // adjust for material:
//        for (int i=22; i<100; i++) {
//            if (b[i] != 0 && b[i] != 7)  ret += 1.75f * b[i];
//        }
//    
//        // adjust for positional advantages:
//        setControlData(pos);
//        int control = 0;
//        for (int i=22; i<100; i++) {
//            control += humanControl[i];
//            control -= computerControl[i];
//        }
//        // Count center squares extra:
//        control += humanControl[55] - computerControl[55];
//        control += humanControl[56] - computerControl[56];
//        control += humanControl[65] - computerControl[65];
//        control += humanControl[66] - computerControl[66];
//
//        control /= 10.0f;
//        ret += control;
//
//        // credit for attacked pieces:
//        for (int i=22; i<100; i++) {
//            if (b[i] == 0 || b[i] == 7) continue;
//            if (b[i] < 0) {
//                if (humanControl[i] > computerControl[i]) {
//                    ret += 0.9f * value[-b[i]];
//                }
//                if (b[i] == -Position.QUEEN && humanControl[i]>0) ret += 2;
//                if (b[i] == -Position.KING && humanControl[i]>0)  ret += 4;
//            } 
//            if (b[i] > 0) {
//                if (humanControl[i] < computerControl[i]) {
//                    ret -= 0.9f * value[b[i]];
//                }
//                if (b[i] == Position.QUEEN && humanControl[i]>0) ret -= 2;
//                if (b[i] == Position.KING && humanControl[i]>0)  ret -= 4;
//            }
//        }
//        // adjust if computer side to move:
//        if (!player) ret = -ret;
//        return ret;
//    }
//
//    public void printPosition(Position p) {
//        System.out.println("Board position:");
//       Position pos = (Position)p;
//        int [] b = pos.board;
//        for (int col=92; col>=22; col-=10) {
//            System.out.println();
//            for (int ii=0; ii<8; ii++) {
//                int i = ii + col;
//                if (b[i] != 0) {
//                    System.out.print(pp(b[i], i));
//                } else {
//                    boolean white_sq = true;
//                    for (int k=0; k<blackSquares.length; k++) {
//                        if (i == blackSquares[k]) {
//                            white_sq = false;
//                            break;
//                        }
//                    }
//                    if (white_sq) System.out.print("   ");
//                    else          System.out.print(" . ");
//                }
//            }
//        }
//        System.out.println();
//    }
//    private String pp(int piece, int square_index) {
//        if (piece == 0) return "   ";
//        String color;
//        if (piece < 0) color = "B"; else color = "W";
//        int p = piece; if (p < 0) p = -p;
//        switch (p) {
//        case 1: return " " + color + "P";
//        case 2: return " " + color + "N";
//        case 3: return " " + color + "B";
//        case 4: return " " + color + "R";
//        case 5: return " " + color + "Q";
//        case 9: return " " + color + "K";
//        }
//        return "error";
//    }
//
//    final public Position [] possibleMoves(Position p, boolean player) {
//        if (Chess.DEBUG) System.out.println("posibleMoves("+p+","+player+")");
//        //System.out.println("Chess.possibleMoves(): pos=" + pos);
//        //for (int i=22; i<40; i++) System.out.println(pos.board[i]);
//        int num = calcPossibleMoves(p, player);
//        if (num == 0) {
//            System.out.println("Stalemate");
//            System.exit(0);
//        }
//        Position [] chessPos = new Position[num];
//        for (int i=0; i<num; i++) {
//            chessPos[i] = new Position();
//            for (int j=22; j<100; j++) chessPos[i].board[j] = p.board[j];
//            chessPos[i].board[possibleMoveList[i].to] = chessPos[i].board[possibleMoveList[i].from];
//            chessPos[i].board[possibleMoveList[i].from] = 0;
//        }
//        return chessPos;
//    }
//    public Position makeMove(Position pos, boolean player, ChessMove m) {
//        if (Chess.DEBUG) System.out.println("Entered Chess.makeMove");
//        Position pos2 = new  Position();
//        for (int i=0; i<120; i++) pos2.board[i] = pos.board[i];
//        int pp;
//        if (player) pp =  1;
//        else        pp = -1;
//        if (Chess.DEBUG) System.out.println("makeMove: m.from = " + m.from +
//                                                 ", m.to = " + m.to);
//        pos2.board[m.to] = pos2.board[m.from];
//        pos2.board[m.from] = 0;
//        return pos2;
//    }
//    
//    final public boolean reachedMaxDepth(Position p, int depth) {
//        if (depth < 5) return false;
//        return true;
//    }
//
//    private BufferedReader in = null;
//
//    public ChessMove createMove() {
//        if (Chess.DEBUG) System.out.println("Enter blank square index [0,8]:");
//        ChessMove mm = new ChessMove();
//        System.out.println("enter a move like 'd2d4' or 'oo'");
//        try {
//            if (in == null) {
//                in = new BufferedReader(new InputStreamReader(System.in));
//            }
//            String s = in.readLine().toLowerCase();
//            System.out.println("s="+s);
//            
//            // TBD: check for oo and ooo:
//            
//            char c0 = (char)(s.charAt(0) - 'a' + 2);
//            char r0 = (char)(s.charAt(1) - '1' + 2);
//            char c1 = (char)(s.charAt(2) - 'a' + 2);
//            char r1 = (char)(s.charAt(3) - '1' + 2);
//            mm.from = r0*10+c0;
//            mm.to   = r1*10+c1;
//            System.out.println("From " + mm.from + ", to " + mm.to);
//        } catch (Exception e) { System.out.println(e); }
//        return mm;
//    }
//
//    static public void main(String [] args) {
//    	Position p = new Position();
//        for (int i=0; i<120; i++) p.board[i] = initialBoard[i];
//        Chess ttt = new Chess();
//        /* DEBUG*/ //  ttt.setControlData(p);
//        ttt.playGame(p, true);
//    }
//
//    /**
//     *             PRIVATE API, mostly chess move and evaluation utilities
//     */
//
//    // static data that can be re-used (assume single threading!)
//    static private float [] computerControl = new float[120];
//    static private float [] humanControl    = new float[120];
//    
//
//    private void setControlData(Position pos) {
//        for (int i=0; i<120; i++) {
//            computerControl[i] = 0;
//            humanControl[i] = 0;
//        }
//        int [] b = pos.board;
//        float [] control; // set to computerControl or humanControl, as appropriate
//        for (int square_index=22; square_index<100; square_index++) {
//            int piece = b[square_index];
//            if (piece == 7 || piece == 0) continue;
//            int piece_type = piece;
//            if (piece_type < 0) {
//                piece_type = -piece_type;
//                control = computerControl;
//            } else {
//                control = humanControl;
//            }
//            int count = 0, side_index, move_offset, temp, next_square;
//            int piece_index = index[piece_type];
//            int move_index = pieceMovementTable[piece_index];
//            if (piece < 0) side_index = -1;
//            else           side_index = 1;
//            switch (piece_type) {
//            case Position.PAWN:
//                {
//                    // first check for possible pawn captures:
//                    for (int delta=-1; delta<= 1; delta += 2) {
//                        move_offset = square_index + side_index * 10 + delta;
//                        control[move_offset] += 1.1f;
//                        int target = b[move_offset];
//                        if ((target <= -1 && target != 7 && piece > 0) ||
//                            (target >= 1 && target != 7 && piece < 0)) {
//                            // kluge: count pawn control more:
//                            control[square_index + side_index * delta] += 1.25f;
//                        }
//                    }
//                }
//                // Note: no break here: we want pawns to use move table also:
//                break; // ??
//
//
//            case Position.KNIGHT:
//            case Position.BISHOP:
//            case Position.ROOK:
//            case Position.KING:
//            case Position.QUEEN:
//                {
//                    move_index = piece; if (move_index < 0) move_index = -move_index;
//                    move_index = index[move_index];
//                    //System.out.println("move_index="+move_index);
//                    next_square = square_index + pieceMovementTable[move_index];
//                outer:
//                    while (true) {
//                    inner:
//                        while (true) {
//                            if (next_square > 99) break inner;
//                            if (next_square < 22) break inner;
//                            if (b[next_square] == 7) break inner;
//                            control[next_square] += 1;
//                            // the next statement should be augmented for x-ray analysis:
//                            if (side_index < 0 && b[next_square] < 0) break inner;
//                            if (side_index > 0 && b[next_square] > 0 && b[next_square] != 7) break inner;
//                            // NOTE: prevents calculating guarding:
//                            //if (b[next_square] != 0) break inner;
//
//                            if (piece_type == Position.PAWN &&
//                                (square_index / 10 == 3))  break inner;
//                            if (piece_type == Position.KNIGHT) break inner;
//                            if (piece_type == Position.KING) break inner;
//                            next_square += pieceMovementTable[move_index];
//                        }
//                        move_index += 1;
//                        if (pieceMovementTable[move_index] == 0) break outer;
//                        next_square = square_index + pieceMovementTable[move_index];
//                    }
//                }
//            }
//        }
//        if (false) {
//            printPosition(pos);
//            System.out.println("Human control:");
//            for (int col = 92; col >= 22; col -= 10) {
//                System.out.println();
//                for (int ii = 0; ii < 8; ii++) {
//                    int i = ii + col;
//
//                    //for (int i=99; i>=22; i--) {
//                    //if (b[i] == 7 && b[i + 1] == 7) {
//                    //    System.out.println();
//                    // }
//                    if (b[i] != 7) {
//                        System.out.print(humanControl[i] + " ");
//                    }
//                }
//            }
//            System.out.println();
//            System.out.println("Computer control:");
//            for (int col = 92; col >= 22; col -= 10) {
//                System.out.println();
//                for (int ii = 0; ii < 8; ii++) {
//                    int i = ii + col;
//                    //if (b[i] == 7 && b[i + 1] == 7) {
//                    //    System.out.println();
//                    //}
//                    if (b[i] != 7) {
//                        System.out.print(computerControl[i] + " ");
//                    }
//                }
//            }
//            System.out.println();
//            System.exit(1);
//        }
//    }
//
//    private static ChessMove [] possibleMoveList = new ChessMove[255];
//    static {
//        for (int i=0; i<255; i++) possibleMoveList[i] = new ChessMove();
//    }
//
//    private int calcPossibleMoves(Position pos, boolean player) {
//        //System.out.println("calcPossibleMoves()");
//        int [] b = pos.board;
//        int count = 0;
//        for (int i=22; i<100; i++) {
//            int board_val = b[i];
//            //System.out.println(board_val);
//            if (board_val == 7) continue;
//            // computer pieces will be negative:
//            if ((board_val < 0 && !player) || (board_val > 0 && player)) {
//                int num = calcPieceMoves(pos, i);
//                for (int j=0; j<num; j++) {
//                    if (b[piece_moves[j]] != 7) {
//                        //System.out.println("count="+count+", i="+i);
//                        possibleMoveList[count].from = i;
//                        possibleMoveList[count].to = piece_moves[j];
//                        //                      System.out.println("possible move: player="+player+
//                        //                                         ", from="+i+", to="+piece_moves[j]);
//                        count++;
//                    }
//                }
//                // TBD: castle logic, etc. (page 159)
//            }
//        }
//        return count;
//    }
//
//    private int calcPieceMoves(Position pos, int square_index) {
//        int [] b = pos.board;
//        int piece = b[square_index];
//        int piece_type = piece;
//        if (piece_type < 0) piece_type = -piece_type;
//        int count = 0, side_index, move_offset, temp, next_square;
//        int piece_index = index[piece_type];
//        int move_index = pieceMovementTable[piece_index];
//        if (piece < 0) side_index = -1;
//        else           side_index = 1;
//        switch (piece_type) {
//        case Position.PAWN:
//            {
//                // first check for possible pawn captures:
//                for (int delta=-1; delta<= 1; delta += 2) {
//                    move_offset = square_index + side_index * 10 + delta;
//                    int target = b[move_offset];
//                    if ((target <= -1 && target != 7 && piece > 0) ||
//                        (target >= 1 && target != 7 && piece < 0)) {
//                        piece_moves[count++] = square_index + side_index * 10 + delta;
//                    }
//                }
//                // check for initial pawn move of 2 squares forward:
//                move_offset = square_index + side_index * 20;
//                if (piece > 0) temp = 3; else temp = 8;
//                if (b[move_offset] == 0 &&
//                    (square_index / 10) == temp &&
//                    ((piece < 0 && b[square_index - 10]==0) ||
//                     (piece > 0 && b[square_index + 10]==0))) {
//                    piece_moves[count++] = square_index + side_index * 20;
//                }
//                // try to move forward 1 square:
//                move_offset = square_index + side_index * 10;
//                if (b[move_offset] == 0) {
//                    piece_moves[count++] = move_offset;
//                }
//            }
//            break;
//        case Position.KNIGHT:
//        case Position.BISHOP:
//        case Position.ROOK:
//        case Position.KING:
//        case Position.QUEEN:
//            {
//		move_index = piece; if (move_index < 0) move_index = -move_index;
//		move_index = index[move_index];
//		//System.out.println("move_index="+move_index);
//		next_square = square_index + pieceMovementTable[move_index];
//    outer:
//		while (true) {
//		inner:
//		    while (true) {
//			if (next_square > 99) break inner;
//			if (next_square < 22) break inner;
//                        if (b[next_square] == 7) break inner;
//			
//                        // check for piece on the same side:
//                        if (side_index < 0 && b[next_square] < 0) break inner;
//                        if (side_index >0 && b[next_square]  > 0) break inner;
//			
//                        piece_moves[count++] = next_square;
//                        if (b[next_square] != 0) break inner;
//                        if (piece_type == Position.KNIGHT) break inner;
//                        if (piece_type == Position.KING) break inner;
//                        next_square += pieceMovementTable[move_index];
//		    }
//		    move_index += 1;
//		    if (pieceMovementTable[move_index] == 0) break outer;
//		    next_square = square_index + pieceMovementTable[move_index];
//		}
//	    }
//        }
//        return count;
//    }
//
//    private static int [] piece_moves = new int[255];
//
//    private static int [] initialBoard = {
//        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
//        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
//        4, 2, 3, 5, 9, 3, 2, 4, 7, 7,   // white pieces
//        1, 1, 1, 1, 1, 1, 1, 1, 7, 7,   // white pawns
//        0, 0, 0, 0, 0, 0, 0, 0, 7, 7,   // 8 blank squares, 2 off board
//        0, 0, 0, 0, 0, 0, 0, 0, 7, 7,   // 8 blank squares, 2 off board
//        0, 0, 0, 0, 0, 0, 0, 0, 7, 7,   // 8 blank squares, 2 off board
//        0, 0, 0, 0, 0, 0, 0, 0, 7, 7,   // 8 blank squares, 2 off board
//        -1,-1,-1,-1,-1,-1,-1,-1, 7, 7,  // black pawns
//        -4,-2,-3,-5,-9,-3,-2,-4, 7, 7,  // black pieces
//        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
//        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
//    };
//
//    private static int [] index = {
//        0, 12, 15, 10, 1, 6, 0, 0, 0, 6
//    };
//
//    private static int [] pieceMovementTable = {
//        0, -1, 1, 10, -10, 0, -1, 1, 10, -10, -9, -11, 9,
//        11, 0, 8, -8, 12, -12, 19, -19, 21, -21, 0, 10, 20,
//        0, 0, 0, 0, 0, 0, 0, 0
//    };
//    private static int [] value = {
//        0, 1, 3, 3, 5, 9, 0, 0, 0, 20
//    };
//    private static int [] blackSquares = {
//        22, 24, 26, 28, 33, 35, 37, 39,
//        42, 44, 46, 48, 53, 55, 57, 59,
//        62, 64, 66, 68, 73, 75, 77, 79,
//        82, 84, 86, 88, 93, 95, 97, 99
//    };
}
