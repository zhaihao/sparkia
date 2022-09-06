scalaVersion := "2.13.8"
name         := "sparkia"
organization := "me.ooon"

Global / excludeLintKeys := Set(idePackagePrefix)

idePackagePrefix := Some("me.ooon.sparkia")

libraryDependencies ++= Seq(NSCALA, OS_LIB, SQUANTS, ORISON, TYPESAFE_CONFIG, PLAY_JSON)
libraryDependencies ++= Seq(SCALA_TEST, LOG, SPARK).flatten

excludeDependencies in Global ++= excludes
dependencyOverrides in Global ++= overrides
