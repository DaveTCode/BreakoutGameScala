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
import org.newdawn.slick.geom.Vector2f
import collection.mutable.{ArrayBuffer}

class InGameGameState(stateId: Int, game: Game) extends NiftyOverlayBasicGameState with MessagingComponent {
  val INIT_BAT_POS = new Vector2f(Configuration.gameWidth / 2.0f,
                                  Configuration.gameHeight - 5.0f)
  val INIT_BAT_VEL = new Vector2f(0.0f, 0.0f)
  val INIT_BALL_POS = new Vector2f(Configuration.gameWidth / 2.0f,
                                   Configuration.gameHeight / 3.0f)
  val INIT_BALL_VEL = new Vector2f(0.0f, 0.0f)

  override def getID(): Int = stateId
  
  private val mEventBuffer = new ArrayBuffer[Message]

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
    MessagePassing.register(this, (new BatVelocityChange(0, new Vector2f())).name)
    MessagePassing.register(this, (new BallVelocityChange(0, new Vector2f())).name)

    initNifty(gameContainer, game)

    MessagePassing.send(new BallVelocityChange(Application.ticks,
                                               new Vector2f(20.0f, -7.0f)))
    MessagePassing.send(new BatVelocityChange(Application.ticks,
                                              new Vector2f(0.0f, 0.0f)))
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

  private def batVelocityEvents(t: Long): Seq[BatVelocityChange] = {
    mEventBuffer.filter(message => message.ticks < t) collect {
      case message: BatVelocityChange => message
    }
  }
  
  private def ballVelocityEvents(t: Long): Seq[BallVelocityChange] = {
    mEventBuffer.filter(message => message.ticks < t) collect {
      case message: BallVelocityChange => message
    }
  }

  /**
   * The latest velocity of the bat. Used to draw animations.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return
   */
  def batVelocity(t: Long): Vector2f = {
    batVelocityEvents(t).last.vel
  }

  /**
   * The latest position of the bat. Used to draw the bat and determine
   * collisions.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return
   */
  def batPosition(t: Long): Vector2f = {
    def recurCalcPosition(velocityChanges: List[BatVelocityChange],
                          currentPos: Vector2f,
                          currentVel: Vector2f,
                          currentTime: Long): Vector2f = {
      velocityChanges match {
        case Nil => {
          val deltaT = (t - currentTime) / 1000.0f
          currentPos.add(currentVel.scale(deltaT))
        }
        case head :: tail => {
          val deltaT = (head.t - currentTime) / 1000.0f
          val newPos = currentPos.add(currentVel.scale(deltaT))
          
          recurCalcPosition(tail, newPos, head.vel, head.t)
        } 
      }
    }

    recurCalcPosition(batVelocityEvents(t).toList, INIT_BAT_POS, INIT_BAT_VEL, 0)
  }

  /**
   * Calculate the current velocity of the ball.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return
   */
  def ballVelocity(t: Long): Vector2f = {
    ballVelocityEvents(t).last.vel
  }

  /**
   * Calculate the current ball position. Used for collision handling and for
   * drawing the ball.
   *
   * Note: Not common code with bat position yet because extracting this into a
   * library is for future.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return Vector2f representing the ball position.
   */
  def ballPosition(t: Long): Vector2f = {
    def recurCalcPosition(velocityChanges: List[BallVelocityChange],
                          currentPos: Vector2f,
                          currentVel: Vector2f,
                          currentTime: Long): Vector2f = {
      velocityChanges match {
        case Nil => {
          val deltaT = (t - currentTime) / 1000.0f
          currentPos.add(currentVel.scale(deltaT))
        }
        case head :: tail => {
          val deltaT = (head.t - currentTime) / 1000.0f
          val newPos = currentPos.add(currentVel.scale(deltaT))

          recurCalcPosition(tail, newPos, head.vel, head.t)
        }
      }
    }

    recurCalcPosition(ballVelocityEvents(t).toList, INIT_BALL_POS, INIT_BALL_VEL, 0)
  }
}