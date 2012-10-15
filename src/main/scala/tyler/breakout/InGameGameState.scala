package tyler.breakout

import collisions.CollisionHandler
import input.InputHandler
import levels.RedBrick
import rendering.Renderer
import messaging._
import org.newdawn.slick.state.{StateBasedGame}
import org.newdawn.slick.{Graphics, GameContainer}
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.builder.{ImageBuilder, LayerBuilder, ScreenBuilder}
import collection.mutable.ArrayBuffer

class InGameGameState(stateId: Int, game: Game, level: Level) extends NiftyOverlayBasicGameState with MessagingComponent {

  private val mLevelInstance = new LevelInstance(level)
  private val mEventBuffer = new ArrayBuffer[Message]

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
    MessagePassing.register(this, (new BrickHitEvent(0, new RedBrick(0,0,0,0))).name)

    initNifty(gameContainer, game)
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

  def ballState(t: Long) = mLevelInstance.ballState(t, mEventBuffer)
  def batState(t: Long) = mLevelInstance.batState(t, mEventBuffer)

  def allLiveBlocks(t: Long) = mLevelInstance.liveBlocks(t, mEventBuffer)
}