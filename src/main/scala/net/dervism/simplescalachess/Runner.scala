package net.dervism.simplescalachess

import net.dervism.simplescalachess.BreakableByName._

/**
 * Hello world!
 *
 */
object Runner extends App with Log {
  override def loginfo = true
  
  // checking logging
  info("Hello World!")
  
  // checking sorting & max
  class sort {
    def find(p: Array[Int]) : Int = {
      p.sorted.max // finds max with/without sorting
    }
  }
  val s = new sort().find(Array(1, 2, 5, 2, 1, 4))
  println("max: " + s)
  
  // checking the += operator
  var x = 1
  x += 5
  println("x(1) += 5: " + x)
  
  // checking for-to & for-until
  print("for-to: "); for (i <- 1 to 6) print(i)
  print("\nfor-until: "); for (i <- 1 until 6) print(i)
  print("\nfor (i <- 1 until 6 by 2): "); for (i <- 1 until 6 by 2) print(i)
  print("\nfor (i <- 6 to 0 by -1): "); for (i <- 6 to 0 by -1) print(i)
  print("\nfor (i <- 6 until 0 by -1): "); for (i <- 6 until 0 by -1) print(i)
  print("\nfor(delta <- -1 to 1 by 2)"); for(delta <- -1 to 1 by 2) print(delta)
  
  // checking array initializing
  var control = Array[Float]()
  println("control: " + control.length)
  
  // checking breakableByName (aka named loops in Java)
  breakableByName("outer") {
    for(i <- 0 to 4) {
     breakableByName("inner") {
      for(j <- 1 to 3) {
        if (j == 2) breakn("inner")
        print("j= " + j + " ")
      }
     }
     print("i= " + i + " ")
    }
  }
  
  // checking fallthrough
  val f = 2 
  f match {
    case 1 | 2 | 3 => println("\nfallthrough")
  }
  
    var p = new Position()
    for (i <- 0 until 120) p.board(i) = ChessGame.initialBoard(i)
    val ttt = new ChessGame()
    /* DEBUG*/ //  ttt.setControlData(p);
    ttt.playGame(p, true)
}
