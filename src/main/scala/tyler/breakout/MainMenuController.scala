package tyler.breakout

import de.lessvoid.nifty.screen.{Screen, ScreenController}
import de.lessvoid.nifty.{EndNotify, Nifty}
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame

class MainMenuController(game: Game) extends ScreenController {
  var mNifty: Nifty = null
  var mScreen: Screen = null

  override def bind(nifty: Nifty, screen: Screen) {
    mNifty = nifty;
    mScreen = screen;
  }
  
  override def onStartScreen() {

  }
  
  override def onEndScreen() {
  }
  
  def newGame() {
    println("New Game clicked")

    game.startGame()
  }
  
  def exit() {
    mScreen.endScreen(new EndNotify() {
      def perform() { System.exit(0) }
    })
  }
}

