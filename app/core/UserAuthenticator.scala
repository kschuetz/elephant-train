package core

import models.UserAuthResponse

object UserAuthenticator {

  val loginDAO = models.slick.SlickLoginDAO

  sealed abstract class LoginError
  case object InvalidCredentials extends LoginError
  case object TooManyAttempts extends LoginError

  import com.github.nscala_time.time.Imports._

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

  def authenticate(emailAddress: String, plainPassword: String): Either[LoginError, UserAuthResponse] = {

    val now = DateTime.now
    val loginTime = new java.sql.Timestamp(now.getMillis)

    def isThrottled(info: UserAuthResponse): Boolean = {
      info.loginDisallowedUntil.map { loginTime.before(_) } getOrElse(false)
    }

    val authInfo = loginDAO.userLoginByEmailAddress(emailAddress)

    authInfo match {
      case Some(info) => {
        if(isThrottled(info)) Left(TooManyAttempts)
        else {
          if(checkPassword(plainPassword, info.password)) {
            loginDAO.updateSuccessForUser(info.userID, Some(loginTime))
            Right(info)
          } else {
            val failedAttempts = info.failedLoginAttempts + 1
            val throttleUntil = getThrottleTime(failedAttempts)
            loginDAO.updateThrottlingForUser(info.userID, failedAttempts, throttleUntil)
            Left(InvalidCredentials)
          }
        }
      }
      case _ => Left(InvalidCredentials)
    }

  }

}