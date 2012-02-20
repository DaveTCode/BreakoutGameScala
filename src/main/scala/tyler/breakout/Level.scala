package tyler.breakout

abstract class Level {

  /**
   * Get the initial contents of the location x,y. Implementation decides how
   * to determine the contents.
   *
   * @param x
   * @param y
   */
  def getContentsOfLocation(x: Int,  y: Int)

  def initialBatPosition(): ImmutableVector2f
  def initialBatVelocity(): ImmutableVector2f

  def initialBallPosition(): ImmutableVector2f
  def initialBallVelocity(): ImmutableVector2f
}

