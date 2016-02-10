// http://ensime.github.io/build_tools/sbt/
import sbt._
import org.ensime.Imports.EnsimeKeys

object EnsimeProjectSettings extends AutoPlugin {
  override def requires = org.ensime.EnsimePlugin
  override def trigger = allRequirements
  override def projectSettings = Seq(
    // your settings here
  )
}