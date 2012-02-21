package tyler.breakout

import levels.RedBrick

abstract class Level {

  def blockCollection(): Seq[RedBrick]

  def initialBatPosition(): ImmutableVector2f
  def initialBatVelocity(): ImmutableVector2f

  def initialBallPosition(): ImmutableVector2f
  def initialBallVelocity(): ImmutableVector2f
}

