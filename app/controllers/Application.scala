package controllers

import play.api._
import play.api.mvc._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import securesocial.controllers.routes._

import models._
import views._

object Application extends Controller {

  val loginForm = Form(
    tuple(
      "mail" -> text,
      "password" -> text) verifying ("Invalid email or password", result => result match {
        case (mail, password) => UserBean.authenticate(mail, password).isDefined
      }))
}
