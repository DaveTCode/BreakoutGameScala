package tyler.breakout.renderer

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11._

object GlGraphicsDevice {

  val WINDOW_TITLE = "Breakout";
  
  def init(fullscreen : Boolean, width : Int, height : Int) {
    try {
      initLwjgl(width, height, fullscreen)
      initGl(width, height)
    } catch {
      case e : LWJGLException => System.exit(1);
    }
  }
  
  private def initLwjgl(width : Int, height : Int, fullscreen : Boolean) {
    Display.setTitle(WINDOW_TITLE)
    Display.setFullscreen(fullscreen)
    Display.setDisplayMode(new DisplayMode(width, height))
    Display.create()
  }
  
  private def initGl(width : Int, height : Int) {
    glEnable(GL_TEXTURE_2D)
    glDisable(GL_DEPTH_TEST)
    
    /*
     * Set up the projection matrix for 2D drawing.
     */
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glOrtho(0, width, height, 0, -1, 1)
    
    /*
     * Set up the model view matrix. 
     */
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    glViewport(0, 0, width, height)
  }
}