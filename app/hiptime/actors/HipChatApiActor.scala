package hiptime.actors

import akka.actor.Actor
import hiptime.data.LiveChannels
import hiptime.data.JsonSerializer
import hiptime.models.User
import hiptime.actors.ActorName._

class HipChatApiActor extends Actor {

  def receive = {
    case _ => {
      LiveChannels.channels.map {
        case (key, chs) =>
          val jsonUsers = JsonSerializer.usersToJson(hiptime.utils.HipchatFetcher.users(key).toList)
          chs.map {
            ChannelMessage(_, jsonUsers)
          }
      }.flatten.foreach {
        context.actorSelection(path(channelActor)) ! _
      }
    }
  }

//  def users(apiKey: String): Seq[User] = ???
}
