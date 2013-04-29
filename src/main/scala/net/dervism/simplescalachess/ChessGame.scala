package net.dervism.simplescalachess
import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable

class ChessGame extends GameSearch {
  val piece_moves = new Array[Int](255)

  val index = Array(
    0, 12, 15, 10, 1, 6, 0, 0, 0, 6)

  val pieceMovementTable = Array(
    0, -1, 1, 10, -10, 0, -1, 1, 10, -10, -9, -11, 9,
    11, 0, 8, -8, 12, -12, 19, -19, 21, -21, 0, 10, 20,
    0, 0, 0, 0, 0, 0, 0, 0)

  val value = Array(
    0, 1, 3, 3, 5, 9, 0, 0, 0, 20)

  val blackSquares = Array(
    22, 24, 26, 28, 33, 35, 37, 39,
    42, 44, 46, 48, 53, 55, 57, 59,
    62, 64, 66, 68, 73, 75, 77, 79,
    82, 84, 86, 88, 93, 95, 97, 99)

  val possibleMoveList = new Array[Move](255)
  for (i <- 0 until possibleMoveList.length) possibleMoveList(i) = Move()

  var control = new Array[Float](120)
  val computerControl = new Array[Float](120)
  val humanControl = new Array[Float](120)
  var in: java.io.BufferedReader = null

  def drawnPosition(p: Position): Boolean = {
    // We want to keep searching:
    false
  }

  def wonPosition(p: Position, player: Boolean): Boolean = {
    // eventually, we should check for checkmates here...
    false
  }

  def positionEvaluation(pos: Position, player: Boolean): Float = {
    //debug("positionEvaluation: " + pos)
    val b = pos.board
    var ret = 0.0f
    // adjust for material:
    for (i <- 22 until 100) {
      if (b(i) != 0 && b(i) != 7) ret += 1.75f * b(i)
    }

    // adjust for positional advantages:
    setControlData(pos)
    var control = 0.0f // TODO: why not use 'ret' instead?

    for (i <- 22 until 100) {
      control += humanControl(i)
      control -= computerControl(i)
    }
    // Count center squares extra:
    control += humanControl(55) - computerControl(55)
    control += humanControl(56) - computerControl(56)
    control += humanControl(65) - computerControl(65)
    control += humanControl(66) - computerControl(66)

    control /= 10.0f
    ret += control

    // credit for attacked pieces:
    for (i <- 22 until 100) {
      if (b(i) != 0 && b(i) != 7) {
        if (b(i) < 0) {
          if (humanControl(i) > computerControl(i)) {
            ret += 0.9f * value(-b(i))
          }
          if (b(i) == -Position.QUEEN && humanControl(i) > 0) ret += 2
          if (b(i) == -Position.KING && humanControl(i) > 0) ret += 4
        }
        if (b(i) > 0) {
          if (humanControl(i) < computerControl(i)) {
            ret -= 0.9f * value(b(i))
          }
          if (b(i) == Position.QUEEN && humanControl(i) > 0) ret -= 2
          if (b(i) == Position.KING && humanControl(i) > 0) ret -= 4
        }
      }
    }
    // adjust if computer side to move:
    if (!player) ret = -ret

    ret
  }

  def printPosition(p: Position) {
    debug("Board position:")
    val pos = p
    val b = pos.board

    for (col <- 92 to 22 by -10) {
      println(" ")
      for (ii <- 0 until 8) {
        val i = ii + col
        if (b(i) != 0) {
          print(pp(b(i), i))
        } else {
          var white_sq = true
          breakable {
            for (k <- 0 until blackSquares.length) {
              if (i == blackSquares(k)) {
                white_sq = false
                break()
              }
            }
          }
          if (white_sq) print("   ")
          else print(" . ")
        }
      }
    }
    println()
  }

  def pp(piece: Int, square_index: Int): String = {
    if (piece == 0) "   "
    var color = "W"

    if (piece < 0) color = "B"

    var p = piece
    if (p < 0) p = -p

    p match {
      case 1 => return " " + color + "P"
      case 2 => return " " + color + "N"
      case 3 => return " " + color + "B"
      case 4 => return " " + color + "R"
      case 5 => return " " + color + "Q"
      case 9 => return " " + color + "K"
    }

    "error"
  }

