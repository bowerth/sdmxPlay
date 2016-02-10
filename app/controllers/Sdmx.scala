package controllers

import models.SdmxData
import play.api.mvc._
import scala.io.Source

import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
import java.io._

object Sdmx extends Controller {

  // private val errorSdmxData = SdmxData(null, null, null, null, "NA")
  private val errorSdmxData = SdmxData(null, null, null, null)

  private def getSdmxData(provider: String, query: String, start: String, end: String) = {
    if ( provider == null ) errorSdmxData
    else try {

      val res = SdmxClientHandler.getTimeSeries(provider, query, start, end)
      //
      val res2 = res.toArray.
        map(_.asInstanceOf[PortableTimeSeries])
      //
      val nameArray = for (ts <- res2) yield getName(ts)
      val headerArray = "TIME_PERIOD" +: nameArray
      //
      val years0 = getYears(res2(0))
      val valueArray = for (series <- res2) yield getValues(series)
      val dataArray = years0 +: valueArray
      //
      val l = makeTable(data = dataArray)
      //
      val output = headerArray.mkString(",") + "\n" + l
      //
      val writer = new PrintWriter(new File("public/data/sdmx/tsdata.csv"))
      writer.write(output)
      writer.close()

      // SdmxData(provider, query, start, end, output)
      // SdmxData(provider, query, start, end, "NA")
      SdmxData(provider, query, start, end)
    } catch {
      case _: Throwable => errorSdmxData
    }
  }

  private def validateSdmx(provider: String, query: String, start: String, end: String): (String,String,String,String) = {
    if ( start.matches("\\d{4}") && end.matches("\\d{4}") ) (provider.toUpperCase, query, start, end)
    else (null,null,null,null)
  }

  private def getName (tts: PortableTimeSeries): String =
    tts.getName

  private def getYears(tts: PortableTimeSeries): Array[String] =
    tts.getTimeSlotsArray.map(_.toString)

  private def getValues(tts: PortableTimeSeries): Array[String] =
    tts.getObservationsArray.map(_.toString)

  private def makeRowSeq(row: Int, data: Array[Array[String]]) =
    for (col <- 0 to data.length-1) yield {
      data(col)(row)
    }

  private def makeRow(row: Int, data: Array[Array[String]]) =
    makeRowSeq(row, data).mkString(",")

  private def makeTable(data: Array[Array[String]]) = {
    val tableSeq =
      for (row <- 0 to data(0).length-1)
      yield makeRow(row, data)
    tableSeq.mkString("\n")
  }

  def index = Action { implicit request =>
    // val queryECB = "EXR.A.USD+NZD.EUR.SP00.A"
    val queryECB = "EXR.M.USD+GBP+CAD+AUD.EUR.SP00.A"
    Redirect(routes.Sdmx.main("ECB", queryECB, "1999", "2015"))
  }

  def redirect(provider: String, query: String, start: String, end: String) = Action { implicit request =>
    Redirect(routes.Sdmx.main(provider, query, start, end))
  }

  def main(provider: String, query: String, start: String, end: String) = Action {
    val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)

    // val res = SdmxClientHandler.getTimeSeries(prov, qy, st, ed)
    // //
    // val res2 = res.toArray.
    // map(_.asInstanceOf[PortableTimeSeries])
    // //
    // val nameArray = for (ts <- res2) yield getName(ts)
    // val headerArray = "TIME_PERIOD" +: nameArray
    // //
    // val years0 = getYears(res2(0))
    // val valueArray = for (series <- res2) yield getValues(series)
    // val dataArray = years0 +: valueArray
    // //
    // val l = makeTable(data = dataArray)
    // //
    // val output = headerArray.mkString(",") + "\n" + l
    // //
    // val writer = new PrintWriter(new File("public/data/sdmx/tsdata.csv"))
    // writer.write(output)
    // writer.close()

    val sd = getSdmxData(prov, qy, st, ed)
    val sdView =
      if ( sd.provider == null )
        // SdmxData(provider.toUpperCase, query, start, end, "NA")
        SdmxData(provider.toUpperCase,query,start,end)
      else sd
    Ok(views.html.sdmx(sdView))
  }

}

