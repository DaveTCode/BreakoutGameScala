package tyler.breakout

import collection.mutable.ArrayBuffer
import messaging.{BallVelocityChange, BatVelocityChange, Message}

class LevelInstance(level: Level) {

  /**
   * All velocity events for the bat in order from oldest to youngest
   *
   * @param t - Only events before this point taken into account
   * @return
   */
  private def batVelocityEvents(t: Long, eventBuffer: Seq[Message]): Seq[BatVelocityChange] = {
    eventBuffer.filter(message => message.ticks < t) collect {
      case message: BatVelocityChange => message
    }
  }

  /**
   * All velocity events for the ball in order from oldest to youngest
   *
   * @param t - Only events before this point taken into account
   * @return
   */
  private def ballVelocityEvents(t: Long, eventBuffer: Seq[Message]): Seq[BallVelocityChange] = {
    eventBuffer.filter(message => message.ticks < t) collect {
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
  def batVelocity(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    batVelocityEvents(t, eventBuffer).last.vel
  }

  /**
   * The latest position of the bat. Used to draw the bat and determine
   * collisions.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return
   */
  def batPosition(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    def recurCalcPosition(velocityChanges: List[BatVelocityChange],
                          currentPos: ImmutableVector2f,
                          currentVel: ImmutableVector2f,
                          currentTime: Long): ImmutableVector2f = {
      velocityChanges match {
        case Nil => {
          val deltaT = (t - currentTime) / 1000.0f
          currentPos + currentVel.scale(deltaT)
        }
        case head :: tail => {
          val deltaT = (head.t - currentTime) / 1000.0f
          val newPos = currentPos + currentVel.scale(deltaT)

          recurCalcPosition(tail, newPos, head.vel, head.t)
        }
      }
    }

    recurCalcPosition(batVelocityEvents(t, eventBuffer).toList,
                      level.initialBatPosition(),
                      level.initialBatVelocity(),
                      0)
  }

  /**
   * Calculate the current velocity of the ball.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   *
   * @return
   */
  def ballVelocity(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    ballVelocityEvents(t, eventBuffer).last.vel
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
  def ballPosition(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    def recurCalcPosition(velocityChanges: List[BallVelocityChange],
                          currentPos: ImmutableVector2f,
                          currentVel: ImmutableVector2f,
                          currentTime: Long): ImmutableVector2f = {
      velocityChanges match {
        case Nil => {
          val deltaT = (t - currentTime) / 1000.0f
          currentPos + currentVel.scale(deltaT)
        }
        case head :: tail => {
          val deltaT = (head.t - currentTime) / 1000.0f
          val newPos = currentPos + currentVel.scale(deltaT)

          recurCalcPosition(tail, newPos, head.vel, head.t)
        }
      }
    }

    recurCalcPosition(ballVelocityEvents(t, eventBuffer).toList,
                      level.initialBallPosition(),
                      level.initialBallVelocity(),
                      0)
  }
}

