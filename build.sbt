name := "Checkout"
version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.3" % Test,
  "org.scalacheck" %% "scalacheck" % "1.15.2" % Test,
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.3.0" % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
