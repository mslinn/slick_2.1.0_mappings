organization := "com.micronautics"

name := "slick_2.1.0"

version := "0.1.0"

scalaVersion := "2.10.6"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
    "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/changeMe/tree/master€{FILE_PATH}.scala"
  )
}

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")

resolvers ++= Seq(
  "Lightbend Releases" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick"   %% "slick"             % "2.1.0" withSources(),
  "org.slf4j"            %  "slf4j-nop"         % "1.6.4",
  "org.postgresql"       %  "postgresql"        % "9.4.1208.jre7" withSources(),
  //"org.joda"             %  "joda-convert"      % "1.6" withSources(),
  //"com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0" withSources(),
  "org.scalatest"        %% "scalatest"         % "2.2.4" % "test"
)

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

cancelable := true

