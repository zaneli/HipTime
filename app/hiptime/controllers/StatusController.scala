package hiptime.controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.WebSocket
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}
import scala.concurrent.ExecutionContext


object StatusController extends Controller {
  import ExecutionContext.Implicits.global

  def index = Action {
    Ok(views.html.index())
  }

  def status = WebSocket.using[String] { request =>


    val response = Json.obj(
      "type" -> "users",
      "value" -> Json.arr(
        Json.obj("name" -> "hoge", "email" -> "hoge@example.com", "active" -> true),
        Json.obj("name" -> "fuga", "email" -> "fuga@example.com", "active" -> true),
        Json.obj("name" -> "piyo", "email" -> "piyo@example.com", "active" -> false)
      )
    )

    val (out, ch) = Concurrent.broadcast[String]

    val in = Iteratee.foreach[String] {
      msg =>
        ch.push(response.toString)
    }

    (in, out)
  }
}
