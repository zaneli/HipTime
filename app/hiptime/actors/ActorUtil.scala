package hiptime.actors

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import play.api.libs.concurrent.Akka
import play.api.Application
import akka.actor.Props
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue
import play.api.libs.concurrent.Execution.Implicits._

object ActorUtil {
  val init = Duration.create(0, TimeUnit.SECONDS)
  val interval = Duration.create(1, TimeUnit.SECONDS)

  def create(ch: Channel[JsValue], apiKey: String)(implicit app: Application): Unit = {
    val props = Props(classOf[ApiStatusActor], ch)
    val actorRef = Akka.system.actorOf(props, name = apiKey)
    Akka.system.scheduler.schedule(init, interval, actorRef, "ping")
  }
}
