package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
// import play.api.Routes
import play.api.routing.JavaScriptReverseRouter
import controllers.SdmxMeta.getSdmxProvider
import controllers.SdmxMeta.getSdmxFlow
import controllers.SdmxMeta.getSdmxDimension
import controllers.SdmxMeta.getSdmxCode

case class Message(value: String)

object MessageController extends Controller {

  implicit val fooWrites = Json.writes[Message]

  def getMessageSdmxProvider = Action {
    Ok(Json.toJson(Message(getSdmxProvider().output)))
  }

  def getMessageSdmxFlow(provider: String, pattern: String) = Action {
    Ok(Json.toJson(Message(getSdmxFlow(provider, pattern).flow_id.mkString(", "))))
  }

  def getMessageSdmxDimension(provider: String, flow: String) = Action {
    // getSdmxDimension("ECB", "EXR").dimension_id.mkString(" . ")
    Ok(Json.toJson(Message(getSdmxDimension(provider, flow).dimension_id.mkString(" . "))))
  }

  def getMessageSdmxCode(provider: String, flow: String) = Action {
    // val dimensions = getSdmxDimension("ECB", "EXR").dimension_id
    val dimensions = getSdmxDimension(provider, flow).dimension_id
    val codes = for (dim <- dimensions) yield dim + ": " + getSdmxCode(provider, flow, dim).code_id.mkString(" + ")
    Ok(Json.toJson(Message(codes.mkString("____"))))
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      controllers.routes.javascript.MessageController.getMessageSdmxProvider,
      controllers.routes.javascript.MessageController.getMessageSdmxFlow,
      controllers.routes.javascript.MessageController.getMessageSdmxDimension,
      controllers.routes.javascript.MessageController.getMessageSdmxCode
    )).as("text/javascript")
  }

}
