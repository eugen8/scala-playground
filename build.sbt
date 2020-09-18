name := "scala-playground"

version := "0.1"

scalaVersion := "2.13.3"



libraryDependencies ++= {
  val json4sVersion = "3.6.9"

  Seq(
    "org.json4s" %% "json4s-native" % json4sVersion,
    "org.typelevel" %% "cats-core" % "2.0.0",
    "org.mockito" % "mockito-core" % "2.24.5" % "it,test",
    "org.scalatest" %% "scalatest" % "3.1.1" % "it,test",
  )
}
