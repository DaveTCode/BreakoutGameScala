package tyler.breakout.rendering

import tyler.breakout.config.Configuration
import tyler.breakout.InGameGameState
import org.newdawn.slick.{UnicodeFont, Image, Color}
import org.newdawn.slick.font.effects.ColorEffect

object Renderer {
  private val backgroundImage = new Image(Configuration.backgroundImage)
  private val ballImage = new Image(Configuration.ballImageFile)
  private val batImage = new Image(Configuration.batImageFile)
  private val redBrickImage = new Image(Configuration.redBrickImageFile)
  private val scoreFont = new UnicodeFont(Configuration.scoreFontFile, 30, false, false)

  {
    scoreFont.addAsciiGlyphs()
    scoreFont.addGlyphs(400, 600)
    val effects = scoreFont.getEffects.asInstanceOf[java.util.List[AnyRef]]
    effects.add(new ColorEffect(java.awt.Color.WHITE))
    scoreFont.loadGlyphs()
  }
  
  def draw(t: Long, gameState: InGameGameState) {
    val ball = gameState.ballState(t)
    val bat = gameState.batState(t)
    
    backgroundImage.draw(0.0f, 0.0f)

    ballImage.draw(ball.pos.x, ball.pos.y)
    batImage.draw(bat.pos.x, bat.pos.y)
    
    gameState.allLiveBlocks(t).foreach(brick =>
      redBrickImage.draw(brick.x, brick.y, brick.width, brick.height)
    )

    drawLives(gameState.numLives(t))
  }

  def drawLives(numLives: Int) {
    scoreFont.drawString(Configuration.gameWidth - 20f, 10f, numLives.toString, new Color(255,255,255))
  }
}
