package tyler.breakout.collisions

import tyler.breakout.messaging.{BallVelocityChange, BatVelocityChange, MessagePassing}
import tyler.breakout.config.Configuration
import tyler.breakout.{ImmutableVector2f, InGameGameState}

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

  private def checkBallBatCollision(gameState: InGameGameState, t: Long) {
    val ballPos = gameState.ballPosition(t)
    val batPos = gameState.batPosition(t)
  }
}