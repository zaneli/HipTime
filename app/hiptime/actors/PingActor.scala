package hiptime.actors

import akka.actor.Actor
import hiptime.data.LiveChannels
import play.api.libs.json.Json
import hiptime.actors.ActorName._

class PingActor extends Actor {

  def receive = {
    case _ => {
      LiveChannels.channels.values.flatten.foreach { ch =>
        val msg = ChannelMessage(ch, Json.obj("type" -> "ping"))
        context.actorSelection(path(channelActor)) ! msg
      }
    }
  }
}
