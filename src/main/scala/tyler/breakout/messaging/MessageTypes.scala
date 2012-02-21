package tyler.breakout.messaging

import tyler.breakout.ImmutableVector2f
import tyler.breakout.levels.RedBrick

abstract class Message(t: Long, n: String) {
  def ticks: Long = t
  def name: String = n
}
case class KeyUpMessage(t: Long, key: Int) extends Message(t, "KEY_UP")
case class KeyDownMessage(t: Long, key: Int) extends Message(t, "KEY_DOWN")
case class NewGameMessage(t: Long) extends Message(t, "NEW_GAME")
case class BallVelocityChange(t: Long, vel: ImmutableVector2f) extends Message(t, "BALL_VELOCITY_CHANGE")
case class BatVelocityChange(t: Long,  vel: ImmutableVector2f) extends Message(t, "BAT_VELOCITY_CHANGE")
case class BrickHitEvent(t: Long, brick: RedBrick) extends Message(t, "BRICK_HIT")