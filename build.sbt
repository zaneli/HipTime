name := "hiptime"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1" % "compile",
  "org.slf4j" % "slf4j-api" % "1.7.5" % "compile",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "compile",
  "org.scalaj" %% "scalaj-http" % "0.3.11" % "compile",
  "net.liftweb" %% "lift-json" % "2.5.1" % "compile",
  "org.scalaj" % "scalaj-time_2.10.2" % "0.7" % "compile"
)

play.Project.playScalaSettings
