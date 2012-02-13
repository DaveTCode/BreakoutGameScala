package tyler.breakout

import config.Configuration
import org.newdawn.slick.AppGameContainer
import java.util.logging.{Level, Logger}

object Application {
  
  val startTime = System.currentTimeMillis()

  def main(args: Array[String]) {
    Logger.global.setLevel(Level.WARNING)
    val agc = new AppGameContainer(new Game)

    agc.setTargetFrameRate(Configuration.framerate)
    agc.setDisplayMode(Configuration.gameWidth, Configuration.gameHeight, Configuration.fullscreen)
    agc.start()
  }
  
  def ticks: Long = {
    System.currentTimeMillis() - startTime
  }
}