package hiptime.actors

import akka.actor.Actor
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import akka.event.Logging

class ApiStatusActor(ch: Channel[JsValue]) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "ping" => ch.push(Json.obj("type" -> "ping"))
    case other => log.info(s"Unknown message: $other")
  }
}
