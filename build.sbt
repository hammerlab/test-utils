name := "test-utils"
version := "1.3.1-SNAPSHOT"

addScala212

enableMacroParadise

deps ++= Seq(
  commons_io,
  paths % "1.2.0",
  scalatest,
  scala_reflect
)

// Don't inherit default test-deps from parent plugin.
testDeps := Seq()
