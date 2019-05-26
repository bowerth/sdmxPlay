package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

// class JavascriptRouter @Inject()(controllerComponents: KnolxControllerComponents) extends KnolxAbstractController(controllerComponents) {
class JavascriptRouter @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def jsRoutes: Action[AnyContent] = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      // controllers.routes.javascript.FeedbackFormsController.createFeedbackForm,
      // controllers.routes.javascript.RecommendationController.scheduleSession
      controllers.routes.javascript.MessageController.getMessageSdmxProvider,
      controllers.routes.javascript.MessageController.getMessageSdmxHelp,
      controllers.routes.javascript.MessageController.getMessageSdmxFlow,
      controllers.routes.javascript.MessageController.getMessageSdmxDimension,
      controllers.routes.javascript.MessageController.getMessageSdmxCode,
      controllers.routes.javascript.MessageController.getDownloadSdmx
    )).as("text/javascript")
  }

}
