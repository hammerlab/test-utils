name := "test-utils"
version := "1.0.0"
libraryDependencies ++= Seq(
  libraries.value('scalatest),
  "commons-io" % "commons-io" % "2.4"
)
