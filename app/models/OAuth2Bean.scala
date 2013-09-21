package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class OAuth2Bean(accessToken: String, tokenType: Option[String], expiresIn: Option[Int], refreshToken: Option[String])

object OAuth2Bean {
  val simple = {
    get[Pk[Long]]("oauth2_info.id_user") ~
      get[String]("oauth2_info.access_token") ~
      get[Option[String]]("oauth2_info.token_type") ~
      get[Option[Int]]("oauth2_info.expires_in") ~
      get[Option[String]]("oauth2_info.refresh_token") map {
        case _ ~ accessToken ~ tokenType ~ expiresIn ~ refreshToken => OAuth2Bean(accessToken, tokenType, expiresIn, refreshToken)
      }
  }

  def findById(id: Pk[Long]): Option[OAuth2Bean] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT id_user, access_token, token_type, expires_in, refresh_token FROM oauth2_info WHERE id_user = {id_user}").on('id_user -> id).as(OAuth2Bean.simple.singleOpt)
    }
  }
}