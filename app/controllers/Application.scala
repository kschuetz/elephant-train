package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  val loginDAO = models.slick.SlickLoginDAO
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def login = TODO
  def authenticate = TODO
  def logout = TODO


}