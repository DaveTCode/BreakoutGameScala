package tyler.breakout

import org.newdawn.slick.AppGameContainer

object Application {
  
  val startTime = System.currentTimeMillis()

  def main(args: Array[String]) {

    val agc = new AppGameContainer(new Game)

    agc.setDisplayMode(800, 600, false)
    agc.start()
  }
  
  def ticks(): Long = {
    System.currentTimeMillis() - startTime
  }
}