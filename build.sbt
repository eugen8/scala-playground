name := "scala-playground"
version := "0.1"
scalaVersion := "2.13.3"

libraryDependencies ++= {
  val json4sVersion = "3.6.9"
  val AkkaVersion = "2.6.13"

  Seq(
    "org.json4s" %% "json4s-native" % json4sVersion,
    "org.typelevel" %% "cats-core" % "2.0.0",
    "org.typelevel" %% "cats-effect" % "3.1.0",
    "org.typelevel" %% "cats-tagless-macros" % "0.14.0",
    "org.mockito" % "mockito-core" % "2.24.5" % "it,test",
    "org.scalactic" %% "scalactic" % "3.2.0",
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.0" % "it,test",
  )
}

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

//scalacOptions += "-Ypartial-unification" //not needed for scala 2.13