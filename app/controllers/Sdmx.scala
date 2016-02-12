package controllers

import models.SdmxData
import play.api.mvc._
import scala.io.Source

import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
// import java.io._

object Sdmx extends Controller {

  private def validateYear(year: Option[String]): Option[String] = {
    val empty = Option[String]("")
    if ( year.isEmpty || !year.get.matches("\\d{4}") ) (empty)
    else (year)
  }

  private def validateSdmx(provider: String, query: String, start: Option[String], end: Option[String]): (String, String, Option[String], Option[String]) = {
    // if ( start.get.matches("\\d{4}") && end.get.matches("\\d{4}") ) (provider.toUpperCase, query, start, end)
    // else (null,null,null,null)
    val altstart = validateYear(start)
    val altend = validateYear(end)
    (provider.toUpperCase, query, altstart, altend)
  }

  def redirect(provider: String, query: String, start: Option[String], end: Option[String]) = Action { implicit request =>
    Redirect(routes.Sdmx.main(provider, query, start, end))
  }

  def index = Action { implicit request =>
    val query = "EXR.A.USD+GBP+CAD+AUD.EUR.SP00.A"
    val provider = "ECB"
    val start = Option[String](null)
    val end = Option[String](null)
    // Redirect(routes.Sdmx.main("ECB", queryECB, Option[String](null), Option[String](null)))
    Redirect(routes.Sdmx.main(provider, query, start, end))
  }

  def main(provider: String, query: String, start: Option[String], end: Option[String]) = Action {
    val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)

    val sd = getSdmxData(prov, qy, st, ed)
    val sdView =
      if ( sd.provider == null )
        SdmxData(provider.toUpperCase, query, start, end, "")
        // SdmxData(provider.toUpperCase,query,start,end)
      else sd
    Ok(views.html.sdmx(sdView))
  }

  private val errorSdmxData = SdmxData(null, null, null, null, "")
  // private val errorSdmxData = SdmxData(null, null, null, null)

  private def getName (tts: PortableTimeSeries): String = {
    tts.getName
  }

  private def getTime(tts: PortableTimeSeries): Array[String] = {
    tts.getTimeSlotsArray.map(_.toString)
  }

  private def dateQtoD (date: String) = {
    val month = ((date.takeRight(1).toInt - 1) * 3 + 1).toString
    val fillzero = "0" * (2 - month.length)
    date.take(4) + "/" + fillzero + month + "/01"
  }

  private def modifyDate(date: String) = {
    if (date.length == 4) date + "/01/01"
    else if (date.length == 7)
      if (date.contains("Q"))
        dateQtoD(date)
      else
        date + "/01"
    else
      date
  }
  // Array("1999", "1999-02", "1999-Q2").map(modifyDate)

  private def getValues(tts: PortableTimeSeries): Array[String] = {
    tts.getObservationsArray.map(_.toString)
  }

  private def makeRowSeq(row: Int, data: Array[Array[String]]) = {
    for (col <- 0 to data.length-1)
    yield data(col)(row)
    // yield "["+ data(col)(row) +"]"
  }

  private def makeRow(row: Int, data: Array[Array[String]]) = {
    // makeRowSeq(row, data).mkString(",")
    "["+ makeRowSeq(row, data).mkString(",") +"]"
  }

  private def makeTable(data: Array[Array[String]]) = {
    val tableSeq =
      for (row <- 0 to data(0).length-1)
      yield makeRow(row, data)
    "[\n"+ tableSeq.mkString(",\n") +"\n]"
    // tableSeq.mkString("\\n")
  }
  // val row = data(1)
  // val data = dataArray
  // val l = makeTable(data = dataArray)


  private def getSdmxData(provider: String, query: String, start: Option[String], end: Option[String]) = {
    if ( provider == null ) errorSdmxData
    else try {

      // val query = "EXR.A+Q.USD+GBP.EUR.SP00.A"
      // val provider = "ECB"
      // val start = Option[String]("2002")
      // val end = Option[String]("2003")

      val res = SdmxClientHandler.getTimeSeries(provider, query, start.get, end.get)
      val res2 = res.toArray.
        map(_.asInstanceOf[PortableTimeSeries])
      //
      val nameArray = for (ts <- res2) yield getName(ts)
      val headerArray = "TIME_PERIOD" +: nameArray
      //
      // val time0 = getTime(res2(0))
      val time0 = getTime(res2(0)).map(modifyDate)

      // val timeArray = for (series <- res2) yield getTime(series).map(modifyDate)
      // val timeArrayLength = timeArray.map(_.length)
      // val timeArrayLongest = timeArray(timeArrayLength.indexOf(timeArrayLength.max))

      val valueArray = for (series <- res2) yield getValues(series)
      val dataArray = time0 +: valueArray
      //
      val l = makeTable(data = dataArray)
      //
      // val output = headerArray.mkString(",") + "\n" + l
      // val output = headerArray.mkString(",") + "\\n" + l
      val output = l + ",\n{labels: [ \"" + headerArray.mkString("\",\"") + "\" ] }"
      //
      // val writer = new PrintWriter(new File("public/data/sdmx/tsdata.csv"))
      // writer.write(output)
      // writer.close()p

      // SdmxData(provider, query, start, end)
      SdmxData(provider, query, start, end, output)
    } catch {
      case _: Throwable => errorSdmxData
    }
  }

}

