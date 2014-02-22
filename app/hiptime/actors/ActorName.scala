package hiptime.actors

object ActorName {

  val channelActor = "ChannelActor"
  val hipChatApiActor = "HipChatApiActor"
  val pingActor = "PingActor"
  def path(name: String) = s"/user/${name}"
}