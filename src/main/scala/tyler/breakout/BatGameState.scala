package tyler.breakout

import config.Configuration

class BatGameState(pos: ImmutableVector2f, vel: ImmutableVector2f) extends MovableGameState {
  override def left = position.x
  override def right = position.x + Configuration.batWidth
  override def top = position.y
  override def bottom = position.y + Configuration.batHeight
}