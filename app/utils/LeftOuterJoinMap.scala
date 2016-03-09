//https://gist.github.com/jkyamog/2870693
// comment package name to prevent subfolder creation
// package experiments
// navigate to folder with scala file and run scalac "scalafile"
// run scala with name of object/class: "scala LeftOuterJoinMap"

// result:
//  Map(1 -> (Some(a),Some(1)), 2 -> (Some(b),None), 3 -> (Some(c),Some(3)))

package utils

class LeftOuterJoinMap[K, V1, V2](map1: Map[K, V1], map2: Map[K, V2]) {

  type JoinValue = Tuple2[Option[V1], Option[V2]]

  def join: Map[K, JoinValue] = {
    map1.map { case (k, v) => (k -> ((Some(v), map2.get(k)): (Option[V1], Option[V2]))) }
  }
}

object LeftOuterJoinMap {
  
  def main(args: Array[String]): Unit = {
    
    // val map1 = Map(1 -> "a", 2 -> "b", 3 -> "c")
    // val map2 = Map(1 -> "1",           3 -> "3")
    // val joined = (new LeftOuterJoinMap(map1, map2)).join

    val times = Array[String]("2002/01/01", "2002/02/01", "2002/03/01", "2002/04/01")
    val values = Array[String]("10", "20", "30", "40")
    val timesValues = times zip values
    val timesValuesMap = timesValues.toMap

    val times2 = Array[String]("2002/01/01", "2002/04/01")
    val values2 = Array[String]("10", "40")
    val timesValues2 = times2 zip values2
    val timesValuesMap2 = timesValues2.toMap

    val joined2 = (new LeftOuterJoinMap(timesValuesMap, timesValuesMap2)).join

    // println(joined)
    println(joined2)
  }

}
