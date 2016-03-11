package controllers

import play.api.mvc._

import models.SdmxData
import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
// import utils.LeftOuterJoinMap
import utils.SdmxSeriesHelper._
// import scala.collection.immutable.ListMap

import java.io.PrintWriter
import java.io.File
import utils.PrintFile.withPrintWriter

object SdmxSeries extends Controller {

  def redirect(provider: String, query: String, start: Option[String], end: Option[String]) = Action { implicit request =>
    Redirect(routes.SdmxSeries.main(provider, query, start, end))
  }

  def index = Action { implicit request =>
    val query = "EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A"
    val provider = "ECB"
    val start = Option[String](null)
    val end = Option[String](null)
    Redirect(routes.SdmxSeries.main(provider, query, start, end))
  }

  def main(provider: String, query: String, start: Option[String], end: Option[String]) = Action {
    val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)

    val sd = getSdmxData(prov, qy, st, ed)
    val sdView =
      if ( sd.provider == null )
        // SdmxData(provider.toUpperCase, query, start, end, "")
        SdmxData(provider.toUpperCase, query, start, end, "", "", "")
      else sd
    Ok(views.html.sdmx(sdView))
  }

  def validateYear(year: Option[String]): Option[String] = {
    val empty = Option[String]("")
    if ( year.isEmpty || !year.get.matches("\\d{4}") ) (empty)
    else (year)
  }

  def validateSdmx(provider: String, query: String, start: Option[String], end: Option[String]): (String, String, Option[String], Option[String]) = {
    val altstart = validateYear(start)
    val altend = validateYear(end)
    (provider.toUpperCase, query, altstart, altend)
  }

  // val errorSdmxData = SdmxData(null, null, null, null, "")
  val errorSdmxData = SdmxData(null, null, null, null, "", "", "")

  private def getSdmxData(provider: String, query: String, start: Option[String], end: Option[String]): SdmxData = {
    if ( provider == null ) errorSdmxData
    else try {

      // def getName (tts: PortableTimeSeries): String = {
      //   tts.getName
      // }

      // def getTime(tts: PortableTimeSeries): Array[String] = {
      //   tts.getTimeSlotsArray.map(_.toString)
      // }

      // def modifyDate(date: String) = {

      //   def dateQtoD (date: String) = {
      //     val month = ((date.takeRight(1).toInt - 1) * 3 + 1).toString
      //     val fillzero = "0" * (2 - month.length)
      //     date.take(4) + "/" + fillzero + month + "/01"
      //   }

      //   if (date.length == 4) date + "/01/01"
      //   else if (date.length == 7)

      //   if (date.contains("Q"))
      //     dateQtoD(date)
      //   else
      //     // date + "/01"
      //     date.replace("-", "/") + "/01"
      //   else
      //     date
      // }
      // // Array("1999", "1999-02", "1999-Q2").map(modifyDate)

      // def makeRowSeq(row: Int, data: Array[Array[String]]) = {
      //   for (col <- 0 to data.length-1)
      //   yield data(col)(row)
      // }

      // def makeRow(row: Int, data: Array[Array[String]], brackets: Boolean) = {
      //   if (brackets == true)
      //     "["+ makeRowSeq(row, data).mkString(",") +"]"
      //   else
      //     makeRowSeq(row, data).mkString(",")
      // }

      // def makeTable(data: Array[Array[String]], brackets: Boolean): String = {
      //   val tableSeq =
      //     for (row <- 0 to data(0).length-1)
      //     yield makeRow(row, data, brackets)
      //   val table =
      //     if (brackets == true)
      //       "[\n"+ tableSeq.mkString(",\n") +"\n]"
      //     else
      //       tableSeq.mkString(",\n")
      //   return table
      // }

      // def stringExtreme(array: Array[String], which: String): Double = {
      //   val singleArrayDouble = for (s <- array; if s != "null") yield {
      //     s.toDouble
      //   }
      //   val res =
      //     if (which == "max") singleArrayDouble.max
      //   // if (which == "max") singleArrayDouble.reduceLeft(_ max _)
      //     else singleArrayDouble.min
      //   return res
      // }

      // def fillValues(tts: PortableTimeSeries, time: Array[String], fill: String): Array[String] = {

      //   def getValues(tts: PortableTimeSeries): Array[String] = {
      //     tts.getObservationsArray.map(_.toString)
      //   }

      //   val emptyVal = List.fill(time.length)("")
      //   val timeValueRef = (time zip emptyVal).toMap
      //   val timeA = getTime(tts).map(modifyDate)
      //   val valueA = getValues(tts)
      //   val timeValueA = (timeA zip valueA).toMap
      //   val joined = (new LeftOuterJoinMap(timeValueRef, timeValueA)).join
      //   val joinedSorted = ListMap(joined.toSeq.sortBy(_._1):_*)
      //   val joinedArray = joinedSorted.flatMap(e => List(e._2._2)).toArray
      //   val output =
      //     for (field <- joinedArray) yield {
      //       if (field.isEmpty) fill
      //       else field.get.toString
      //     }
      //   return output
      // }

      // val query = "EXR.A+Q.USD+GBP.EUR.SP00.A"
      // val query = "EXR.A+Q+M.USD+GBP.EUR.SP00.A"
      // val query = "EXR.M.USD+GBP.EUR.SP00.A"
      // val query = "EXR.M.*.EUR.SP00.A"
      // val provider = "ECB"
      // val query = "QNA.AUT.B1G.CQR.Q"
      // QNA.AUT+DEU+FIN+DNK+SWE+USA.B1G.VNBQR+VNBARSA+VIXNBSA+LNBQRSA+DOBSA+CTQRGPSA+CPCARSA+VOBARSA+VIXOBSA+CUR+CARSA+GRW+CD+VNBQRSA+HRSSA+POP+CAR+IND+HCPCARSA+VNBAR+CQRSA+LNBARSA+GYSA+GPSA+LNBQR+VOL+PERSA+CQR+HVPVOBARSA+VPVOBARSA+DNBSA+HRS+PER.Q
      // val provider = "OECD"
      // val start = Option[String]("2000")
      // val end = Option[String]("2015")

      val res = SdmxClientHandler.getTimeSeries(provider, query, start.get, end.get)
      val res2 = res.toArray.
        map(_.asInstanceOf[PortableTimeSeries])
      //
      val nameArray = for (ts <- res2) yield getName(ts)
      val headerArray = "TIME_PERIOD" +: nameArray

      val seriesLength = for (series <- res2) yield getTime(series).length
      val indexLongest = seriesLength.indexOf(seriesLength.max)
      val timeRef = getTime(res2(indexLongest)).map(modifyDate)
      val valueArray = for (series <- res2) yield fillValues(series, timeRef, "null")

      val maxArray = for (s <- valueArray) yield stringExtreme(s, "max")
      val nameMax =  nameArray(maxArray.indexOf(maxArray.max))
      val minArray = for (s <- valueArray) yield stringExtreme(s, "min")
      val nameMin =  nameArray(minArray.indexOf(minArray.min))

      // val nameMin = "testMin"
      // val nameMax = "testMax"

      // JavaScript data format for dygraphs
      val timeRefDate = for (date <- timeRef) yield ("new Date(\"" + date + "\")")
      val dataArray = timeRefDate +: valueArray
      val l = makeTable(data = dataArray, brackets = true)
      val output = l + ",\n{labels: [ \"" + headerArray.mkString("\",\"") + "\" ] }"

      // csv data format for download
      val file = new File("output.csv")
      // val file = new File(provider +"-"+ query.split("[.]")(0) +".csv")
      val valueArrayCsv = for (series <- res2) yield fillValues(series, timeRef, "")
      val dataArrayCsv = timeRef +: valueArrayCsv
      val lcsv = makeTable(data = dataArrayCsv, brackets = false)
      withPrintWriter(file) {
        writer => writer.println(headerArray.mkString(",") +"\n"+ lcsv)
      }

      // function return value
      // SdmxData(provider, query, start, end, output)
      return SdmxData(provider, query, start, end, output, nameMin, nameMax)
      // SdmxData(provider, query, start, end, output, "", "")
    } catch {
      case _: Throwable => errorSdmxData
    }
  }

}

