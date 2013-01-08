package net.dervism.simplescalachess
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable

trait GameSearch extends Log {

  /**
   * control values
   */
  val PROGRAM = false;
  val HUMAN = true;

  /**
   * disable debug logging
   */
  override def logdebug = true

  /**
   * abstract methods subclasses need to implement in order to make new games
   */
  def drawnPosition(p: Position): Boolean
  def wonPosition(p: Position, player: Boolean): Boolean
  def positionEvaluation(p: Position, player: Boolean): Float
  def printPosition(p: Position): Unit
  def possibleMoves(p: Position, player: Boolean): Array[Position]
  def makeMove(p: Position, player: Boolean, move: Move): Position
  def reachedMaxDepth(p: Position, depth: Int): Boolean
  def createMove(): Move

  /**
   * helper class for holding search results
   */
  protected[this] class SearchResult(val score: Float, val best: List[Position])
  protected[this] object SearchResult {
    def apply(score: Float, best: List[Position]) = new SearchResult(score, best)
    def apply(score: Float) = new SearchResult(score, Nil)
  }

  protected def alphaBeta(depth: Int, p: Position, player: Boolean): SearchResult = {
    return alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
  }

  /**
   * First version; uses an imperative style to loop and recurse.
   */
  protected def alphaBetaHelper(depth: Int, p: Position, player: Boolean, alpha: Float, beta: Float): SearchResult = {
    //debug("alphaBetaHelper("+depth+", "+p.toString()+", "+alpha+", " +beta+")")
    //printPosition(p)

    if (reachedMaxDepth(p, depth)) {
      val score = positionEvaluation(p, player)
      //debug("score = " + score + " player: " + player);
      return SearchResult(score, List(p));
    }
    
    val moves = possibleMoves(p, player);
    var best = new ListBuffer[Position]
    var eval = beta
    
    breakable {
      for (move <- moves) {
        val searchResult = alphaBetaHelper(depth + 1, move, !player, -beta, -alpha)
        val next = searchResult.best
        if (next.size > 0) {
          val value = -searchResult.score

          if (value > eval) {
            //debug("! ! ! value="+value+", beta=" + eval)
            eval = value
            best = new ListBuffer[Position]
            best += move
            best ++= next.drop(1).filter(p => !p.isEmpty) // skip the first, add the remaining non-empty lists
          }

          if (eval >= alpha) {
            print("eval > alpha")
            break
          }
        }
      }
    }

    SearchResult(eval, best.toList)
  }

  /**
   * Version 2: replaced breaks with boolean
   */
  def playGame(initialPosition: Position, humanPlayFirst: Boolean): Unit = {

    var startingPosition = initialPosition

    if (humanPlayFirst == false) {
      val searchResult = alphaBeta(0, startingPosition, PROGRAM)
      val v: List[Position] = searchResult.best
      startingPosition = v(0)
    }

    var end = false

    while (!end) {
      printPosition(startingPosition)
      
      println("Your move:")

      val chessMove = createMove()

      startingPosition = makeMove(startingPosition, HUMAN, chessMove)
      printPosition(startingPosition)

      val searchResult = alphaBeta(0, startingPosition, PROGRAM)
      val v: List[Position] = searchResult.best

      for (o <- v) {
        println(" next element: " + o)
      }

      startingPosition = v(0)
      if (wonPosition(startingPosition, PROGRAM)) {
        println("Program won")
        end = true
      }
      if (wonPosition(startingPosition, HUMAN)) {
        println("Human won")
        end = true
      }
      if (drawnPosition(startingPosition)) {
        println("Drawn game")
        end = true
      }
    }
  }

}

