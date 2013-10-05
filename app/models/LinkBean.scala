package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class LinkBean(id: Pk[Long], link: String)

object LinkBean {

  val simple = {
    get[Pk[Long]]("link.id") ~
      get[String]("link.link") map {
        case id ~ link => LinkBean(id, link)
      }
  }

  def findLink(link: String) = {
    DB.withConnection { implicit connection =>
      SQL("SELECT id, link FROM link WHERE link = {link}").on('link -> link).as(LinkBean.simple.singleOpt)

    }
  }

  def createOrRetrieve(link: String): LinkBean = {
    DB.withConnection { implicit connection =>
      val linkById = findLink(link)
      linkById match {
        case Some(x) => x
        case _ =>
          val id = SQL("INSERT INTO link(link) VALUES ({link})")
            .on('link -> link)
            .executeInsert()
          findLink(link) match { case Some(x) => x }
      }
    }
  }
}