package models

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db._
import securesocial.core.Identity
import securesocial.core._
import securesocial.core.SocialUser
import securesocial.core.PasswordInfo
import securesocial.core.AuthenticationMethod
import securesocial.core.OAuth1Info
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo
import securesocial.core.OAuth1Info
import securesocial.core.OAuth1Info
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.Json

case class TagBean(id: Pk[Long], name: String)
case class TagWeightBean(name: String, weight: Long)

object TagWeightBean {
  val simpleWeighted = {
    get[String]("tag.name") ~
      get[Long]("weight") map {
        case name ~ weight => TagWeightBean(name, weight)
      }
  }

  implicit val tagWeighdWrites = new Writes[TagWeightBean] {
    def writes(c: TagWeightBean): JsValue = {
      Json.obj("name" -> c.name, "weight" -> c.weight)
    }
  }

  def findForUser(userId: String, providerId: String): List[TagWeightBean] = {
    DB.withConnection { implicit connection =>
      SQL("""
					SELECT tag.name, count( * ) AS weight
					FROM tag
					INNER JOIN bookmark_tag ON tag.id = bookmark_tag.tag_id
					INNER JOIN bookmark ON bookmark.id = bookmark_tag.bookmark_id
					INNER JOIN user ON bookmark.user_id = user.id
					WHERE user.user_id = {userId}
					AND user.provider_id = {providerId}
					GROUP BY tag.name
					""").on('userId -> userId, 'providerId -> providerId).as(TagWeightBean.simpleWeighted *)
    }
  }
}
object TagBean {

  implicit val tagWrites = new Writes[TagBean] {
    def writes(c: TagBean): JsValue = {

      def n = c.id match {
        case Id(refererId) => refererId.toString
        case _ => ""
      }
      Json.obj("id" -> n, "name" -> c.name)
    }
  }

  val simple = {
    get[Pk[Long]]("tag.id") ~
      get[String]("tag.name") map {
        case id ~ name => TagBean(id, name)
      }
  }

  def findByBookmarkId(bookmarkId: Pk[Long]): List[TagBean] = {
    DB.withConnection { implicit connection =>
      SQL("""
    		SELECT tag.id, tag.name
			FROM tag
			INNER JOIN bookmark_tag ON tag.id = bookmark_tag.tag_id
			WHERE bookmark_tag.bookmark_id = {bookmarkId}
          """).on('bookmarkId -> bookmarkId).as(TagBean.simple *)
    }
  }

  def findDistinctForUser(userId: String, providerId: String): List[TagBean] = {
    DB.withConnection { implicit connection =>
      SQL("""
    		SELECT DISTINCT tag.id, tag.name
			FROM tag
            INNER JOIN bookmark_tag ON tag.id = bookmark_tag.tag_id
            INNER JOIN bookmark ON bookmark.id = bookmark_tag.bookmark_id
			INNER JOIN user ON bookmark.user_id = user.id
			WHERE user.user_id = {userId}
			AND user.provider_id = {providerId}
          """).on('userId -> userId, 'providerId -> providerId).as(TagBean.simple *)
    }
  }
}