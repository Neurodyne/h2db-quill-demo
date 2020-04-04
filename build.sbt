resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val commonSettings = Seq(
// Refine scalac params from tpolecat
  scalacOptions --= Seq(
    "-Xfatal-warnings"
  )
)

lazy val deps = libraryDependencies ++= Seq(
  "io.getquill"           %% "quill-jdbc" % Version.quill,
  "com.github.pureconfig" %% "pureconfig" % Version.pureconfig,
  "com.h2database"        % "h2"          % Version.h2db
)

lazy val root = (project in file("."))
  .settings(
    organization := "Neurodyne",
    name := "h2-demo",
    version := "0.0.1",
    scalaVersion := "2.13.1",
    maxErrors := 3,
    commonSettings,
    deps,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.3.2"
