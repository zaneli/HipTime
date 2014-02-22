package hiptime.data

import play.api.libs.iteratee.Concurrent.Channel
import scala.collection.mutable
import play.api.libs.json.JsValue

object LiveChannels {

  private[this] val chs: mutable.Map[String, Set[Channel[JsValue]]] = mutable.Map()

  def channels: Map[String, Set[Channel[JsValue]]] =
    this.synchronized(chs.toMap)

  def addChannel(apiKey: String, ch: Channel[JsValue]) {
    this.synchronized {
      val old = chs.getOrElse(apiKey, Set())
      chs.update(apiKey, old + ch)
    }
  }
}
