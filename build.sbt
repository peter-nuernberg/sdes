val scala3Version = "3.0.2"

// === MAIN DEPENDENCIES ===

// --- cats ---

val catsVersion = "2.6.1"
val catsDeps = Seq("cats-core")
    .map(m => "org.typelevel" %% m % catsVersion)

// --- commons math ---

val commonsMathVersion = "3.6.1"
val commonsMathDeps = Seq("commons-math3")
    .map(m => "org.apache.commons" % m % commonsMathVersion)

// --- scalactic ---

val scalacticVersion = "3.2.9"
val scalacticDeps = Seq("scalactic")
    .map(m => "org.scalactic" %% m % scalacticVersion)

// --- scalactic ---

val scalaLoggingVersion = "3.9.4"
val scalaLoggingDeps = Seq("scala-logging")
    .map(m => "com.typesafe.scala-logging" %% m % scalaLoggingVersion)

val logbackVersion = "1.2.6"
val logbackDeps = Seq("logback-classic")
    .map(m => "ch.qos.logback" % m % logbackVersion)

// === TEST DEPENDENCIES ===

// --- scalacheck ---

val scalacheckVersion = "1.15.4"
val scalacheckDeps = Seq("scalacheck")
    .map(m => "org.scalacheck" %% m % scalacheckVersion % "test")

// --- scalatest ---

val scalatestVersion = "3.2.9"
val scalatestDeps = Seq("scalatest")
    .map(m => "org.scalatest" %% m % scalatestVersion % "test")

val scalatestPlusVersion = scalatestVersion + ".0"
val scalacheckBridgeSuffix = scalacheckVersion
    .split(raw"\.").iterator.take(2).foldLeft("") { case (acc, elem) => acc + s"-$elem" }
val scalatestPlusDeps = Seq(s"scalacheck$scalacheckBridgeSuffix")
    .map(m => "org.scalatestplus" %% m % scalatestPlusVersion % "test")

// === MODULES ===

// --- root ---

lazy val root = project
  .in(file("."))
  .settings(
    name := "sdes",
    version := "0.1.0",
    scalaVersion := scala3Version,

    libraryDependencies ++=
        catsDeps
            ++ commonsMathDeps
            ++ scalacticDeps
            ++ scalacheckDeps
            ++ scalaLoggingDeps
            ++ logbackDeps
            ++ scalatestDeps
            ++ scalatestPlusDeps
)
