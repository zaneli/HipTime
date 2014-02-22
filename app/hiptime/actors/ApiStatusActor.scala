package hiptime.actors

import akka.actor.Props
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import akka.actor.Actor
import play.api.libs.concurrent.Akka
import play.api.Application
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Iteratee
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.libs.json.JsValue

class ApiStatusActor(ch: Channel[JsValue]) extends Actor {

  def receive = {
    case "ping" => {
      val (_, ch) = Concurrent.broadcast[String]

      val in = Iteratee.foreach[String] {
        msg => println("!!!!!" + msg); ch.push(Json.obj("type" -> "ping").toString)
      }
    }
    case _ => println("xxx") //
  }
}
