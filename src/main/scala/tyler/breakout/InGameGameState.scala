package tyler.breakout

import collisions.CollisionHandler
import config.Configuration
import input.InputHandler
import rendering.Renderer
import messaging._
import org.newdawn.slick.state.{StateBasedGame}
import org.newdawn.slick.{Graphics, GameContainer}
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.builder.{ImageBuilder, LayerBuilder, ScreenBuilder}
import collection.mutable.{ArrayBuffer}

class InGameGameState(stateId: Int, game: Game) extends NiftyOverlayBasicGameState with MessagingComponent {
  val INIT_BAT_POS = new ImmutableVector2f(Configuration.gameWidth / 2.0f,
                                           Configuration.gameHeight - 5.0f)
  val INIT_BAT_VEL = new ImmutableVector2f(0.0f, 0.0f)
  val INIT_BALL_POS = new ImmutableVector2f(Configuration.gameWidth / 2.0f,
                                            Configuration.gameHeight / 3.0f)
  val INIT_BALL_VEL = new ImmutableVector2f(0.0f, 0.0f)

  override def getID(): Int = stateId

  override def prepareNifty(nifty: Nifty, game: StateBasedGame) {
    val _ = new ScreenBuilder("game") {{
      layer(new LayerBuilder("background") {{
        childLayoutCenter()

        image(new ImageBuilder("background-image") {{
          filename("/src/main/resources/main-menu-bg.png")
          width("800px")
          height("600px")
        }});
      }})
    }}.build(nifty)
  }

  override def enterState(gameContainer: GameContainer, game: StateBasedGame) {

  }

  override def leaveState(gameContainer: GameContainer, game: StateBasedGame) {

  }

  override def initGameAndGUI(gameContainer: GameContainer,
                              game: StateBasedGame) {
    MessagePassing.register(this, (new BatVelocityChange(0, new ImmutableVector2f(0.0f, 0.0f))).name)
    MessagePassing.register(this, (new BallVelocityChange(0, new ImmutableVector2f(0.0f, 0.0f))).name)

    initNifty(gameContainer, game)

    MessagePassing.send(new BallVelocityChange(Application.ticks,
                                               new ImmutableVector2f(20.0f, -7.0f)))
    MessagePassing.send(new BatVelocityChange(Application.ticks,
                                              new ImmutableVector2f(0.0f, 0.0f)))
  }

  override protected def renderGame(gameContainer: GameContainer,
                                    game: StateBasedGame,
                                    graphics: Graphics) {
    Renderer.draw(Application.ticks, this)
  }

  override protected def updateGame(gameContainer: GameContainer,
                                    game: StateBasedGame,
                                    delta: Int) {
    InputHandler.handleEvents(gameContainer.getInput)
    CollisionHandler.checkCurrentCollisions(this, Application.ticks)
  }

  override def receive(message: Message) {
    message match {
      case message: BallVelocityChange => mEventBuffer += message
      case message: BatVelocityChange => mEventBuffer += message
    }
  }
}