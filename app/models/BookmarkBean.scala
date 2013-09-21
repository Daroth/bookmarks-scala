package models

import java.util.Date
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class BookmarkBean(id: Pk[Long], date: Date, description: String, userId: Long, linkId: Long, link:LinkBean)

object BookmarkBean {
  val simple = {
    get[Pk[Long]]("bookmark.id") ~
      get[Date]("bookmark.date") ~
      get[String]("bookmark.description") ~
      get[Long]("bookmark.user_id") ~
      get[Long]("bookmark.link_id") ~
      get[String]("link.link") map {
        case id ~ date ~ description ~ userId ~ linkId ~ link => BookmarkBean(id, date, description, userId, linkId, LinkBean(Id(linkId), link))
      }
  }

  def findById(id: Long): Option[BookmarkBean] = {
    DB.withConnection { implicit connection =>
      SQL("select bookmark.`id`, bookmark.`date`, bookmark.`description`, bookmark.`user_id`, bookmark.`link_id`, link.link from bookmark inner join link in link.id = bookmark.link_id where id = {id}").on('id -> id).as(BookmarkBean.simple.singleOpt)
    }
  }
}