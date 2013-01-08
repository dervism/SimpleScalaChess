package net.dervism.simplescalachess

class Move(var from: Int, var to: Int) {
  
}
object Move {
	def apply(from: Int, to: Int) = new Move(from, to)
	def apply() = new Move(0, 0)
}