package tyler.breakout

object Application {
  
  val startTime = System.currentTimeMillis()

  def main(args: Array[String]) {
    loadSubsystems()
    
    Thread.sleep(10000)
    
    System.exit(0)
  }
  
  def ticks(): Long = {
    System.currentTimeMillis() - startTime
  }
  
  def loadSubsystems() {
    renderer.GlGraphicsDevice.init(false, 640, 480)
    input.InputHandler.init()
  }
}