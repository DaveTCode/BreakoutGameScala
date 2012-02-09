package tyler.breakout

import messaging._
import org.newdawn.slick.state.{StateBasedGame, BasicGameState}
import org.newdawn.slick.{Graphics, GameContainer}

class InGameGameState(stateId: Int) extends BasicGameState with MessagingComponent {

  override def getID(): Int = stateId

  override def init(gc: GameContainer, sbg: StateBasedGame) {

  }

  override def render(gc: GameContainer, sbg: StateBasedGame, graphics: Graphics) {

  }

  override def update(gc: GameContainer, sbg: StateBasedGame, delta: Int) {

  }

  override def receive(message: Message) {

  }
}