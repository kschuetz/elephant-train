package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import play.api.libs.iteratee._

trait Secured extends Controller {


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