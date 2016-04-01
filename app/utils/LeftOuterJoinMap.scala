// https://gist.github.com/jkyamog/2870693
package utils

class LeftOuterJoinMap[K, V1, V2](map1: Map[K, V1], map2: Map[K, V2]) {

  type JoinValue = Tuple2[Option[V1], Option[V2]]

  def join: Map[K, JoinValue] = {
    map1.map { case (k, v) => (k -> ((Some(v), map2.get(k)): (Option[V1], Option[V2]))) }
  }
}

// object LeftOuterJoinMap {
  
//   def main(args: Array[String]): Unit = {
    
//     val times = Array[String]("2002/01/01", "2002/02/01", "2002/03/01", "2002/04/01")
//     val values = Array[String]("10", "20", "30", "40")
//     val timesValues = times zip values
//     val timesValuesMap = timesValues.toMap

//     val times2 = Array[String]("2002/01/01", "2002/04/01")
//     val values2 = Array[String]("10", "40")
//     val timesValues2 = times2 zip values2
//     val timesValuesMap2 = timesValues2.toMap

//     val joined2 = (new LeftOuterJoinMap(timesValuesMap, timesValuesMap2)).join

//     println(joined2)
//   }

// }
