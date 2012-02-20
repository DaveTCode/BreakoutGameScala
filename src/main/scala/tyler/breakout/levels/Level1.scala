package tyler.breakout.levels

import tyler.breakout.{ImmutableVector2f, Level}
import tyler.breakout.config.Configuration
import util.Random

class Level1 extends Level {
  object Level1Constants {
    private val BALL_SPEED = 2.0

    val INIT_BAT_POS = new ImmutableVector2f(Configuration.gameWidth / 2, Configuration.gameHeight - 5)
    val INIT_BAT_VEL = new ImmutableVector2f(0.0f, 0.0f)

    val INIT_BALL_POS = new ImmutableVector2f(Configuration.gameWidth / 2, Configuration.gameHeight / 2)
    val INIT_BALL_VEL = randBallVector

    private def randBallVector: ImmutableVector2f = {
      val randAngle = (new Random()).nextDouble() * 2 * scala.math.Pi
      
      new ImmutableVector2f((scala.math.cos(randAngle) * BALL_SPEED).toFloat, 
                            (scala.math.sin(randAngle) * BALL_SPEED).toFloat)
    }
  }

  def getContentsOfLocation(x: Int,  y: Int) {

  }

  def initialBatPosition(): ImmutableVector2f = Level1Constants.INIT_BAT_POS
  def initialBatVelocity(): ImmutableVector2f = Level1Constants.INIT_BAT_VEL

  def initialBallPosition(): ImmutableVector2f = Level1Constants.INIT_BALL_POS
  def initialBallVelocity(): ImmutableVector2f = Level1Constants.INIT_BALL_VEL
}