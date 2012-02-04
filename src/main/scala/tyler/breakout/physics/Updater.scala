package tyler.breakout.physics

import org.lwjgl.util.vector.Vector2f

object Updater {
  
  def updateVector(v : Vector2f, dv : Vector2f, t : Float) = {
    new Vector2f(v.getX + dv.getX * t, v.getY + dv.getY * t)
  }
}