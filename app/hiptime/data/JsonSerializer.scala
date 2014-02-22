package hiptime.data

import hiptime.models.User
import play.api.libs.json.JsObject
import play.api.libs.json.Json

object JsonSerializer {

  def usersToJson(users: Seq[User]): JsObject = {
    val values = users.map { user =>
      Json.obj("name" -> user.name, "email" -> user.email, "active" -> user.active)
    }
    Json.obj(
      "type" -> "users",
      "value" -> Json.arr(values))
  }
}
