package tyler.breakout

import levels.RedBrick
import messaging.{BrickHitEvent, BallVelocityChange, BatVelocityChange, Message}
import annotation.tailrec

class LevelInstance(level: Level) {

  /**
   * All velocity events for the bat in order from oldest to youngest
   *
   * @param t - Only events before this point taken into account
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  private def batVelocityEvents(t: Long, eventBuffer: Seq[Message]): Seq[BatVelocityChange] =
    eventBuffer.filter(_.ticks < t) collect {
      case message: BatVelocityChange => message
    }

  /**
   * All velocity events for the ball in order from oldest to youngest
   *
   * @param t - Only events before this point taken into account
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  private def ballVelocityEvents(t: Long, eventBuffer: Seq[Message]): Seq[BallVelocityChange] =
    eventBuffer.filter(_.ticks < t) collect {
      case message: BallVelocityChange => message
    }

  /**
   * All events which indicate that a brick has been hit.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  private def brickHitEvents(t: Long, eventBuffer: Seq[Message]): Seq[BrickHitEvent] =
    eventBuffer.filter(_.ticks < t) collect  {
      case message: BrickHitEvent => message
    }
  
  /**
   * The latest velocity of the bat. Used to draw animations.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  def batVelocity(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f =
    batVelocityEvents(t, eventBuffer) match {
      case Nil => level.initialBatVelocity()
      case lst => lst.last.vel
    }

  /**
   * The latest position of the bat. Used to draw the bat and determine
   * collisions.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  def batPosition(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    @tailrec def recurCalcPosition(velocityChanges: List[BatVelocityChange],
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
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   *
   * @return
   */
  def ballVelocity(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f =
    ballVelocityEvents(t, eventBuffer) match {
      case Nil => level.initialBallVelocity()
      case lst => lst.last.vel
    }

  /**
   * Calculate the current ball position. Used for collision handling and for
   * drawing the ball.
   *
   * Note: Not common code with bat position yet because extracting this into a
   * library is for future.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   *
   * @return Vector2f representing the ball position.
   */
  def ballPosition(t: Long, eventBuffer: Seq[Message]): ImmutableVector2f = {
    @tailrec def recurCalcPosition(velocityChanges: List[BallVelocityChange],
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

  /**
   * Provides access to the ball state at a given time t. Access to velocity,
   * position, future position.
   *
   * @param t
   * @param eventBuffer
   * @return
   */
  def ballState(t: Long, eventBuffer: Seq[Message]): BallGameState =
    new BallGameState(ballPosition(t, eventBuffer), ballVelocity(t, eventBuffer))

  /**
   * Provides access to the bat state at a given time t. Access to velocity,
   * position, future position.
   *
   * @param t
   * @param eventBuffer
   * @return
   */
  def batState(t: Long, eventBuffer: Seq[Message]): BatGameState =
    new BatGameState(batPosition(t, eventBuffer), batVelocity(t, eventBuffer))

  /**
   * All live blocks at time t.
   *
   * @param t - Game time (as retrieved by Application.ticks)
   * @param eventBuffer - All messages for this level (can include messages beyond t).
   * @return
   */
  def liveBlocks(t: Long, eventBuffer: Seq[Message]): Seq[RedBrick] = {
    val initBlockCollection = level.blockCollection()
    val brickHitCollection = brickHitEvents(t, eventBuffer)

    initBlockCollection.filter(
      brick => !brickHitCollection.exists(message => message.brick == brick))
  }
}

