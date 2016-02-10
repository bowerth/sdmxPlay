package controllers

// import java.util._
import models.SdmxData
import play.api.mvc._
import scala.io.Source

import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
import java.io._

object Sdmx extends Controller {

  // private val R = org.ddahl.rscala.callback.RClient()
  // private val file = "R/sdmx.R"

  // private val lines = Source.fromFile(file).getLines().toList
  // R.synchronized{ R eval lines.mkString("\n") }

  // val errorSdmxData = SdmxData(null,null,null,null,"NA","NA")
  private val errorSdmxData = SdmxData(null,null,null,null)

  private def getSdmxData(provider: String, query: String, start: String, end: String) = {
    if ( provider == null ) errorSdmxData
    else try {
      // val s1 = R.synchronized{R.evalS1(s"unlist(cache('$provider', '$query', '$start', '$end'))")}
      // SdmxData(provider, query, start, end, s1(1), s1(2))
      SdmxData(provider, query, start, end)
    } catch {
      case _: Throwable => errorSdmxData
    }
  }

  private def validateSdmx(provider: String, query: String, start: String, end: String): (String,String,String,String) = {
    if ( start.matches("\\d{4}") && end.matches("\\d{4}") ) (provider.toUpperCase, query, start, end)
    else (null,null,null,null)
  }

  def index = Action { implicit request =>
    val queryECB = "EXR.A.USD+NZD.EUR.SP00.A"
    Redirect(routes.Sdmx.main("ECB",queryECB,"2000","2005"))
  }

  def redirect(provider: String, query: String, start: String, end: String) = Action { implicit request =>
    Redirect(routes.Sdmx.main(provider,query,start,end))
  }

  def main(provider: String, query: String, start: String, end: String) = Action {
    val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)

    // val res = SdmxClientHandler.getTimeSeries(provider, query, start, end)
    val res = SdmxClientHandler.getTimeSeries(prov, qy, st, ed)
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

    val sd = getSdmxData(prov, qy, st, ed)
    val sdView =
      if ( sd.provider == null )
        // SdmxData(provider.toUpperCase,query,start,end,"NA","NA")
        SdmxData(provider.toUpperCase,query,start,end)
      else sd
    Ok(views.html.sdmx(sdView))
  }

  // def plot(provider: String, query: String, start: String, end: String, format: String) = {
  //   if ( format != "svg" ) Action { Ok("Invalid plot type.") }
  //   else {
  //     val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)
  //     if ( prov == null ) Action { Ok("Invalid URL.") }
  //     else try {
  //       val file = new java.io.File(R.synchronized{R.evalS0(s"cache('$prov','$qy','$st','$ed')[['filename']]")})
  //       val source = scala.io.Source.fromFile(file)(scala.io.Codec.UTF8)
  //       val byteArray = source.map(_.toByte).toArray
  //       source.close()
  //       Action { Ok(byteArray).as("image/svg+xml") }
  //     } catch {
  //       case _: Throwable => Action { Ok("No data.") }
  //     }
  //   }
  // }

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

}

