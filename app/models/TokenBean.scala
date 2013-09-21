package models

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db._
import org.joda.time.DateTime
import java.util.Date

import securesocial.core.providers.Token

object TokenBean {
  val simple = {
    get[Pk[Long]]("token.id") ~
      get[String]("token.uuid") ~
      get[String]("token.email") ~
      get[Date]("token.creation_time") ~
      get[Date]("token.expiration_time") ~
      get[Boolean]("token.is_sign_up") map {
        case _ ~ uuid ~ email ~ creationTime ~ expirationTime ~ isSignUp => Token(uuid, email, new DateTime(creationTime), new DateTime(expirationTime), isSignUp)
      }
  }

  def selectAll(): List[Token] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT id, uuid, email, creation_time, expiration_time, is_sign_up FROM token").as(TokenBean.simple *)
    }
  }

  def getByToken(token: String): Option[Token] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT id, uuid, email, creation_time, expiration_time, is_sign_up FROM token WHERE uuid = {token}").on(
        'token -> token).as(TokenBean.simple.singleOpt)
    }
  }

  def save(token: Token) = {
    DB.withConnection { implicit connection =>
      val id = SQL("""
            INSERT INTO token(uuid, email, creation_time, expiration_time, is_sign_up)
    		VALUES ({uuid}, {email}, {creation_time}, {expiration_time}, {is_sign_up})
            """)
        .on('uuid -> token.uuid, 'email -> token.email, 'creation_time -> token.creationTime.toDate(), 'expiration_time -> token.expirationTime.toDate(), 'is_sign_up -> token.isSignUp)
        .executeInsert()
    }
  }

  def deleteToken(uuid: String) = {
    DB.withConnection { implicit connection =>
      val id = SQL("DELETE FROM token WHERE uuid = {uuid}")
        .on('uuid -> uuid)
        .executeUpdate()
    }
  }

  def deleteExpiredTokens() = {
    selectAll map { x =>
      if (x.isExpired) {
        DB.withConnection { implicit connection =>
          val id = SQL("DELETE FROM token WHERE uuid = {uuid}").on('uuid -> x.uuid)
            .executeUpdate()
        }
      }
    }
  }
}