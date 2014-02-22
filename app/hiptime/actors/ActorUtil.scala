package hiptime.actors

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import play.api.libs.concurrent.Akka
import play.api.Application
import akka.actor.Props
import akka.actor.ActorRef
//import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue
import play.api.libs.concurrent.Execution.Implicits._

object ActorUtil {
  val init = Duration.create(0, TimeUnit.SECONDS)
  val interval = Duration.create(1, TimeUnit.SECONDS)

  def create(app: Application, ch: Channel[JsValue], apiKey: String) = {
    val props = Props().withCreator(new ApiStatusActor(ch))
    val ref: ActorRef = Akka.system(app).actorOf(props, name = apiKey)
    Akka.system(app).scheduler.schedule(init, interval, ref, "ping")
  }
}
