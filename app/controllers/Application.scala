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

    import com.github.nscala_time.time.Imports._

    val now = DateTime.now
    val loginTime = new java.sql.Timestamp(now.getMillis)

    def isThrottled(info: UserAuthResponse): Boolean = {
      info.loginDisallowedUntil.map { loginTime.before(_) } getOrElse(false)
    }

    def getThrottleTime(failedAttempts: Int): Option[java.sql.Timestamp] = {
      if(failedAttempts < 1) None
      else {
        val interval = failedAttempts match {
          case 1 => 5
          case 2 => 15
          case _ => 45
        }

        val throttleUntil = DateTime.now + interval.seconds
        Some(new java.sql.Timestamp(throttleUntil.getMillis))
      }
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
                val failedAttempts = info.failedLoginAttempts + 1
                val throttleUntil = getThrottleTime(failedAttempts)
                loginDAO.updateThrottlingForUser(info.userID, failedAttempts, throttleUntil)
                Ok("no way")
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