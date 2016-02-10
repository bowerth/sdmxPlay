package controllers

import java.util._
import models.TemperatureData
// import models.SdmxData
import play.api.mvc._
import scala.io.Source

object Weather extends Controller {

  private val file = "R/weather.R"

  private val lines = Source.fromFile(file).getLines().toList

  private val R = org.ddahl.rscala.callback.RClient()

  R.synchronized{ R eval lines.mkString("\n") }

  private val errorTemperatureData = TemperatureData(null,null,"NA","NA","NA","NA")

  private def getTemperatureData(location: String, year: String) = {
    if ( location == null ) errorTemperatureData
    else try {
      val s1 = R.synchronized{R.evalS1(s"unlist(cache('$location', '$year'))")}
      TemperatureData(location,year,s1(1),s1(2),s1(3),s1(4))
    } catch {
      case _: Throwable => errorTemperatureData
    }
  }

  // private val errorSdmxData = SdmxData(null,null,null,null,"NA")

  // private def getSdmxData(provider: String, query: String, start: String, end: String) = {
  //   if ( provider == null ) errorSdmxData
  //   else try {
  //     val s1 = R.synchronized{R.evalS1(s"unlist(cache('$provider', '$query', '$start', '$end'))")}
  //     SdmxData(provider, query, start, end, s1(1))
  //   } catch {
  //     case _: Throwable => errorSdmxData
  //   }
  // }


  private def validate(location: String, year: String): (String,String) = {
    if ( location.matches("\\w{3,4}") && year.matches("\\d{4}") ) (location.toUpperCase,year)
    else (null,null)
  }

  // private def validateSdmx(provider: String, query: String, start: String, end: String): (String,String,String,String) = {
  //   if ( start.matches("\\d{4}") && end.matches("\\d{4}") ) (provider.toUpperCase, query, start, end)
  //   else (null,null,null,null)
  // }

  def index = Action { implicit request =>
    val previousYear = (Calendar.getInstance().get(Calendar.YEAR)-1).toString
    Redirect(routes.Weather.main("PVU",previousYear))
  }

  def redirect(location: String, year: String) = Action { implicit request =>
    Redirect(routes.Weather.main(location,year))
  }

  def main(location: String, year: String) = Action {
  // def main(location: String, year: String, provider: String, query: String, start: String, end: String) = Action {
    val (loc,yr) = validate(location,year)
    val td = getTemperatureData(loc,yr)
    val tdView = if ( td.location == null ) TemperatureData(location.take(4).toUpperCase,year.take(4),"NA","NA","NA","NA")
    else td

    // val (prov, qy, st, ed) = validateSdmx(provider, query, start, end)
    // val sd = getSdmxData(prov, qy, st, ed)
    // val sdView = if ( sd.provider == null ) SdmxData(provider.take(4).toUpperCase,query.take(4),"NA","NA","NA")
    // else sd

    Ok(views.html.weather(tdView))
    // Ok(views.html.weather(sdView))

  }

  def plot(location: String, year: String, format: String) = {
    if ( format != "svg" ) Action { Ok("Invalid plot type.") }
    else {
      val (loc,yr) = validate(location,year)
      if ( loc == null ) Action { Ok("Invalid URL.") }
      else try {
        val file = new java.io.File(R.synchronized{R.evalS0(s"cache('$loc','$yr')[['filename']]")})
        val source = scala.io.Source.fromFile(file)(scala.io.Codec.UTF8)
        val byteArray = source.map(_.toByte).toArray
        source.close()
        Action { Ok(byteArray).as("image/svg+xml") }
      } catch {
        case _: Throwable => Action { Ok("No data.") }
      }
    }
  }

}

