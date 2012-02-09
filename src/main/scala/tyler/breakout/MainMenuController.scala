package tyler.breakout

import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.{Screen, ScreenController}

class MainMenuController extends ScreenController {

  
  override def bind(nifty: Nifty, screen: Screen) {
    
  }
  
  override def onStartScreen() {
  }
  
  override def onEndScreen() {
  }
  
  def newGame() {
    println("New Game clicked")

    Game.
  }
  
  def exit() {
    println("Exit clicked")

    System.exit(0)
  }
}

