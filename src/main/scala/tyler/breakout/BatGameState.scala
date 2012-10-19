package tyler.breakout

import config.Configuration

class BatGameState(pos: ImmutableVector2f, vel: ImmutableVector2f) extends MovableGameState(pos, vel) {
  override def left = pos.x
  override def right = pos.x + Configuration.batWidth
  override def top = pos.y
  override def bottom = pos.y + Configuration.batHeight
}