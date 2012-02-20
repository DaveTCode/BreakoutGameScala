abstract class LevelObject(x: Int,  y: Int)

case class BlankSpace(x: Int,  y: Int, width: Int, height:Int) extends LevelObject(x, y)
case class RedBrick(x: Int, y: Int, width: Int, height: Int) extends LevelObject(x, y)

