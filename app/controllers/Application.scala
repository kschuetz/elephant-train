package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models.UserAuthResponse

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

    val now = new java.util.Date()
    val loginTime = new java.sql.Timestamp(now.getTime)

    def isThrottled(info: UserAuthResponse): Boolean = {
      info.loginDisallowedUntil.map { loginTime.before(_) } getOrElse(false)
    }

    def checkPassword(passwordIn: String, password: String): Boolean = {
      import com.github.t3hnar.bcrypt._
      passwordIn.isBcrypted(password)
    }

    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.main("Login")(views.html.login(formWithErrors))),
      credentials => {
        val authInfo = loginDAO.userLoginByEmailAddress(credentials._1)

        authInfo match {
          case Some(info) => {
            if(isThrottled(info)) Ok("throttled")
            else {
              if(checkPassword(credentials._2, info.password)) {
                loginDAO.updateSuccessForUser(info.userID, Some(loginTime))
                Ok("hello " + info.emailAddress)
              } else {
                Ok("bad password")
              }
            }
          }
          case _ => Ok("nope")
        }
      }

    )

  }


  def logout = TODO


}