package net.dervism.simplescalachess

/**
 * an interface for a game position
 */
class Position {
  
  def isEmpty: Boolean = {
//    for (i <- 0 to 120) 
//      if (board(i) != 0) return false 
//    return true
    
    board.forall(_ == 0)
  }
  
  val board = new Array[Int](120)
  
  override def toString() = {
    val sb = new StringBuffer("[")
    for (i <- 22 until 100) {
      sb.append("" + board(i) + ",")
    }
    sb.append("]")
    sb.toString()
  }
}
object Position {
  val BLANK = 0
  val HUMAN = 1
  val PROGRAM = -1
  val PAWN = 1
  val KNIGHT = 2
  val BISHOP = 3
  val ROOK = 4
  val QUEEN = 5
  val KING = 9
}