package hiptime.controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json


object StatusController extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def status = Action {
    val response = Json.obj(
      "type" -> "users",
      "value" -> Json.arr(
        Json.obj("name" -> "hoge", "email" -> "hoge@example.com", "active" -> true),
        Json.obj("name" -> "fuga", "email" -> "fuga@example.com", "active" -> true),
        Json.obj("name" -> "piyo", "email" -> "piyo@example.com", "active" -> false)
      )
    )
    Ok(response)
  }
}
