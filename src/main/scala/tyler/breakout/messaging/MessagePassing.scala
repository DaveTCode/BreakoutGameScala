package tyler.breakout.messaging

import collection.mutable.{LinkedList, HashMap}

object MessagePassing {
  private val registeredComponents = new HashMap[Message, LinkedList[MessagingComponent]]()

  def register(component : MessagingComponent, message : Message) {
    val componentList = registeredComponents.getOrElseUpdate(message, new LinkedList[MessagingComponent]())

    componentList:+component
  }

  def send(message: Message) {
    val componentList = registeredComponents.getOrElse(message, new LinkedList[MessagingComponent]())

    componentList.foreach((component: MessagingComponent) => {
      println(component.toString() + " -> " + message.toString())
      component.receive(message)
    })
  }
}