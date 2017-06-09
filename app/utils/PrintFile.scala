package utils

import java.io.File
import java.io.PrintWriter

class PrintFile

object PrintFile {

  /** @param file Output file of type java.io.File
    * @return A text file 
    * @example val file = new java.io.File("test.txt")
    *          withPrintWriter(file) { 
    *            writer => writer.println(List("a", "b").mkString("\n")) 
    *          }        
    */
    def withPrintWriter(file: File)(op: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

}
