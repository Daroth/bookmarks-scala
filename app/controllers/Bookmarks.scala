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

  private def userId = "a@a.a" // request.user.id.id
  private def providerId = "userpass" // request.user.id.providerId

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def bookmarksList = Action { implicit request =>
    Ok(views.html.bookmarks.bookmarks_list())
  }

  def bookmarksEdit = Action { implicit request =>
    Ok(views.html.bookmarks.bookmarks_edit())
  }

  def tags = Action { request =>
    val tagsList = TagWeightBean.findForUser(userId, providerId)
    Ok(Json.toJson(tagsList))
  }

  def getBookmarks = Action { request =>
    //    var bookmarksList = BookmarkBeanWithTags.findForUser(request.user.id.id, request.user.id.providerId)
    val bookmarksList = BookmarkBeanWithTags.findForUser(userId, providerId)
    Ok(Json.toJson(bookmarksList))
  }

  def getBookmark(bookmarkId: Long) = Action { request =>
    val bookmarksList = BookmarkBeanWithTags.findByIdForUser(bookmarkId, userId, providerId)
    Ok(Json.toJson(bookmarksList))
  }

  //  def saveBookmark = Action { request =>
  //    request.body.asJson.map { json =>
  //      json.validate[(String, String, String, String)].map {
  //        case (link, title, tags, description) => {
  //          BookmarkBean.save(link, title, tags, description, request.user)
  //          Ok(Json.obj("status" -> "OK"))
  //        }
  //      }.recoverTotal {
  //        e => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(e)))
  //      }
  //    }.getOrElse {
  //      BadRequest("Expecting Json data")
  //    }
  //  }
}