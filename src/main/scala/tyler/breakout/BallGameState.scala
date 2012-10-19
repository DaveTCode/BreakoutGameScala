package tyler.breakout

import config.Configuration

class BallGameState(pos: ImmutableVector2f, vel: ImmutableVector2f) extends MovableGameState(pos, vel) {
  override def left = pos.x - Configuration.ballRadius
  override def right = pos.x + Configuration.ballRadius
  override def top = pos.y - Configuration.ballRadius
  override def bottom = pos.y + Configuration.ballRadius
}