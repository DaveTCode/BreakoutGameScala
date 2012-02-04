package tyler.breakout.messaging

/**
 * User: David
 * Date: 04/02/12
 * Time: 13:10
 */

abstract class Message

case class KeyUpMessage(key: Int) extends Message

case class KeyDownMessage(key: Int) extends Message