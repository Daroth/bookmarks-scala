package controllers

import play.api._
import play.api.mvc._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Application extends Controller {

  val loginForm = Form(
    tuple(
      "mail" -> text,
      "password" -> text) verifying ("Invalid email or password", result => result match {
        case (mail, password) => User.authenticate(mail, password).isDefined
      }))

  def login = Action {
    Ok(html.login(loginForm))
  }

  def authenticate = Action { implicit request =>

    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Bookmarks.index).withSession("email" -> user._1))
  }

}

trait Secured {

  private def email(request: RequestHeader) = request.session.get("email")

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(email, onUnauthorized) { user =>
    Logger(this.getClass).error("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    Action(request => f(user)(request))
  }
}