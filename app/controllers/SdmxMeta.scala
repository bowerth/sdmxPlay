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
  //
  def getSdmxProvider(): models.SdmxProvider = {
    try {
      val res = SdmxClientHandler.getProviders()
      val output = res.toArray.mkString(", ")
      SdmxProvider(output)
    } catch {
      case _: Throwable => errorSdmxProvider
    }
  }
  // val providermodel = getSdmxProvider()
  // getSdmxProvider().output
  // providermodel.output

  val errorSdmxFlow = SdmxFlow(null, Array[String](""), Array[String](""))
  //
  def getSdmxFlow(provider: String, pattern: String): models.SdmxFlow = {
    if ( provider == null ) errorSdmxFlow
    else try {

      // val provider = "ECB"
      // val provider = "OECD"
      // val provider = "OECD_RESTR"
      // val pattern = ""
      // val pattern = "rail"
      // pattern
      // Array(pattern.toUpperCase)

      // val res = SdmxClientHandler.getFlows(provider, pattern)
      val res = SdmxClientHandler.getFlows(provider.toUpperCase, pattern)

      // res.get("YC")
      // for (key <- Array("YC", "STP")) yield res.get(key)
      // for ((k,v) <- res) printf("key: %s, value: %s\n", k, v)

      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]

      SdmxFlow(provider, ids, labels)

    } catch {
      case _: Throwable => errorSdmxFlow
    }
  }
  // getSdmxFlow("ECB", "").flow_id

  val errorSdmxDimension = SdmxDimension(null, null, Array[String](""))
  //
  def getSdmxDimension(provider: String, flow: String): models.SdmxDimension = {
    if ( provider == null || flow == null ) errorSdmxDimension
    else try {

      // val provider = "ECB"
      // val flow = "EXR"

      val res = SdmxClientHandler.getDimensions(provider, flow).toArray
      // for (l <- res.toArray) println(l)
      // val res0 = res(0)
      // res0.asInstanceOf[Dimension].getId
      val ids = for (obj <- res) yield obj.asInstanceOf[Dimension].getId

      SdmxDimension(provider, flow, ids)

    } catch {
      case _: Throwable => errorSdmxDimension
    }
  }

  val errorSdmxCode = SdmxCode(null, null, null, Array[String](""), Array[String](""))
  //
  def getSdmxCode(provider: String, flow: String, dimension: String): models.SdmxCode = {
    if ( provider == null || flow == null || dimension == null ) errorSdmxCode
    else try {

      // val provider = "ECB"
      // val flow = "EXR"
      // val dimension = "CURRENCY"

      val res = SdmxClientHandler.getCodes(provider, flow, dimension) //.toArray
      val ids = (for ((k,v) <- res) yield k).toArray[String]
      val labels = (for ((k,v) <- res) yield v).toArray[String]

      SdmxCode(provider, flow, dimension, ids, labels)

    } catch {
      case _: Throwable => errorSdmxCode
    }
  }

}
