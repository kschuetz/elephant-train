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

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.main("Login")(views.html.login(formWithErrors))),
      credentials => {
        val authInfo = loginDAO.userLoginByEmailAddress(credentials._1)

        authInfo match {
          case Some(info) => Ok("hello " + info.emailAddress)
          case _ => Ok("nope")
        }
      }

    )

  }


  def logout = TODO


}