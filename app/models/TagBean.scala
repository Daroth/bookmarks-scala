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
import securesocial.core.IdentityId
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo
import securesocial.core.OAuth1Info
import securesocial.core.OAuth1Info

case class TagBean(id: Pk[Long], name: String)
case class TagWeightBean(name: String, weight: Long)

object TagBean {

  val simpleWeighted = {
    get[String]("tag.name") ~
      get[Long]("weight") map {
        case name ~ weight => TagWeightBean(name, weight)
      }

  }

  def findForUser(userId: String, providerId: String): Seq[TagWeightBean] = {
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
          """).on('userId -> userId, 'providerId -> providerId).as(TagBean.simpleWeighted *)
    }
  }
}