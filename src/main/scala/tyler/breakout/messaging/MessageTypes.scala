package tyler.breakout.messaging

abstract class Message
case class KeyUpMessage(key: Int) extends Message
case class KeyDownMessage(key: Int) extends Message
case class NewGameMessage() extends Message