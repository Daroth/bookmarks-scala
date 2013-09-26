package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import anorm._
import models._
import views._
import scala.util.parsing.json.JSONObject
import play.api.libs.json.Json
import play.api.libs.json.Json._

object Bookmarks extends Controller with securesocial.core.SecureSocial {
  def index = SecuredAction { implicit request =>

    Ok(views.html.index())
  }

  def tags = SecuredAction { request =>
    val tagsList = TagBean.findForUser(request.user.identityId.userId, request.user.identityId.providerId)
    Ok(Json.toJson(tagsList map { tag => Json.obj(tag.name -> tag.weight) }))
  }
}

