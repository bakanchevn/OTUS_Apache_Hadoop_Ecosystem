name := "otus_spark_course"

version := "0.1"

scalaVersion := "2.12.12"

val hw1 = project.in(file("HW1")).settings(
  name := "HW1",
  libraryDependencies ++= Seq(dependencies.json4sNative)
)

lazy val dependencies =
  new {
    val json4sVersion = "3.7.0-M7"

    val json4sNative = "org.json4s" %% "json4s-native" % json4sVersion
  }