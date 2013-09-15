package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Bookmarks extends Controller with Secured {
  def index = IsAuthenticated { email => _ =>
      User.findByEmail(email).map { user =>
        Ok(html.index())
      }.getOrElse(Forbidden)

  }
}

