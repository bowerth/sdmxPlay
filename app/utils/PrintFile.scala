package utils

import java.io.File
import java.io.PrintWriter

class PrintFile

object PrintFile {

  def withPrintWriter(file: File)(op: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

}
