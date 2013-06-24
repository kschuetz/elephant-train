package core

import models.UserAuthResponse

object LoginManager {

  val loginDAO = models.slick.SlickLoginDAO

  case class LoginError(msg: String)

  def userLogin(emailAddress: String, password: String): Either[LoginError, UserAuthResponse] = ???

}