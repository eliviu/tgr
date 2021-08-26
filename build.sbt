name := "trg"

version := "0.1"

scalaVersion := "2.12.13"
val sparkVersion = "3.1.2"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.typesafe.play" %% "play" % "2.8.8",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "net.liftweb" %% "lift-json" % "3.4.3",
  "com.google.code.gson" % "gson" % "2.8.8",
  guice
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, DockerPlugin)
  .settings(
    name := """play-scala-hello-world-tutorial""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    ),
    dockerAliases ++= Seq(dockerAlias.value.withTag(Option("stable")))
  )

Compile / unmanagedSourceDirectories += baseDirectory.value / "src"
Universal / javaOptions ++= Seq(
  "-Dpidfile.path=/dev/null"
)