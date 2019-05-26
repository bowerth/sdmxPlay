package controllers

// 2.6.x
import scala.concurrent.ExecutionContext
import play.api.http.{FileMimeTypesConfiguration, DefaultFileMimeTypesProvider}
// https://www.playframework.com/documentation/2.6.x/ScalaJavascriptRouting

import javax.inject.Inject
import play.api.mvc._
// import play.api.routing._

import models.Message
// import play.api.mvc.{Action, BaseController}
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
// import laika.ast.{Comment, QuotedBlock}
import laika.format.{HTML, Markdown}

// https://aknay.github.io/2018/01/04/javascript-routing-with-play-framework.html
// object MessageController extends Controller {
// @Singleton
class MessageController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
// class MessageController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
// class MessageController @Inject() () extends InjectedController {

  implicit val ec = ExecutionContext.global
  implicit val fileMimeType = new DefaultFileMimeTypesProvider(FileMimeTypesConfiguration(Map("csv" -> "text/csv"))).get

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
    Ok(Json.toJson(Message(getSdmxProvider.output)))
  }

  def getMessageSdmxHelp = Action {
    val mdFile = "public/docs/MessageSdmxHelp.md"
    val mdMessage = Source.fromFile(mdFile).getLines.mkString("\n")
    val htmlMessage = Transform.from(Markdown).to(laika.format.HTML).fromString(mdMessage).toString()
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

  // def javascriptRoutes = Action { implicit request =>
  //   Ok(JavaScriptReverseRouter("jsRoutes")(
  //     routes.javascript.MessageController.getMessageSdmxProvider,
  //     routes.javascript.MessageController.getMessageSdmxHelp,
  //     routes.javascript.MessageController.getMessageSdmxFlow,
  //     routes.javascript.MessageController.getMessageSdmxDimension,
  //     routes.javascript.MessageController.getMessageSdmxCode,
  //     routes.javascript.MessageController.getDownloadSdmx
  //   )).as("text/javascript")
  // }

}
