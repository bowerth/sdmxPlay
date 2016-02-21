package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
// import play.api.Routes
import play.api.routing.JavaScriptReverseRouter
import controllers.SdmxMeta.getSdmxProvider
import controllers.SdmxMeta.getSdmxFlow
import controllers.SdmxMeta.getSdmxDimension
import controllers.SdmxMeta.getSdmxCode

import scala.collection.breakOut

case class Message(value: String)

object MessageController extends Controller {

  implicit val fooWrites = Json.writes[Message]

  private def tuple2ToList[T](t: (T,T)): List[T] = List(t._1, t._2)
  private def tuple3ToList[T](t: (T,T,T)): List[T] = List(t._1, t._2, t._3)

  private def arrayToMap(array: Array[String], colnames: Array[String]) = {
    // val colnames = Array("id", "codes")
      (colnames zip array).toMap
  }

  def getMessageSdmxProvider = Action {
    Ok(Json.toJson(Message(getSdmxProvider().output)))
  }

  def getMessageSdmxFlow(provider: String, pattern: String) = Action {
    // Ok(Json.toJson(Message(getSdmxFlow(provider, pattern).flow_id.mkString(", "))))

    // val provider = "ECB"
    // val pattern = ""
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
    // getSdmxDimension("ECB", "EXR").dimension_id.mkString(" . ")
    Ok(Json.toJson(Message(getSdmxDimension(provider, flow).dimension_id.mkString(" . "))))
  }

  def getMessageSdmxCode(provider: String, flow: String) = Action {

    // val provider = "ECB"
    // val flow = "EXR"

    val dimensions = getSdmxDimension(provider, flow).dimension_id
    // val codes = for (dim <- dimensions) yield getSdmxCode(provider, flow, dim).code_id.mkString(" + ")

    // val dim = dimensions(0)
    def codecombine(dim: String) = {
      val codeid = getSdmxCode(provider, flow, dim).code_id
      val codelabel = getSdmxCode(provider, flow, dim).code_label
      val combineTuple = codeid zip codelabel
      val dimrep = List.fill(codeid.length)(dim)
      val prepended = dimrep.zip(combineTuple).map { case (i, (a, b)) => (i, a, b) }
      val combine = for (c <- prepended) yield tuple3ToList(c).toArray
      combine
    }

    val test = for (dim <- dimensions) yield codecombine(dim)
    val testflat = test.flatten

    // val freqcode = codecombine("FREQ")
    // val exrtypecode = codecombine(dimensions(3))
    // freqcode ::: exrtypecode

    val colnames = Array("dim", "id", "label")
    val arrayJson = for (s <- testflat) yield toJson(arrayToMap(s, colnames))
    val jsontest = Json.toJson(Map("value" -> toJson(arrayJson)))
    // println(jsontest)
    // codes
    // val codelabels = for (dim <- dimensions) yield getSdmxCode(provider, flow, dim).code_label.mkString(" + ")

    // val combineTuple = dimensions zip codes

    // val combineTuple = codes zip codelabels

    // val combine = for (c <- combineTuple) yield tuple2ToList(c).toArray
    // val colnames = Array("id", "codes")
    // val arrayJson = for (s <- combine) yield toJson(arrayToMap(s, colnames))
    // val jsontest = Json.toJson(Map("value" -> toJson(arrayJson)))

    // val jsontest = Json.toJson(
    //   Map(
    //     "value" -> Seq(
    //       toJson(Map(
    //         "id" -> toJson("FREQ"),
    //         "codes" -> toJson("D + B + A + W + S + Q + N + M + H + E + 2")
    //       )
    //       ),
    //       toJson(Map(
    //         "id" -> toJson("EXR_SUFFIX"),
    //         "codes" -> toJson("A + T + S + R + P + E")
    //       )
    //       )
    //     )
    //   )
    // )

    Ok(jsontest)

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
