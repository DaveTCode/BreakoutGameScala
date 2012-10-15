package tyler.breakout

abstract class MovableGameState(pos: ImmutableVector2f, vel: ImmutableVector2f) {
  abstract def left: Float
  abstract def right: Float
  abstract def top: Float
  abstract def bottom: Float

  def projLeft(t:Long) = left + t * vel.x
  def projRight(t:Long) = right + t * vel.x
  def projTop(t:Long) = top + t * vel.y
  def projBottom(t:Long) = bottom + t * vel.y
}