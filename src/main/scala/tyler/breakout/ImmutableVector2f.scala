package tyler.breakout

/**
 * Custom written immutable floating point vector since the Vector2f included
 * with Slick2d is mutable when using functions like scale.
 *
 * @param _x
 * @param _y
 */
class ImmutableVector2f(private val _x: Float, private val _y: Float) {
  
  def x = _x
  def y = _y
  
  def +(v: ImmutableVector2f): ImmutableVector2f = {
    new ImmutableVector2f(x + v.x, y + v.y)
  }

  def -(v: ImmutableVector2f): ImmutableVector2f = {
    new ImmutableVector2f(x - v.x, y - v.y)
  }

  def scale(factor: Float): ImmutableVector2f = {
    new ImmutableVector2f(x * factor, y * factor)
  }

  def length: Float = {
    x * x + y * y
  }
  
  def dot(v: ImmutableVector2f): Float = {
    x * v.x + y * v.y
  }
}
