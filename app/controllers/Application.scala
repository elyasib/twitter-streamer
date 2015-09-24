package controllers

import play.api._
import play.api.mvc._
import play.api.libs.oauth.{ConsumerKey, RequestToken, OAuthCalculator}
import play.api.Play.current
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws._
import play.api.libs.iteratee._

import play.api.libs.json._
import play.extras.iteratees._

import actors.TwitterStreamer

class Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Tweets"))
  }

  def tweets = WebSocket.acceptWithActor[String, JsValue] {
    request => out => TwitterStreamer.props(out)
  }


  //def tweets = Action.async {

  //  val (iteratee, enumerator) = Concurrent.joined[Array[Byte]]

  //  val jsonStream : Enumerator[JsValue] = 
  //    enumerator &>
  //    Encoding.decode() &>
  //    Enumeratee.grouped(JsonIteratees.jsSimpleObject)

  //  //val loggingIteratee : Iteratee[JsObject,Unit] = Iteratee.foreach[JsObject] { value => 
  //  val loggingIteratee = Iteratee.foreach[JsValue] { value => 
  //    Logger.info(value.toString)
  //  }

  //  credentials.map { case (consumerKey, requestToken) => 
  //    jsonStream run[Unit] loggingIteratee
  //    WS
  //      .url("https://stream.twitter.com/1.1/statuses/filter.json")
  //      .withRequestTimeout(0)
  //      .sign(OAuthCalculator(consumerKey, requestToken))
  //      .withQueryString("track" -> "Scala")
  //      .get { response =>
  //        Logger.info("Status: " + response.status)
  //        iteratee
  //      }.map { _ => 
  //        Ok("Stream closed")
  //      }
  //  } getOrElse {
  //    Future {
  //      InternalServerError("Twitter credentials missing")
  //    }
  //  }
  //}

  def credentials : Option[(ConsumerKey, RequestToken)] = for {
    apiKey <- Play.configuration.getString("twitter.apiKey")
    apiSecret <- Play.configuration.getString("twitter.apiSecret")
    token <- Play.configuration.getString("twitter.token")
    tokenSecret <- Play.configuration.getString("twitter.tokenSecret")
  } yield (
    ConsumerKey(apiKey, apiSecret),
    RequestToken(token, tokenSecret)
  )
}
