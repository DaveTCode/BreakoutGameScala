package tyler.breakout.messaging

import collection.mutable.{ArrayBuffer, HashMap}

/**
 * Message passing subsystem. Allows for decoupling all components and routing
 * all messages through this system instead.
 *
 * General strategy is that there is a map of message types -> components and
 * each new message that comes in is routed to each of the components in turn.
 *
 * Both the send and register methods are designed to be threadsafe although
 * no guarantees are made about the messages themselves.
 */
object MessagePassing {
  private val registeredComponents = new HashMap[String, ArrayBuffer[MessagingComponent]]()

  /**
   * Register a component to receive messages about a given message type.
   *
   * @param component
   * @param messageTypeName
   */
  def register(component : MessagingComponent, messageTypeName : String) {
    println("Registering component " + component.toString + " for message type " + messageTypeName)
    val componentList = registeredComponents.getOrElseUpdate(messageTypeName, new ArrayBuffer[MessagingComponent]())

    componentList += component
    println(componentList)

    println(registeredComponents)
  }

  /**
   * Send a new message. This will get routed to 0 or more components.
   *
   * @param message
   */
  def send(message: Message) {
    val componentList = registeredComponents.getOrElse(message.name, new ArrayBuffer[MessagingComponent]())
    println("Received message -> " + message.toString)

    componentList.foreach((component: MessagingComponent) => {
      println(component.toString + " -> " + message.toString)
      component.receive(message)
    })
  }
}