  def possibleMoves(p: Position, player: Boolean): Array[Position] = {
    //debug("posibleMoves(" + p + "," + player + ")");
    //System.out.println("Chess.possibleMoves(): pos=" + pos);
    //for (int i=22; i<40; i++) System.out.println(pos.board[i]);
    val num = calcPossibleMoves(p, player)
    if (num == 0) {
      System.out.println("Stalemate")
      System.exit(0)
    }
    val chessPos = new Array[Position](num)
    for (i <- 0 until num) {
      chessPos(i) = new Position()
      for (j <- 22 until 100) chessPos(i).board(j) = p.board(j)
      chessPos(i).board(possibleMoveList(i).to) = chessPos(i).board(possibleMoveList(i).from)
      chessPos(i).board(possibleMoveList(i).from) = 0
    }
    chessPos
  }

  def makeMove(pos: Position, player: Boolean, m: Move): Position = {
    debug("Entered Chess.makeMove")
    val pos2 = new Position()

    for (i <- 0 until 120) pos2.board(i) = pos.board(i)
    var pp = 0

    if (player) pp = 1
    else pp = -1

    debug("makeMove: m.from = " + m.from + ", m.to = " + m.to)

    pos2.board(m.to) = pos2.board(m.from)
    pos2.board(m.from) = 0
    pos2
  }

  def reachedMaxDepth(p: Position, depth: Int): Boolean = {
    if (depth < 5) return false
    true
  }

  def createMove(): Move = {
    debug("Enter blank square index [0,8]:")
    val mm = Move()
    System.out.println("enter a move like 'd2d4' or 'oo'")

    try {
      if (in == null) {
        in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in))
      }
      val s = in.readLine().toLowerCase
      System.out.println("s=" + s)

      // TODO: check for oo and ooo:

