package controllers

import javax.inject.Inject
import play.api.mvc._
import models.SdmxProvider
import models.SdmxFlow
import models.SdmxDimension
import models.SdmxCode
import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.Dimension
//import scala.collection.JavaConversions.mapAsScalaMap
//import scala.collection.JavaConverters.mapAsScalaMap
import scala.collection.JavaConverters._

//object SdmxMeta extends Controller {
//@Singleton
// class SdmxMeta @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
object SdmxMeta {

  // val errorSdmxProvider = SdmxProvider("")
  def getSdmxProvider: SdmxProvider = {
    try {
      val res = SdmxClientHandler.getProviders().asScala
//      val output = res.toArray.mkString(", ")
      val output = (for ((k,v) <- res) yield k).toArray.mkString(", ")
      SdmxProvider(output)
    } catch {
      case _: Throwable => SdmxProvider("") // errorSdmxProvider
    }
  }

  def getSdmxFlow(provider: String, pattern: String): SdmxFlow = {
    val errorSdmxFlow = SdmxFlow(null, Array[String](""), Array[String](""))
    if ( provider == null ) errorSdmxFlow
    else try {
      // val res = SdmxClientHandler.getFlows("ECB", "").asScala
      val res = SdmxClientHandler.getFlows(provider.toUpperCase, pattern).asScala
      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]
      SdmxFlow(provider, ids, labels)
    } catch {
      case _: Throwable => errorSdmxFlow
    }
  }

  def getSdmxDimension(provider: String, flow: String): SdmxDimension = {
    val errorSdmxDimension = SdmxDimension(null, null, Array[String](""))
    if ( provider == null || flow == null ) errorSdmxDimension
    else try {
      // val res = SdmxClientHandler.getDimensions("ECB", "IMF").toArray
      val res = SdmxClientHandler.getDimensions(provider, flow).toArray
      val ids = for (obj <- res) yield obj.asInstanceOf[Dimension].getId
      SdmxDimension(provider, flow, ids)
    } catch {
      case _: Throwable => errorSdmxDimension
    }
  }

  // val errorSdmxCode = SdmxCode(null, null, null, Array[String](""), Array[String](""))
  def getSdmxCode(provider: String, flow: String, dimension: String): SdmxCode = {
    val errorSdmxCode = SdmxCode(null, null, null, Array[String](""), Array[String](""))
    if ( provider == null || flow == null || dimension == null ) errorSdmxCode
    else try {
      val res = SdmxClientHandler.getCodes(provider, flow, dimension).asScala //.toArray
      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]
      SdmxCode(provider, flow, dimension, ids, labels)
    } catch {
      case _: Throwable => errorSdmxCode
    }
  }

}
