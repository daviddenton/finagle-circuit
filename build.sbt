import _root_.bintray.Plugin._

val orgName = "io.github.daviddenton"

val projectName = "finagle-circuit"

organization := orgName

name := projectName

description := "Http circuit-breaking for Finagle"

scalaVersion := "2.12.0"

crossScalaVersions := Seq("2.12.0", "2.11.8")

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.twitter" %% "bijection-util" % "0.9.5",
  "com.twitter" %% "finagle-http" % "6.41.0" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test")

licenses +=("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

pomExtra :=
  <url>http://{projectName}.github.io/</url>
    <scm>
      <url>git@github.com:daviddenton/{projectName}.git</url>
      <connection>scm:git:git@github.com:daviddenton/{projectName}.git</connection>
      <developerConnection>scm:git:git@github.com:daviddenton/{projectName}.git</developerConnection>
    </scm>
    <developers>
      <developer>
        <name>David Denton</name>
        <email>dev@fintrospect.io</email>
        <organization>{projectName}</organization>
        <organizationUrl>http://daviddenton.github.io</organizationUrl>
      </developer>
    </developers>

Seq(bintraySettings: _*)
