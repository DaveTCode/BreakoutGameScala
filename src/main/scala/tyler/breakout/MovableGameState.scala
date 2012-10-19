package tyler.breakout

abstract class MovableGameState(val pos: ImmutableVector2f, val vel: ImmutableVector2f) {
  def left: Float
  def right: Float
  def top: Float
  def bottom: Float

  def projLeft(t:Long) = left + t * vel.x
  def projRight(t:Long) = right + t * vel.x
  def projTop(t:Long) = top + t * vel.y
  def projBottom(t:Long) = bottom + t * vel.y
}