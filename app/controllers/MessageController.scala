package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
// import play.api.Routes
import play.api.routing.JavaScriptReverseRouter
import controllers.SdmxMeta.getSdmxProvider
import controllers.SdmxMeta.getSdmxFlow

case class Message(value: String)

object MessageController extends Controller {

  implicit val fooWrites = Json.writes[Message]

  // def getMessage = Action {
  //   Ok(Json.toJson(Message("Hello from Scala")))
  // }

  def getMessageSdmxProvider = Action {
    Ok(Json.toJson(Message(getSdmxProvider().output)))
  }

  def getMessageSdmxFlow(provider: String) = Action {
    Ok(Json.toJson(Message(getSdmxFlow(provider, "").flow_id.mkString(", "))))
  }

  // def javascriptRoutes = Action { implicit request =>
  //   Ok(Routes.javascriptRouter("jsRoutes")(routes.javascript.MessageController.getMessage)).as(JAVASCRIPT)
  // }
  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      // controllers.routes.javascript.MessageController.getMessage,
      controllers.routes.javascript.MessageController.getMessageSdmxProvider,
      controllers.routes.javascript.MessageController.getMessageSdmxFlow
    )).as("text/javascript")
  }

}
