package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import play.api.libs.iteratee._

trait Secured extends Controller {

  import models._

  private val dummyUserID = java.util.UUID.fromString("12345678-90ab-cdef-1234-56789abcdef1")

  private def getAuthenticatedUser(request: RequestHeader) =
    Some(AuthenticatedUser(dummyUserID))

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def Authenticated[A](userinfo: RequestHeader => Option[A])
                      (action: A => EssentialAction): EssentialAction = {

    EssentialAction { request =>
      userinfo(request).map { user =>
        action(user)(request)
      }.getOrElse {
        Done(onUnauthorized(request), Input.Empty)
      }
    }

  }
}