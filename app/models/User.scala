package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(id: Pk[Long], mail: String, password: String)

object User {
  val simple = {
    get[Pk[Long]]("user.id") ~
      get[String]("user.mail") ~
      get[String]("user.password") map {
        case id ~ email ~ password => User(id, email, password)
      }
  }

  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL("insert into user (mail, password) values ({mail}, {password})")
        .on('mail -> user.mail, 'password -> user.password)
        .executeUpdate()
      user
    }
  }

  def findById(id: Long): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE id = {id}").on(
        'id -> id).as(User.simple.singleOpt)
    }
  }

  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(User.simple *)
    }
  }

  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE mail = {email}").on(
        'email -> email).as(User.simple.singleOpt)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("""
          select * from user 
          where mail = {email} 
          and password = {password}
      """).on('email -> email, 'password -> password).as(User.simple.singleOpt)
    }
  }
}