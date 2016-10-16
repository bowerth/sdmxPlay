name := """sdmxPlay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
  //   ,
  // "org.webjars" %% "webjars-play" % "2.2.0",
  // "org.webjars" % "bootstrap" % "2.3.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
// routesGenerator := InjectedRoutesGenerator
