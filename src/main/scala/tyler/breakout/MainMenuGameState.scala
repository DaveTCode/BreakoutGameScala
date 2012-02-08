package tyler.breakout

import collection.mutable.ListBuffer
import messaging.{NewGameMessage, MessagePassing, KeyDownMessage, Message}
import org.lwjgl.input.Keyboard.{KEY_UP, KEY_DOWN, KEY_RETURN}

/**
 * Game state when the game is in the main menu. Registers for key press events and
 * keeps them in a buffer so that it can always calculate which menu item is
 * selected at any given time.
 *
 * This was my vague attempt at making a very simple one layer menu in a
 * functional deterministic style. I think the process could be easily expanded
 * to multi level menus. Don't intend to find out yet.
 */
class MainMenuGameState extends GameState {

  private val initialMenuItemIndex: Int = 0
  val keyPressBuffer: ListBuffer[KeyPress] = new ListBuffer[KeyPress];
  val mainMenu: Array[MenuItem] = Array(new NewGameMenuItem(), new ExitMenuItem())
  
  override def receive(message: Message) {
    message match {
      case KeyDownMessage(key: Int) => {
        key match {
          case KEY_UP => keyPressBuffer += new KeyUpPress
          case KEY_DOWN => keyPressBuffer += new KeyDownPress
          case KEY_RETURN => whichMenuItem(keyPressBuffer, Application.ticks()).execute()
        }
      }
    }
  }

  private def whichMenuItem(buffer: ListBuffer[KeyPress], ticks: Long): MenuItem = {
    val relevantKeyPresses: ListBuffer[KeyPress] = buffer.filter(x => x.ticks < ticks)

    val keyUpCount = relevantKeyPresses.collect({ case k: KeyUpPress => k }).length
    val keyDownCount = relevantKeyPresses.collect({ case k: KeyDownPress => k }).length

    mainMenu((initialMenuItemIndex + keyUpCount - keyDownCount) % 1)
  }
  
  abstract class KeyPress(val ticks: Long)
  case class KeyUpPress() extends KeyPress(Application.ticks())
  case class KeyDownPress() extends KeyPress(Application.ticks())
  
  abstract class MenuItem(val text: String) {
    def execute()
  }
  case class ExitMenuItem() extends MenuItem("Exit") {
    def execute() {
      System.exit(0)
    }
  }
  case class NewGameMenuItem() extends MenuItem("New Game") {
    def execute() {
      MessagePassing.send(new NewGameMessage())
    }
  }
}