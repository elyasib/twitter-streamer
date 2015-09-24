package actors

import akka.actor.{Actor, ActorRef, Props}
import play.api.Logger
import play.api.libs.json.Json

class TwitterStreamer(out: ActorRef) extends Actor {

  def receive = {
    case "subscribe" => 
      Logger.info("Received subscription from client")
      out ! Json.obj("text" -> "Hello, World")

  } 
}

object TwitterStreamer {
  
  def props(out: ActorRef) = Props(new TwitterStreamer(out))

}
