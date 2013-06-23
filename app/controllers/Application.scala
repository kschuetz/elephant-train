package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {

  val loginDAO = models.slick.SlickLoginDAO
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    )
  )

  def login = Action { implicit request =>
    Ok(views.html.main("Login")(views.html.login(loginForm)))
  }

  def authenticate = TODO
  def logout = TODO


}