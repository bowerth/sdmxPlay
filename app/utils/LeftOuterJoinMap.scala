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
    // map1.map {case (k1, v1) =>
    // 	map2.get(k1) match {
    // 		case v2: Some[V2] => (k1 -> (Some(v1), v2))
    // 		case None => (k1 -> (Some(v1), None))
    // 	}
    // }
    map1.map { case (k, v) => (k -> ((Some(v), map2.get(k)): (Option[V1], Option[V2]))) }
  }
}

object LeftOuterJoinMap {
  
  def main(args: Array[String]): Unit = {
    
    val map1 = Map(1 -> "a", 2 -> "b", 3 -> "c")
    val map2 = Map(1 -> "1",           3 -> "3")

    val joined = (new LeftOuterJoinMap(map1, map2)).join

    // // two arrays to map
    // val times = Array[String]("2002/01/01", "2002/02/01", "2002/03/01", "2002/04/01")
    // val values = Array[String]("10", "20", "30", "40")
    // val timesValues = times zip values
    // val timesValuesMap = timesValues.toMap

    // val times2 = Array[String]("2002/01/01", "2002/04/01")
    // val values2 = Array[String]("10", "40")
    // val timesValues2 = times2 zip values2
    // val timesValuesMap2 = timesValues2.toMap


    // val joined2 = (new LeftOuterJoinMap(timesValuesMap, timesValuesMap2)).join

    // def h(k:Int, v:Int) = if (v > 2) Some(k->v) else None
    // def h(k: Int, v: Int) = if (v > 2) Some(k -> v) else None
    // val m = Map(1 -> 2, 2 -> 4, 3 -> 6)
    // m.toList.toMap
    // m.flatMap { case (k, v) => h(k, v) }

    // // http://stackoverflow.com/questions/5582862/scala-how-to-create-a-mapk-v-from-a-setk-and-a-function-from-k-to-v
    // val s = Set(2, 3, 5)
    // def func(i: Int) = "" + i + i
    // val func2 = (r: Map[Int,String], i: Int) => r + (i -> func(i))
    // s.foldLeft(Map.empty[Int,String])(func2)

    // map1
    // http://www.brunton-spall.co.uk/post/2011/12/02/map-map-and-flatmap-in-scala/
    //
    // scala> val m = Map(1 -> 2, 2 -> 4, 3 -> 6)
    // m: scala.collection.immutable.Map[Int,Int] = Map(1 -> 2, 2 -> 4, 3 -> 6)
    //
    // scala> def h(k:Int, v:Int) = if (v > 2) Some(k->v) else None
    // h: (k: Int, v: Int)Option[(Int, Int)]
    //
    // scala> m.flatMap { case (k,v) => h(k,v) }
    // res108: scala.collection.immutable.Map[Int,Int] = Map(2 -> 4, 3 -> 6)

    println(joined)
  }

}