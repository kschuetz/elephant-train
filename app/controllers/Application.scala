package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models.UserAuthResponse

object Application extends Controller with Secured {

  val loginDAO = models.slick.SlickLoginDAO
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text,
      "remember" -> boolean
    )
  )

  def login = Action { implicit request =>
    Ok(views.html.main("Login")(views.html.login(loginForm))).withSession("foo" -> "bar", "qwerty" -> "quux")
  }

  private def loginErrorToString(errorType: core.UserAuthenticator.LoginError): String = {
    import core.UserAuthenticator._

    errorType match {
      case InvalidCredentials => "Invalid credentials"
      case TooManyAttempts => "Too many login attempts.  Please wait a moment before trying again."
      case _ => "Unknown error"
    }
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.main("Login")(views.html.login(formWithErrors))),
      credentials => core.UserAuthenticator.authenticate(credentials._1, credentials._2) match {
        case Left(loginError) => Redirect(routes.Application.login).flashing("error" -> loginErrorToString(loginError))
        case Right(userInfo) => Ok("hello " + userInfo.emailAddress)
      }
    )
  }


  def logout = TODO

  def sandbox1 = Authenticated { user =>
    Action { implicit request =>
      Ok("hello " + user.userID.toString)
    }
  }

  def sandbox2 = Action { implicit request =>
    Ok(request.remoteAddress)
  }

}