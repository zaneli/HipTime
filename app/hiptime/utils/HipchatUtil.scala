package hiptime.utils

import hiptime.models.User
import scala.collection.mutable.Map
import java.util.Date
import com.zaneli.escalade.hipchat
import org.joda.time.DateTime
import com.zaneli.escalade.hipchat.model.UserIdentifier

class HipchatFetcher(apiKey: String) {
  import org.scala_tools.time.Imports.DateTime._

  // TODO lastFetchedAtを適切に処理する
  var lastFetchedAt: Option[DateTime] = None
  val roomsApi = new hipchat.Rooms(apiKey)
  val usersApi = new hipchat.Users(apiKey)

  def lastFetchedYMD: Option[(Int, Int, Int)] = lastFetchedAt.map((dt) => (dt.getYear, dt.getMonthOfYear, dt.getDayOfMonth))

  def roomIds: Seq[Int] = {
    val (res, _) = roomsApi.list.call
    res.map(_.roomId)
  }

  def user(userId: Int, active: Boolean) = {
    val (u, _) = usersApi.show.call(userId)
    User(u.name, u.email, active)
  }

  def users: Seq[User] = {
    val messages = roomIds.flatMap(roomsApi.history.call(_, lastFetchedYMD)._1).sortBy(_.date.getMillis)
    val userMap = Map[Int, User]()

//    println(messages)
    messages.map {
      (m) =>
        m.from match {
          case UserIdentifier(Right(userId), name) =>
            val msg = m.message
            println(userId, name, msg)
            val activeOpt = if (msg == "in") {
              Some(true)
            } else if (msg == "out") {
              Some(false)
            } else {
              None
            }
            activeOpt.foreach {
              (active) =>
                // TODO Intの範囲外のときどうする？
                val userIdInt = userId.toInt

                userMap += userIdInt -> user(userIdInt, active)
                // TODO 既にuserMapに入ってる時はusersApiを呼ばない
            }
        }
    }
    userMap.result.values.toSeq
  }
}

object HipchatFetcher {
  val fetchers: Map[String, HipchatFetcher] = Map()

  def users(apiKey: String): Seq[User] = {
    val fetcher = fetchers.getOrElseUpdate(apiKey, new HipchatFetcher(apiKey))
    println(fetchers)
    fetcher.users
  }
}
