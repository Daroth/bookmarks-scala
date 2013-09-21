package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class OAuth1Bean(token: String, secret: String)

object OAuth1Bean {
  val simple = {
    get[Pk[Long]]("oauth1_info.id_user") ~
      get[String]("oauth1_info.token") ~
      get[String]("oauth1_info.secret") map {
        case _ ~ token ~ secret => OAuth1Bean(token, secret)
      }
  }

  def findById(id: Pk[Long]): Option[OAuth1Bean] = {
    DB.withConnection { implicit connection =>
      SQL("select id_user, token, secret from oauth1_info WHERE id_user = {id_user}").on('id_user -> id).as(OAuth1Bean.simple.singleOpt)
    }
  }
}