organization := "uk.co.mikecobra"
name := "wanigraphi-api"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.2"

val Http4sVersion = "0.17.0-M3"

libraryDependencies ++= Seq(
  "org.http4s"     %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"     %% "http4s-circe"        % Http4sVersion,
  "org.http4s"     %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback" %  "logback-classic"     % "1.2.1"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.9.1" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

coverageEnabled := true
