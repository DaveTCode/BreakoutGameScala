package tyler.breakout.levels

import tyler.breakout.{ImmutableVector2f, Level}
import tyler.breakout.config.Configuration
import util.Random

class Level1 extends Level {
  object Level1Constants {
    private val BALL_SPEED = 10.0
    private val TOP_PADDING = 30

    val INIT_BAT_POS = new ImmutableVector2f(Configuration.gameWidth / 2, Configuration.gameHeight - 5)
    val INIT_BAT_VEL = new ImmutableVector2f(0.0f, 0.0f)

    val INIT_BALL_POS = new ImmutableVector2f(Configuration.gameWidth / 2, Configuration.gameHeight / 2)
    val INIT_BALL_VEL = new ImmutableVector2f(-10.03f, -10f)//randBallVector

    val BLOCK_COLLECTION = createBlockCollection

    private def randBallVector: ImmutableVector2f = {
      val randAngle = (new Random()).nextDouble() * 2 * scala.math.Pi
      
      new ImmutableVector2f((scala.math.cos(randAngle) * BALL_SPEED).toFloat, 
                            (scala.math.sin(randAngle) * BALL_SPEED).toFloat)
    }
    
    private def createBlockCollection: Seq[RedBrick] = {
      val numBricksHorizontal = Configuration.gameWidth / Configuration.brickWidth
      val numBricksVertical = 4

      for (i <- 0 until numBricksHorizontal;
           j <- 0 until numBricksVertical)
        yield new RedBrick(i * Configuration.brickWidth,
                           TOP_PADDING + j * Configuration.brickHeight,
                           Configuration.brickWidth,
                           Configuration.brickHeight)
    }
  }

  override def blockCollection(): Seq[RedBrick] = {
    Level1Constants.BLOCK_COLLECTION
  }

  override def initialBatPosition(): ImmutableVector2f = Level1Constants.INIT_BAT_POS
  override def initialBatVelocity(): ImmutableVector2f = Level1Constants.INIT_BAT_VEL

  override def initialBallPosition(): ImmutableVector2f = Level1Constants.INIT_BALL_POS
  override def initialBallVelocity(): ImmutableVector2f = Level1Constants.INIT_BALL_VEL
}