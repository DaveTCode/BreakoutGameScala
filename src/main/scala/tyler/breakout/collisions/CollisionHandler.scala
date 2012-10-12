package tyler.breakout.collisions

import tyler.breakout.messaging.{BallVelocityChange, BatVelocityChange, MessagePassing}
import tyler.breakout.config.Configuration
import tyler.breakout.{ImmutableVector2f, InGameGameState}
import tyler.breakout.levels.RedBrick

/**
 * Game specific collision detection. Only entrance point is through the common
 * checkCurrentCollisions function which then detects (and acts on) all
 * in game collisions.
 *
 * This is called once per update loop.
 */
object CollisionHandler {
  def checkCurrentCollisions(gameState: InGameGameState, t: Long) {
    checkBatWallCollision(gameState, t)
    checkBallWallCollision(gameState, t)
    checkBallBatCollision(gameState, t)
    checkBallBlockCollisions(gameState, t)
  }

  /**
   * If the bat hits the wall then it just sets velocity to 0.
   *
   * @param gameState - Used to get at the bat position.
   * @param t - Number of ticks
   */
  private def checkBatWallCollision(gameState: InGameGameState, t: Long) {
    val batPos = gameState.batPosition(t)
    val batVel = gameState.batVelocity(t)
    val batLeft = batPos.x - (Configuration.batWidth / 2.0f)
    val batRight = batPos.x + (Configuration.batWidth / 2.0f)

    if (batLeft < 0.0f || batRight > Configuration.gameWidth) {
      MessagePassing.send(new BatVelocityChange(t, new ImmutableVector2f(0.0f, 0.0f)))
    }
  }

  /**
   * If the ball hits the wall then it's velocity is reflected in the vertical
   * plane (i.e. (xV, yV) -> (-xV, yV)
   *
   * @param gameState - Used to get at the ball position.
   * @param t - Number of ticks
   */
  private def checkBallWallCollision(gameState: InGameGameState, t: Long) {
    val ballPos = gameState.ballPosition(t)
    val ballVel = gameState.ballVelocity(t)
    val ballTop = ballPos.y - Configuration.ballRadius
    val ballLeft = ballPos.x - Configuration.ballRadius
    val ballRight = ballPos.x + Configuration.ballRadius
    
    if (ballLeft < 0.0f || ballRight > Configuration.gameWidth) {
      val newVel = new ImmutableVector2f(-1.0f * ballVel.x, ballVel.y)
      
      MessagePassing.send(new BallVelocityChange(t, newVel))
    }
    
    if (ballTop < 0.0f) {
      val newVel = new ImmutableVector2f(ballVel.x, -1 * ballVel.y)

      MessagePassing.send(new BallVelocityChange(t, newVel))
    }
  }

  /**
   * Check and handle ball <-> bat collision. Essentially just reflect the ball
   * along the y value if it hits the bat at all.
   *
   * @param gameState
   * @param t
   */
  private def checkBallBatCollision(gameState: InGameGameState, t: Long) {
    val ballPos = gameState.ballPosition(t)
    val ballVel = gameState.ballVelocity(t)
    val batPos = gameState.batPosition(t)
    val ballBottom = ballPos.y + Configuration.ballRadius
    val ballLeft = ballPos.x - Configuration.ballRadius
    val ballRight = ballPos.x + Configuration.ballRadius
    val batTop = batPos.y
    val batLeft = batPos.x
    val batRight = batPos.x + Configuration.batWidth

    if (ballBottom > batTop && ballRight > batLeft && ballLeft < batRight) {
      val newVel = new ImmutableVector2f(ballVel.x, -1 * ballVel.y)

      MessagePassing.send(new BallVelocityChange(t, newVel))
    }
  }

  /**
   * Check and handle any collisions between the ball and bricks. Reflect the ball
   *
   *
   * @param gameState
   * @param t
   */
  private def checkBallBrickCollisions(gameState: InGameGameState, t: Long) {
    val ballPos = gameState.ballPosition(t)
    val ballVel = gameState.ballVelocity(t)
    val ballBottom = ballPos.y + Configuration.ballRadius
    val ballTop = ballPos.y + Configuration.ballRadius
    val ballLeft = ballPos.x - Configuration.ballRadius
    val ballRight = ballPos.x + Configuration.ballRadius

    def checkSingleCollision(brick: RedBrick) {
      val brickTop = brick.y
      val brickLeft = brick.x
      val brickRight = brickLeft + brick.width
      val brickBottom = brickTop + brick.height


      if (brickLeft < ballLeft && brickRight > ballRight) {

      }
    }

    gameState.allLiveBlocks(t).foreach(checkSingleCollision(_))
  }
}
