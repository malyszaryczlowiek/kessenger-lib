ThisBuild / version := "0.3.2"
ThisBuild / organization := "io.github.malyszaryczlowiek"
ThisBuild / organizationName := "io.github.malyszaryczlowiek"
ThisBuild / organizationHomepage := Some(url("https://github.com/malyszaryczlowiek/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/malyszaryczlowiek/kessenger-lib"),
    "scm:git@github.com:malyszaryczlowiek/kessenger-lib.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "malyszaryczlowiek",
    name  = "RobertPomorski",
    email = "rrpomorski@gmail.com",
    url   = url("https://github.com/malyszaryczlowiek/")
  )
)

ThisBuild / description := "Simple util library containing types, traits and classes for Kessenger project."
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/malyszaryczlowiek/kessenger-lib"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true



lazy val scala212 = "2.12.16"
lazy val scala213 = "2.13.10"
lazy val scala31 = "3.1.1"
lazy val supportedScalaVersions = List(scala212, scala31)


lazy val root = (project in file("."))
  .aggregate(scala_2_12, scala_2_13, scala_3_1)
  .settings(
    // crossScalaVersions must be set to Nil on the aggregating project
    crossScalaVersions := Nil,
    publish / skip     := true
  )


lazy val commonSettings = Seq(
  name               := "kessenger-lib",
  idePackagePrefix   := Some("io.github.malyszaryczlowiek"),
  libraryDependencies ++= Seq(

    // kafka
    "org.apache.kafka"  %  "kafka-clients" % "3.1.0",


    // For Tests
    "org.scalameta" %% "munit"            % "0.7.29" % Test,
    "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test

  )
)


lazy val scala_2_12 = (project in file("scala-2.12"))
  .settings(
    idePackagePrefix   := Some("io.github.malyszaryczlowiek"),
    scalaVersion       := scala212,
    commonSettings,
    libraryDependencies ++= Seq(

      // kafka
      "org.apache.kafka" %% "kafka"               % "3.1.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0",

      // spark
      "org.apache.spark" %% "spark-sql"  % "3.3.0",

      // used for serdes
      "io.circe" %% "circe-core"    % "0.14.2",
      "io.circe" %% "circe-generic" % "0.14.2",
      "io.circe" %% "circe-parser"  % "0.14.2"
    )
    // other settings
  )


lazy val scala_2_13 = (project in file("scala-2.13"))
  .settings(
    idePackagePrefix   := Some("io.github.malyszaryczlowiek"),
    scalaVersion       := scala213,
    commonSettings,
    libraryDependencies ++= Seq(

      // kafka
      "org.apache.kafka" %% "kafka"               % "3.1.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0",

      // spark
      "org.apache.spark" %% "spark-sql"  % "3.3.0",

      // used for serdes
      "io.circe" %% "circe-core"    % "0.14.2",
      "io.circe" %% "circe-generic" % "0.14.2",
      "io.circe" %% "circe-parser"  % "0.14.2"
    )
    // other settings
  )


lazy val scala_3_1 = (project in file("scala-3.1"))
  .settings(
    idePackagePrefix   := Some("io.github.malyszaryczlowiek"),
    scalaVersion       := scala31,
    commonSettings,
    libraryDependencies ++= Seq(

      // kafka
      ("org.apache.kafka" %% "kafka"               % "3.1.0").cross(CrossVersion.for3Use2_13),
      ("org.apache.kafka" %% "kafka-streams-scala" % "3.1.0").cross(CrossVersion.for3Use2_13),

      // used for serdes
      ("io.circe" %% "circe-core"    % "0.14.2").cross(CrossVersion.for3Use2_13),
      ("io.circe" %% "circe-generic" % "0.14.2").cross(CrossVersion.for3Use2_13),
      ("io.circe" %% "circe-parser"  % "0.14.2").cross(CrossVersion.for3Use2_13)

    )
    // other settings
  )