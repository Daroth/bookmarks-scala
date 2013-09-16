package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Bookmarks extends Controller with securesocial.core.SecureSocial {
  def index = SecuredAction { implicit request =>

    Ok(views.html.index())
  }
}

