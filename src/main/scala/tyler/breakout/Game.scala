package tyler.breakout

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer

class Game extends StateBasedGame("Breakout - Scala") {

  val MAIN_MENU_STATE_ID = 0
  val IN_GAME_STATE_ID   = 1

  override def initStatesList(gameContainer: GameContainer) {
    addState(new MainMenuGameState(MAIN_MENU_STATE_ID, this))
    addState(new InGameGameState(IN_GAME_STATE_ID, this))
  }

  def startGame() {
    enterState(IN_GAME_STATE_ID)
  }
}