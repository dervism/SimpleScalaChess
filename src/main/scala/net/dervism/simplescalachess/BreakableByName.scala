package net.dervism.simplescalachess

import scala.util.control.ControlThrowable
class BreakableByName {
    def breakableByName(name: String)(op: => Unit) {
      try {
          op
        } catch {
          case ex: Throwable => ex match {
            case x: NamedBreakControl => if (x.name ne name) throw ex 
            case _ => if (!ex.isInstanceOf[NamedBreakControl]) throw ex
          }
        }
    }
    def breakn() { throw new NamedBreakControl("") }
    def breakn(s: String) { throw new NamedBreakControl(s) }
}
object BreakableByName extends BreakableByName
class NamedBreakControl(val name: String) extends ControlThrowable