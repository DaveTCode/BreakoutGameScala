package tyler.breakout

abstract class Level {
  
  def getContentsOfLocation(x: Int,  y: Int)

  def initialBatPosition(): ImmutableVector2f
  def initialBatVelocity(): ImmutableVector2f

  def initialBallPosition(): ImmutableVector2f
  def initialBallVelocity(): ImmutableVector2f
}

