package tyler.breakout.rendering

import tyler.breakout.InGameGameState
import org.newdawn.slick.Image
import tyler.breakout.config.Configuration

object Renderer {
  val backgroundImage = new Image(Configuration.backgroundImage)
  val ballImage = new Image(Configuration.ballImageFile)
  val batImage = new Image(Configuration.batImageFile)
  val redBrickImage = new Image(Configuration.redBrickImageFile)
  
  def draw(t: Long, gameState: InGameGameState) {
    val ball = gameState.ballState(t)
    val bat = gameState.batState(t)
    
    backgroundImage.draw(0.0f, 0.0f)

    ballImage.draw(ball.pos.x, ball.pos.y)
    batImage.draw(bat.pos.x, bat.pos.y)
    
    gameState.allLiveBlocks(t).foreach(brick =>
      redBrickImage.draw(brick.x, brick.y, brick.width, brick.height)
    )
  }
}
