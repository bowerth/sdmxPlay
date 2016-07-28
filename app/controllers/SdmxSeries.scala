package controllers

import play.api.mvc._
import models.SdmxData
import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
import utils.SdmxSeriesHelper._
import java.io.PrintWriter
import java.io.File
import utils.PrintFile.withPrintWriter

object SdmxSeries extends Controller {

  def redirect(provider: String, query: String, start: Option[String], end: Option[String]) = Action { implicit request =>
    Redirect(routes.SdmxSeries.main(provider, query, start, end))
  }

  def index = Action { implicit request =>
    // val query = "EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A"
    // val query = "EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.*"
    // val provider = "ECB"
    // val start = Option[String]("1999")
    // val query = "CNA-2005-FBCF-SI-A17.S11ES14AA.*.IPCH"
    // val provider = "INSEE"
    val query = "nama_gdp_c.A.EUR_HAB.B11.AT"
    val provider = "EUROSTAT"
    // val start = Option[String](null)
    // val start = Option[String]("1950")
    // val end = Option[String](null)
    val start = Option[String]("2000")
    val end = Option[String]("2013")
    Redirect(routes.SdmxSeries.main(provider, query, start, end))
  }

  def main(provider: String, query: String, start: Option[String], end: Option[String]) = Action {
    val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)
    val sd = getSdmxData(prov, qy, st, ed)
    val sdView =
      if ( sd.provider == null )
        SdmxData(provider.toUpperCase, query, start, end, "", "", "", "")
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

  val errorSdmxData = SdmxData(null, null, null, null, "", "", "", "")

  private def getSdmxData(provider: String, query: String, start: Option[String], end: Option[String]): SdmxData = {
    if ( provider == null ) errorSdmxData
    else try {
      val res = SdmxClientHandler.getTimeSeries(provider, query, start.get, end.get)
      val res2 = res.toArray.
        map(_.asInstanceOf[PortableTimeSeries])
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
      // JavaScript data format for dygraphs
      val timeRefDate = for (date <- timeRef) yield ("new Date(\"" + date + "\")")
      val dataArray = timeRefDate +: valueArray
      val l = makeTable(data = dataArray, brackets = true)
      val output = l
      val labels = "[ \"" + headerArray.mkString("\",\"") + "\" ]"
      // csv data format for download
      val file = new File("output.csv")
      val valueArrayCsv = for (series <- res2) yield fillValues(series, timeRef, "")
      val dataArrayCsv = timeRef +: valueArrayCsv
      val lcsv = makeTable(data = dataArrayCsv, brackets = false)
      withPrintWriter(file) {
        writer => writer.println(headerArray.mkString(",") +"\n"+ lcsv)
      }
      // function return value
      return SdmxData(provider, query, start, end, output, labels, nameMin, nameMax)
    } catch {
      case _: Throwable => errorSdmxData
    }
  }

}
