package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import play.api.libs.iteratee._

trait Secured extends Controller {

  import models._

  private def loginTokenExists(userID: String, token: String): Boolean = true
  private def addLoginToken(userID: String, token: String): Unit = { }
  private def replaceLoginToken(userID: String, oldToken: String, newToken: String): Unit = {}
  private def deleteLoginToken(userID: String, token: String): Unit = { }
  private def deleteAllLoginTokens(userID: String): Unit = {}


  private val dummyUserID = java.util.UUID.fromString("12345678-90ab-cdef-1234-56789abcdef1")

  private def getAuthenticatedUser(request: RequestHeader) =
    Some(AuthenticatedUser(dummyUserID, true, "qwerty"))

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def Authenticated(action: AuthenticatedUser => EssentialAction): EssentialAction = {

    EssentialAction { request =>
      getAuthenticatedUser(request).map { user =>
        action(user)(request)
      }.getOrElse {
        Done(onUnauthorized(request), Input.Empty)
      }
    }

  }
}