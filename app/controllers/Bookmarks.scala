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
import play.api.libs.json._
import play.api.libs.functional.syntax._
import views.html.defaultpages.badRequest
import java.util.Date

object Bookmarks extends Controller with securesocial.core.SecureSocial {

  implicit val bookmark = (__ \ "bookmark").read(
    (__ \ 'link).read[String] and
      (__ \ 'title).read[String] and
      (__ \ 'tags).read[String] and
      (__ \ 'description).read[String] tupled)

  def index = SecuredAction { implicit request =>

    Ok(views.html.index())
  }

  def tags = SecuredAction { request =>
    val tagsList = TagBean.findForUser(request.user.id.id, request.user.id.providerId)
    Ok(Json.toJson(tagsList map { tag => Json.obj(tag.name -> tag.weight) }))
  }

  def getBookmarks = SecuredAction { request =>
    var bookmarksList = BookmarkBeanWithTags.findForUser(request.user.id.id, request.user.id.providerId)
    var tagsList = TagBean.findDistinctForUser(request.user.id.id, request.user.id.providerId)
    Ok(Json.obj("bookmarks" -> Json.toJson(bookmarksList), "tags" -> Json.toJson(tagsList)))
  }

  def validateBookmark = SecuredAction { request =>
    request.body.asJson.map { json =>
      json.validate[(String, String, String, String)].map {
        case (link, title, tags, description) => Ok(Json.obj("status" -> "OK"))
      }.recoverTotal {
        e => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(e)))
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }

  def saveBookmark = SecuredAction { request =>
    request.body.asJson.map { json =>
      json.validate[(String, String, String, String)].map {
        case (link, title, tags, description) => {
          BookmarkBean.save(link, title, tags, description, request.user)
          Ok(Json.obj("status" -> "OK"))
        }
      }.recoverTotal {
        e => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(e)))
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }
}