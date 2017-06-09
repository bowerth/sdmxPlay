package controllers

import models.Message
import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.routing.JavaScriptReverseRouter
import controllers.SdmxMeta.getSdmxProvider
import controllers.SdmxMeta.getSdmxFlow
import controllers.SdmxMeta.getSdmxDimension
import controllers.SdmxMeta.getSdmxCode

import scala.collection.breakOut
import java.io.File
import scala.io.Source

import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.parse.rst.ReStructuredText
// import laika.render.HTML

// case class Message(value: String)

object MessageController extends Controller {

  implicit val fooWrites = Json.writes[Message]
  private def tuple2ToList[T](t: (T,T)): List[T] = List(t._1, t._2)
  private def tuple3ToList[T](t: (T,T,T)): List[T] = List(t._1, t._2, t._3)
  private def arrayToMap(array: Array[String], colnames: Array[String]) = {
      (colnames zip array).toMap
  }

  def getDownloadSdmx = Action {
    val file = new File("output.csv")
    Ok.sendFile(
      content = file,
      fileName = _ => file.getName
    )
  }

  def getMessageSdmxProvider = Action {
    Ok(Json.toJson(Message(getSdmxProvider().output)))
  }

  def getMessageSdmxHelp = Action {
    val mdFile = "public/docs/MessageSdmxHelp.md"
    val mdMessage = Source.fromFile(mdFile).getLines.mkString("\n")
    val htmlMessage = Transform.from(Markdown).to(laika.render.HTML).fromString(mdMessage).toString()
//    val rstFile = "public/docs/MessageSdmxHelp.rst"
//    val rstMessage = Source.fromFile(rstFile).getLines.mkString("\n")
//    val htmlMessage = Transform.from(ReStructuredText).to(laika.render.HTML).fromString(rstMessage).toString()
    Ok(Json.toJson(Message(htmlMessage)))
  }

  def getMessageSdmxFlow(provider: String, pattern: String) = Action {
    val flowid = getSdmxFlow(provider, pattern).flow_id
    val flowlabel = getSdmxFlow(provider, pattern).flow_label
    val combineTuple = flowid zip flowlabel
    val combine = for (c <- combineTuple) yield tuple2ToList(c).toArray
    val colnames = Array("id", "label")
    val arrayJson = for (s <- combine) yield toJson(arrayToMap(s, colnames))
    val jsontest = Json.toJson(Map("value" -> toJson(arrayJson)))
    Ok(jsontest)
  }

  def getMessageSdmxDimension(provider: String, flow: String) = Action {
    Ok(Json.toJson(Message(getSdmxDimension(provider, flow).dimension_id.mkString(" . "))))
  }

  def getMessageSdmxCode(provider: String, flow: String) = Action {
    
    def codecombine(dim: String) = {
      val codeid = getSdmxCode(provider, flow, dim).code_id
      val codelabel = getSdmxCode(provider, flow, dim).code_label
      val combineTuple = codeid zip codelabel
      val dimrep = List.fill(codeid.length)(dim)
      val prepended = dimrep.zip(combineTuple).map { case (i, (a, b)) => (i, a, b) }
      val combine = for (c <- prepended) yield tuple3ToList(c).toArray
      combine
    }

    val dimensions = getSdmxDimension(provider, flow).dimension_id
    val test = for (dim <- dimensions) yield codecombine(dim)
    val testflat = test.flatten
    val colnames = Array("dim", "id", "label")
    val arrayJson = for (s <- testflat) yield toJson(arrayToMap(s, colnames))
    val jsontest = Json.toJson(Map("value" -> toJson(arrayJson)))
    Ok(jsontest)
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      controllers.routes.javascript.MessageController.getMessageSdmxProvider,
      controllers.routes.javascript.MessageController.getMessageSdmxHelp,
      controllers.routes.javascript.MessageController.getMessageSdmxFlow,
      controllers.routes.javascript.MessageController.getMessageSdmxDimension,
      controllers.routes.javascript.MessageController.getMessageSdmxCode,
      controllers.routes.javascript.MessageController.getDownloadSdmx
    )).as("text/javascript")
  }

}
