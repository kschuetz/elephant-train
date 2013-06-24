package core

import models.UserAuthResponse
import models.slick

object ThrottledLoginManager {

  val loginDAO = SlickLoginDAO

  case class LoginError(msg: String)

  def userLogin(emailAddress: String, password: String): Either[LoginError, UserAuthResponse] = ???

}