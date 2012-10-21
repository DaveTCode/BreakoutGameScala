/**
 * User: David
 * Date: 04/02/12
 * Time: 12:29
 */
package tyler.breakout.input

import org.newdawn.slick.Input
import tyler.breakout.messaging.{BatVelocityChange, MessagePassing}
import tyler.breakout.{ImmutableVector2f, Application}

object InputHandler {

  def handleEvents(input: Input) {
    if (input.isKeyPressed(Input.KEY_LEFT)) {
      MessagePassing.send(new BatVelocityChange(Application.ticks, 
                                                new ImmutableVector2f(-50.0f, 0.0f)))
    }
    if (input.isKeyPressed(Input.KEY_RIGHT)) {
      MessagePassing.send(new BatVelocityChange(Application.ticks,
                                                new ImmutableVector2f(50.0f, 0.0f)))
    }
  }
}