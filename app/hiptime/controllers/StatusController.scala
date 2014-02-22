package hiptime.controllers

import play.api.mvc.{ Action, Controller }
import play.api.libs.json.{ JsObject, Json }
import play.api.mvc.WebSocket
import play.api.libs.iteratee.{ Concurrent, Enumerator, Iteratee }
import scala.concurrent.ExecutionContext
import play.api.libs.json.JsValue
import play.api.libs.json.JsString
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
import hiptime.data.LiveChannels
import play.api.libs.iteratee.Concurrent.Channel

object StatusController extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def status = WebSocket.using[JsValue] { request =>
    val (out, ch) = Concurrent.broadcast[JsValue]

    //    val response = Json.obj(
    //      "type" -> "users",
    //      "value" -> Json.arr(
    //        Json.obj("name" -> "hoge", "email" -> "hoge@example.com", "active" -> true),
    //        Json.obj("name" -> "fuga", "email" -> "fuga@example.com", "active" -> true),
    //        Json.obj("name" -> "piyo", "email" -> "piyo@example.com", "active" -> false)))
    //    val (out, ch) = Concurrent.broadcast[JsValue]
    //
    var apiKey: Option[String] = None
    val in = Iteratee.foreach[JsValue] { msg =>
      (msg \ "type").as[String] match {
        case "api_key" => {
          val value = msg \ "value"
          apiKey = Option(value.as[String])
          apiKey.foreach (LiveChannels.addChannel(_, ch))
        }
        case "pong" => {
          apiKey.foreach (LiveChannels.addChannel(_, ch))
        }
      }
    }
    (in, out)
  }
}
