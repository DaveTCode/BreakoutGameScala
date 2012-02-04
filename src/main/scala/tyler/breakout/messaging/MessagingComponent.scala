package tyler.breakout.messaging

trait MessagingComponent {
  def receive(x: Message): Unit
}