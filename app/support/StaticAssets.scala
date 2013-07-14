package controllers

import play.api.mvc._

object StaticAssets {

  val cdnPath = "//s3.amazonaws.com/mentaldoor-1/elephant-train"

  val useCDN = play.api.Play.isProd(play.api.Play.current)

  def at(path: String): Call =
    if (useCDN) prod(path) else dev(path)

  def prod(path: String): Call =
    Call("GET", s"$cdnPath/$path")

  def dev(path: String): Call =
    routes.Assets.at(path)

}