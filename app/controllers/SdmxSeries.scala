package controllers

import javax.inject.Inject
import play.api.mvc._
import models.SdmxData
import it.bancaditalia.oss.sdmx.client.SdmxClientHandler
import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
import utils.SdmxSeriesHelper._
import java.io.PrintWriter
import java.io.File
import utils.PrintFile.withPrintWriter


// object SdmxSeries extends Controller {
class SdmxSeries @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def redirect(provider: String, query: String, start: Option[String], end: Option[String]) = Action { implicit request =>
    Redirect(routes.SdmxSeries.main(provider, query, start, end))
  }

  def index = Action { implicit request =>
//    val query = "STANI4_2016.*.INTP.D05T39"
//    val provider = "OECD"
    val query = "CPI.1.*.40027.*.Q"
    val provider = "ABS"
    val start = Option[String]("1980")
    val end = Option[String]("2019")
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

  def getSdmxData(provider: String, query: String, start: Option[String], end: Option[String]): SdmxData = {
    if ( provider == null ) errorSdmxData
    else try {
      val res = SdmxClientHandler.getTimeSeries(provider, query, start.get, end.get)
      val res2 = res.toArray.
        map(_.asInstanceOf[PortableTimeSeries[Double]])
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
//      val file = new File("public/output.csv")
//      val valueArrayCsv = for (series <- res2) yield fillValues(series, timeRef, "")
//      val dataArrayCsv = timeRef +: valueArrayCsv
//      val lcsv = makeTable(data = dataArrayCsv, brackets = false)
//      withPrintWriter(file) {
//        writer => writer.println(headerArray.mkString(",") +"\n"+ lcsv)
//      }
      // function return value
      return SdmxData(provider, query, start, end, output, labels, nameMin, nameMax)
    } catch {
      case _: Throwable => errorSdmxData
    }
  }


}
