package net.dervism.simplescalachess

trait Log {
  self =>
  
  def debug(s: String) = println(if(logdebug) "debug [" + self.getClass().getSimpleName() + ", " + time + "]: " + s)
  def error(s: String) = println("error [" + self.getClass().getSimpleName() + ", " + time + "]: " + s)
  def error(e: Exception) = println("error [" + self.getClass().getSimpleName() + ", " + time + "]: " + e.getStackTraceString)
  def info(s: String) = println(if(loginfo) "info [" + self.getClass().getSimpleName() + ", " + time + "]: " + s)
  
  /**
   * can be overridden to enable/disable debug&info logging
   */
  def logdebug = false
  def loginfo = true
  
  /**
   * returns the current time
   */
  def time = {
    val millis = System.currentTimeMillis()
    val seconds = (millis / 1000) % 60
    val minutes = ((millis / 60000) % 60)
    val hours   = ((millis / (1000*3600) % 24)) + 1
    def now(x: Long) = if (x < 10) "0"+x else x
    now(hours) + ":" + now(minutes) + ":" + now(seconds)
  } 
  
}