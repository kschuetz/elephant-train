import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "elephant-train"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "com.typesafe.play" %% "play-slick" % "0.3.2",
    "net.debasishg" %% "redisclient" % "2.10",
    "commons-codec" % "commons-codec" % "1.4",
    "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.2",
    "com.github.nscala-time" %% "nscala-time" % "0.4.2"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
