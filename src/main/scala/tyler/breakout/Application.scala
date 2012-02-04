package tyler.breakout

object Application {

  def main(args: Array[String]) {
    loadSubsystems()
    
    Thread.sleep(10000)
    
    System.exit(0)
  }
  
  def loadSubsystems() {
    renderer.GlGraphicsDevice.init(false, 640, 480)
    input.InputHandler.init()
  }
}