package tyler.breakout.config

object Configuration {
  def gameWidth: Int = 800
  def gameHeight: Int = 600
  def fullscreen: Boolean = false
  def framerate: Int = 60
  
  def batWidth: Int = 20
  def batHeight: Int = 3

  def ballRadius: Int = 3

  def backgroundImage = "/src/main/resources/bg-game.png"
  def ballImageFile = "/src/main/resources/ball.png"
  def batImageFile = "/src/main/resources/bat.png"
  def redBrickImageFile = "/src/main/resources/red-brick.png"
}
