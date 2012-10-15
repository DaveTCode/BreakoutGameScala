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
    val bat = gameState.ballState(t)

    if (bat.left < 0.0f || bat.right > Configuration.gameWidth) {
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
    val ball = gameState.ballState(t)
    
    if (ball.left < 0.0f || ball.right > Configuration.gameWidth) {
      val newVel = new ImmutableVector2f(-1.0f * ball.vel.x, ball.vel.y)
      
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
    val ball = gameState.ballState(t)
    val bat = gameState.batState(t)

    if (ball.bottom > bat.top && ball.right > bat.left && ball.left < bat.right) {
      val newVel = new ImmutableVector2f(ball.vel.x, -1 * ball.vel.y)

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
    val ball = gameState.ballState(t)

    def checkSingleCollision(brick: RedBrick) {
      val brickTop = brick.y
      val brickLeft = brick.x
      val brickRight = brickLeft + brick.width
      val brickBottom = brickTop + brick.height


      if (brickLeft < ball.left && brickRight > ball.right) {

      }
    }

    gameState.allLiveBlocks(t).foreach(checkSingleCollision(_))
  }
}
