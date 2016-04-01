package controllers

import play.api.mvc._
import models.SdmxProvider
import models.SdmxFlow
import models.SdmxDimension
import models.SdmxCode
import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.Dimension
import scala.collection.JavaConversions.mapAsScalaMap

object SdmxMeta extends Controller {

  val errorSdmxProvider = SdmxProvider("")
  def getSdmxProvider(): SdmxProvider = {
    try {
      val res = SdmxClientHandler.getProviders()
      val output = res.toArray.mkString(", ")
      SdmxProvider(output)
    } catch {
      case _: Throwable => errorSdmxProvider
    }
  }

  val errorSdmxFlow = SdmxFlow(null, Array[String](""), Array[String](""))
  def getSdmxFlow(provider: String, pattern: String): SdmxFlow = {
    if ( provider == null ) errorSdmxFlow
    else try {
      val res = SdmxClientHandler.getFlows(provider.toUpperCase, pattern)
      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]
      SdmxFlow(provider, ids, labels)
    } catch {
      case _: Throwable => errorSdmxFlow
    }
  }

  val errorSdmxDimension = SdmxDimension(null, null, Array[String](""))
  def getSdmxDimension(provider: String, flow: String): SdmxDimension = {
    if ( provider == null || flow == null ) errorSdmxDimension
    else try {
      val res = SdmxClientHandler.getDimensions(provider, flow).toArray
      val ids = for (obj <- res) yield obj.asInstanceOf[Dimension].getId
      SdmxDimension(provider, flow, ids)
    } catch {
      case _: Throwable => errorSdmxDimension
    }
  }

  val errorSdmxCode = SdmxCode(null, null, null, Array[String](""), Array[String](""))
  def getSdmxCode(provider: String, flow: String, dimension: String): SdmxCode = {
    if ( provider == null || flow == null || dimension == null ) errorSdmxCode
    else try {
      val res = SdmxClientHandler.getCodes(provider, flow, dimension) //.toArray
      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]
      SdmxCode(provider, flow, dimension, ids, labels)
    } catch {
      case _: Throwable => errorSdmxCode
    }
  }

}