      val c0 = (s.charAt(0) - 'a' + 2).toChar
      val r0 = (s.charAt(1) - '1' + 2).toChar
      val c1 = (s.charAt(2) - 'a' + 2).toChar
      val r1 = (s.charAt(3) - '1' + 2).toChar
      mm.from = r0 * 10 + c0
      mm.to = r1 * 10 + c1
      System.out.println("From " + mm.from + ", to " + mm.to)
    } catch {
      case e: Exception => error(e)
    }

    mm
  }

  def setControlData(pos: Position) {
    //debug("setControlData: " + pos)

    for (i <- 0 until 120) {
      computerControl(i) = 0
      humanControl(i) = 0
    }

    val b = pos.board

    for (square_index <- 22 until 100) {
      val piece = b(square_index)

      if (piece != 7 && piece != 0) {
        var piece_type = piece

        if (piece_type < 0) {
          piece_type = -piece_type
          control = computerControl
        } else control = humanControl

        var count, side_index, move_offset, temp, next_square = 0
        val piece_index = index(piece_type)
        var move_index = pieceMovementTable(piece_index)

        if (piece < 0) side_index = -1 else side_index = 1

        if (piece_type == Position.PAWN) {
          // first check for possible pawn captures:
          for (delta <- -1 to 1 by 2) {
            move_offset = square_index + side_index * 10 + delta
            control(move_offset) += 1.1f
            val target = b(move_offset)
            if ((target <= -1 && target != 7 && piece > 0) ||
              (target >= 1 && target != 7 && piece < 0)) {
              // kluge: count pawn control more:
              control(square_index + side_index * delta) += 1.25f
            }
          }
        } else if (piece_type == Position.KNIGHT || piece_type == Position.BISHOP || piece_type == Position.ROOK ||
          piece_type == Position.KING || piece_type == Position.QUEEN) {
          move_index = piece
          if (move_index < 0) move_index = -move_index
          move_index = index(move_index)
          do {
            next_square = square_index + pieceMovementTable(move_index)
            breakable {
              while ((next_square <= 99 && next_square >= 22 && b(next_square) != 7)) {
                // the next statement should be augmented for x-ray analysis:
                if ((side_index < 0 && b(next_square) < 0) ||
                  (side_index > 0 && b(next_square) > 0 && b(next_square) != 7)) break()
                // NOTE: prevents calculating guarding:
                //if (b[next_square] != 0) break inner;

                control(next_square) += 1.0f
                if ((piece_type == Position.PAWN && (square_index / 10 == 3)) ||
                  (piece_type == Position.KNIGHT || piece_type == Position.KING)) break()

                next_square += pieceMovementTable(move_index)
              }
            }
            move_index += 1
          } while (pieceMovementTable(move_index) != 0)
        }
      }
    }
  }

  def calcPossibleMoves(pos: Position, player: Boolean): Int = {
    //debug("calcPossibleMoves: " + pos);
    val b = pos.board
    var count = 0
    for (i <- 22 until 100) {
      val board_val = b(i)
      //System.out.println(board_val);
      if (board_val != 7) {
        // computer pieces will be negative:
        if ((board_val < 0 && !player) || (board_val > 0 && player)) {
          val num = calcPieceMoves(pos, i)
          for (j <- 0 until num) {
            if (b(piece_moves(j)) != 7) {
              //System.out.println("count="+count+", i="+i);
              possibleMoveList(count).from = i
              possibleMoveList(count).to = piece_moves(j)
              //    System.out.println("possible move: player="+player+ ", from="+i+", to="+piece_moves[j]);
              count = count + 1
            }
          }
          // TBD: castle logic, etc. (page 159)
        }
      }
    }
    count
  }

  def calcPieceMoves(pos: Position, square_index: Int): Int = {
    //debug("calcPieceMoves: " + pos)
    val b = pos.board
    val piece = b(square_index)
    var piece_type = piece

    if (piece_type < 0) piece_type = -piece_type

    var count, side_index, move_offset, temp, next_square = 0
    val piece_index = index(piece_type)
    var move_index = pieceMovementTable(piece_index)

    if (piece < 0) side_index = -1 else side_index = 1

    piece_type match {
      case Position.PAWN => {
        // first check for possible pawn captures:
        for (delta <- -1 to 1 by 2) {
          move_offset = square_index + side_index * 10 + delta
          val target = b(move_offset)
          if ((target <= -1 && target != 7 && piece > 0) ||
            (target >= 1 && target != 7 && piece < 0)) {
            count = count + 1
            piece_moves(count) = square_index + side_index * 10 + delta
          }
        }
        // check for initial pawn move of 2 squares forward:
        move_offset = square_index + side_index * 20
        if (piece > 0) temp = 3 else temp = 8
        if (b(move_offset) == 0 &&
          (square_index / 10) == temp &&
          ((piece < 0 && b(square_index - 10) == 0) ||
            (piece > 0 && b(square_index + 10) == 0))) {
          count += 1
          piece_moves(count) = square_index + side_index * 20
        }
        // try to move forward 1 square:
        move_offset = square_index + side_index * 10
        if (b(move_offset) == 0) {
          count += 1
          piece_moves(count) = move_offset
        }
      }
      case Position.KNIGHT | Position.BISHOP | Position.ROOK | Position.KING | Position.QUEEN => {
        move_index = piece
        if (move_index < 0) move_index = -move_index
        move_index = index(move_index)

        do {
          next_square = square_index + pieceMovementTable(move_index)
          breakable {
            while ((next_square <= 99 && next_square >= 22 && b(next_square) != 7)) {

              // check for piece on the same side:
              if (side_index < 0 && b(next_square) < 0) break()
              if (side_index > 0 && b(next_square) > 0) break()

              count += 1
              piece_moves(count) = next_square

              if (b(next_square) != 0) break()
              if (piece_type == Position.KNIGHT | piece_type == Position.KING) break()

              next_square += pieceMovementTable(move_index)
            }
          }
          move_index += 1
        } while (pieceMovementTable(move_index) != 0)
      }
    }
    count
  }
}
object ChessGame {
  val initialBoard = Array(
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    4, 2, 3, 5, 9, 3, 2, 4, 7, 7, // white pieces
    1, 1, 1, 1, 1, 1, 1, 1, 7, 7, // white pawns
    0, 0, 0, 0, 0, 0, 0, 0, 7, 7, // 8 blank squares, 2 off board
    0, 0, 0, 0, 0, 0, 0, 0, 7, 7, // 8 blank squares, 2 off board
    0, 0, 0, 0, 0, 0, 0, 0, 7, 7, // 8 blank squares, 2 off board
    0, 0, 0, 0, 0, 0, 0, 0, 7, 7, // 8 blank squares, 2 off board
    -1, -1, -1, -1, -1, -1, -1, -1, 7, 7, // black pawns
    -4, -2, -3, -5, -9, -3, -2, -4, 7, 7, // black pieces
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7)
}
