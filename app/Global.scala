import akka.actor.Props
import hiptime.actors.{ ChannelActor, HipChatApiActor, PingActor }
import play.api.{ Application, GlobalSettings }
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import hiptime.actors.ActorName._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    val system = Akka.system(app)

    system.actorOf(Props(classOf[ChannelActor]), name = channelActor)

    system.actorOf(Props(classOf[HipChatApiActor]), name = hipChatApiActor)
    system.scheduler.schedule(0 seconds, 5 seconds) {
      system.actorSelection(path(hipChatApiActor)) ! "api"
    }

    system.actorOf(Props(classOf[PingActor]), name = pingActor)
    system.scheduler.schedule(0 seconds, 1 seconds) {
      system.actorSelection(path(pingActor)) ! "ping"
    }
  }
}
