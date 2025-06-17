scalaVersion := "2.13.16"
name         := "sparkia"
organization := "me.ooon"

Global / excludeLintKeys := Set(idePackagePrefix)

idePackagePrefix := Some("me.ooon.sparkia")

libraryDependencies ++= Seq(NSCALA, OS_LIB, SQUANTS, ORISON, TYPESAFE_CONFIG, PLAY_JSON)
libraryDependencies ++= Seq(SCALA_TEST, LOG, SPARK).flatten

Test / fork := true
Test / javaOptions ++= Seq(
  "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
)

excludeDependencies in Global ++= excludes
dependencyOverrides in Global ++= overrides
