package tyler.breakout.collisions

import tyler.breakout.ImmutableVector2f

object CollisionUtils {

  /**
   * Check if a point exists within an axis aligned rectangle.
   * 
   * @param p - (x, y) tuple 
   * @param AABB (topLeft.x, topLeft.y), (bottomRight.x, bottomRight.y)
   * @return - True if the point is within the rectangle. False if on the lines or outside rectangle.
   */
  def pointInAABB(p: (Float, Float), AABB: ((Float, Float), (Float, Float))): Boolean = {
    (p._1 > AABB._1._1 && p._1 < AABB._2._1) && (p._2 > AABB._1._2 && p._2 < AABB._2._2)
  }

  /**
   * Used to determine whether a line intersects a circle in 2D. The line need not axis
   * aligned.
   *
   * @param circle ((x,y), radius)
   * @param line ((x1, y1), (x2, y2))
   * @return True if the circle touches the line. False otherwise.
   */
  def circleLineIntersectionTest(circle: ((Float,  Float), Float),
                                 line: ((Float,  Float), (Float, Float))): Boolean = {
    val dirOfLine = new ImmutableVector2f(line._2._1 - line._1._1, line._2._2 - line._1._2)
    val circleToStart = new ImmutableVector2f(line._1._1 - circle._1._1, line._1._2 - circle._1._2)

    val a = dirOfLine dot dirOfLine
    val b = circleToStart.scale(2.0f) dot dirOfLine
    val c = (circleToStart dot circleToStart) - (circle._2 * circle._2)
    val discriminant = b * b - 4 * a * c
    
    if (discriminant >= 0) {
      val rootDiscriminant = scala.math.sqrt(discriminant)
      val t1 = (-1.0f * b + rootDiscriminant) / (2.0f * a)
      val t2 = (-1.0f * b - rootDiscriminant) / (2.0f * a)
      
      if (t1 >= 0 && t1 <= 1) return true 
      if (t2 >= 0 && t2 <= 1) return true
    }

    false
  }
}
