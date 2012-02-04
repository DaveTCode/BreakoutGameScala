/**
 * User: David
 * Date: 04/02/12
 * Time: 12:29
 */
package tyler.breakout.input

import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import tyler.breakout.messaging.{KeyUpMessage, KeyDownMessage, MessagePassing}

object InputHandler {

  def init() {
    Mouse.create()
    Keyboard.create()

    Mouse.setGrabbed(true)
  }

  def dispose() {
    Mouse.setGrabbed(false)

    Mouse.destroy()
    Keyboard.destroy()
  }

  def pollForEvents() {
    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState) {
        MessagePassing.send(new KeyDownMessage(Keyboard.getEventKey))
      }
      else {
        MessagePassing.send(new KeyUpMessage(Keyboard.getEventKey))
      }
    }
  }
}