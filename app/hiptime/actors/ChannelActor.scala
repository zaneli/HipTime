package hiptime.actors

import akka.actor.Actor
import akka.event.Logging
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue

class ChannelActor extends Actor {
  private[this] val log = Logging(context.system, this)

  def receive = {
    case ChannelMessage(ch, msg) => ch.push(msg)
    case other => log.warning(s"Unknown message: $other")
  }
}

case class ChannelMessage(ch: Channel[JsValue], msg: JsValue)
