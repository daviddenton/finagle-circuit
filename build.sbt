
val orgName = "io.github.daviddenton"

val projectName = "finagle-circuit"

organization := orgName

name := projectName

description := "Http circuit-breaking for Finagle"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.4", "2.11.6")

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.10",
  "com.twitter" %% "finagle-http" % "6.24.0" % "provided",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test")

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
