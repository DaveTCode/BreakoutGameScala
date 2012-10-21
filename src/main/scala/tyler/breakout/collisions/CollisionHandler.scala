package tyler.breakout.collisions

import tyler.breakout.messaging._
import tyler.breakout.config.Configuration
import tyler.breakout.{ImmutableVector2f, InGameGameState}
import tyler.breakout.levels.RedBrick
import tyler.breakout.messaging.BallVelocityChange
import tyler.breakout.messaging.BrickHitEvent
import tyler.breakout.messaging.BatVelocityChange
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
    checkBallFloorCollision(gameState, t)
    checkBallBatCollision(gameState, t)
    checkBallBrickCollisions(gameState, t)
  }

  /**
   * If the bat hits the wall then it just sets velocity to 0.
   *
   * @param gameState - Used to get at the bat position.
   * @param t - Number of ticks
   */
  private def checkBatWallCollision(gameState: InGameGameState, t: Long) {
    val bat = gameState.batState(t)

    if (bat.left < 0.0f || bat.right > Configuration.gameWidth) {
      val newVel = new ImmutableVector2f(-1.0f * bat.vel.x, bat.vel.y)

      MessagePassing.send(new BatVelocityChange(t, newVel))
    }
  }

  /**
   * If the ball hits the wall then it's velocity is reflected in the vertical
   * plane (i.e. (xV, yV) -> (-xV, yV))
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
    
    if (ball.top < 0.0f) {
      val newVel = new ImmutableVector2f(ball.vel.x, -1 * ball.vel.y)

      MessagePassing.send(new BallVelocityChange(t, newVel))
    }
  }

  private def checkBallFloorCollision(gameState: InGameGameState, t:Long) {
    val ball = gameState.ballState(t)

    if (ball.bottom > Configuration.gameHeight) {
      val newVel = new ImmutableVector2f(ball.vel.x, -1 * ball.vel.y)

      MessagePassing.send(new LifeLost((t)))
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

    if (circleLineCollision(ball.pos, Configuration.ballRadius,
                            new ImmutableVector2f(bat.left, bat.top),
                            new ImmutableVector2f(bat.right, bat.top))) {
      val newVel = new ImmutableVector2f(ball.vel.x, -1 * ball.vel.y)

      MessagePassing.send(new BallVelocityChange(t, newVel))
    }
  }

  /**
   * Check and handle any collisions between the ball and bricks. Reflect the ball
   * on whichever line it hit the brick.
   *
   * Also destroys the bricks.
   *
   * @param gameState
   * @param t
   */
  private def checkBallBrickCollisions(gameState: InGameGameState, t: Long) {
    val ball = gameState.ballState(t)

    def checkSingleCollision(brick: RedBrick) {
      val brickTopLeft = new ImmutableVector2f(brick.x, brick.y)
      val brickTopRight = new ImmutableVector2f(brick.x + brick.width, brick.y)
      val brickBottomLeft = new ImmutableVector2f(brick.x, brick.y + brick.height)
      val brickBottomRight = new ImmutableVector2f(brick.x + brick.width, brick.y + brick.height)

      val edges = List((brickTopLeft, brickTopRight, (1, -1)),
                       (brickBottomLeft, brickBottomRight, (1, -1)),
                       (brickTopLeft, brickBottomLeft, (-1, 1)),
                       (brickTopRight, brickBottomRight, (-1, 1)))
      edges.foreach{ case (lineStart, lineEnd, (x, y)) => {
        if (circleLineCollision(ball.pos, Configuration.ballRadius, lineStart, lineEnd)) {
          MessagePassing.send(new BrickHitEvent(t, brick))

          val newVel = new ImmutableVector2f(if ((ball.vel.x > 0 && x < 0) ||
                                                 (ball.vel.x < 0 && x > 0)) -ball.vel.x else ball.vel.x,
                                             if ((ball.vel.y > 0 && y < 0) ||
                                                 (ball.vel.y < 0 && y > 0)) -ball.vel.y else ball.vel.y)
          MessagePassing.send(new BallVelocityChange(t, newVel))
        }
      }}
    }

    gameState.allLiveBlocks(t).foreach(checkSingleCollision(_))
  }

  /**
   * Utility function to test whether a circle intersects a line.
   *
   * @param circlePos
   * @param circleRad
   * @param lineStart
   * @param lineEnd
   * @return
   */
  private def circleLineCollision(circlePos: ImmutableVector2f, circleRad: Float,
                                  lineStart: ImmutableVector2f, lineEnd: ImmutableVector2f): Boolean = {
    val d = lineEnd - lineStart
    val f = lineStart - circlePos
    val a = d dot d
    val b = 2f * (f dot d)
    val c = (f dot f) - circleRad * circleRad
    val discriminant = b * b - 4f * a * c

    if (discriminant >= 0) {
      val root_disc = math.sqrt(discriminant)
      val t1 = (-1f * b + root_disc) / (2f * a)
      val t2 = (-1f * b - root_disc) / (2f * a)

      (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)
    } else {
      false
    }
  }
}
