package tyler.breakout.rendering

import tyler.breakout.InGameGameState
import org.newdawn.slick.Image
import tyler.breakout.config.Configuration

object Renderer {
  val backgroundImage = new Image(Configuration.backgroundImage)
  val ballImage = new Image(Configuration.ballImageFile)
  val batImage = new Image(Configuration.batImageFile)
  
  def draw(t: Long, gameState: InGameGameState) {
    val ballPos = gameState.ballPosition(t)
    val batPos = gameState.batPosition(t)

    backgroundImage.draw(0.0f, 0.0f)

    ballImage.draw(ballPos.getX, ballPos.getY)
    batImage.draw(batPos.getX, batPos.getY)
  }
}
