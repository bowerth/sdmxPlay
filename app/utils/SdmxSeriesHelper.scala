package utils

import it.bancaditalia.oss.sdmx.api.PortableTimeSeries
import scala.collection.immutable.ListMap

object SdmxSeriesHelper {

      def getName (tts: PortableTimeSeries[Double]): String = {
        tts.getName
      }

      def getTime(tts: PortableTimeSeries[Double]): Array[String] = {
        tts.getTimeSlotsArray.map(_.toString)
      }

      def modifyDate(date: String) = {

        def dateQtoD (date: String) = {
          val month = ((date.takeRight(1).toInt - 1) * 3 + 1).toString
          val fillzero = "0" * (2 - month.length)
          date.take(4) + "/" + fillzero + month + "/01"
        }

        if (date.length == 4) date + "/01/01"
        else if (date.length == 7)

        if (date.contains("Q"))
          dateQtoD(date)
        else
          date.replace("-", "/") + "/01"
        else
          date
      }

      def makeRowSeq(row: Int, data: Array[Array[String]]) = {
        for (col <- 0 to data.length-1)
        yield data(col)(row)
      }

      def makeRow(row: Int, data: Array[Array[String]], brackets: Boolean) = {
        if (brackets == true)
          "["+ makeRowSeq(row, data).mkString(",") +"]"
        else
          makeRowSeq(row, data).mkString(",")
      }

      def makeTable(data: Array[Array[String]], brackets: Boolean): String = {
        val tableSeq =
          for (row <- 0 to data(0).length-1)
          yield makeRow(row, data, brackets)
        val table =
          if (brackets == true)
            "[\n"+ tableSeq.mkString(",\n") +"\n]"
          else
            tableSeq.mkString("\n")
        return table
      }

      def stringExtreme(array: Array[String], which: String): Double = {
        val singleArrayDouble = for (s <- array; if s != "null") yield {
          s.toDouble
        }
        val res =
          if (which == "max") singleArrayDouble.max
          else singleArrayDouble.min
        return res
      }

      def fillValues(tts: PortableTimeSeries[Double], time: Array[String], fill: String): Array[String] = {

        def getValues(tts: PortableTimeSeries[Double]): Array[String] = {
          tts.getObservationsArray.map(_.toString)
        }

        val emptyVal = List.fill(time.length)("")
        val timeValueRef = (time zip emptyVal).toMap
        val timeA = getTime(tts).map(modifyDate)
        val valueA = getValues(tts)
        val timeValueA = (timeA zip valueA).toMap
        val joined = (new LeftOuterJoinMap(timeValueRef, timeValueA)).join
        val joinedSorted = ListMap(joined.toSeq.sortBy(_._1):_*)
        val joinedArray = joinedSorted.flatMap(e => List(e._2._2)).toArray
        val output =
          for (field <- joinedArray) yield {
            if (field.isEmpty) fill
            else field.get.toString
          }
        return output
      }

}
