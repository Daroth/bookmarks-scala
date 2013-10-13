package models

import java.util.Date
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.Id
import anorm.SqlParser._
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import securesocial.core.Identity

case class BookmarkBean(id: Pk[Long], title: String, date: Date, description: String, userId: Long, linkId: Long, link: LinkBean)
case class BookmarkBeanWithTags(id: Pk[Long], title: String, date: Date, description: String, userId: Long, linkId: Long, link: LinkBean, tags: List[TagBean])

object BookmarkBeanWithTags {
  implicit val bookmarkWithTagsWrites = new Writes[BookmarkBeanWithTags] {
    def writes(c: BookmarkBeanWithTags): JsValue = {

      def n = c.id match {
        case Id(refererId) => refererId.toString
        case _ => ""
      }

      Json.obj(
        "id" -> n,
        "link" -> c.link.link,
        "title" -> c.title,
        "description" -> c.description,
        "date" -> c.date,
        "tags" -> c.tags)
    }
  }

  private def addTagsToBookmark(b: BookmarkBean) = BookmarkBeanWithTags(b.id, b.title, b.date, b.description, b.userId, b.linkId, b.link, TagBean.findByBookmarkId(b.id))

  def findForUser(userId: String, providerId: String): List[BookmarkBeanWithTags] = {
    DB.withConnection { implicit connection =>
      SQL("""
    		SELECT bookmark.id, bookmark.title, bookmark.date, bookmark.description, bookmark.user_id, bookmark.link_id, link.link
			FROM bookmark
    		INNER JOIN link ON bookmark.link_id = link.id
			INNER JOIN user ON bookmark.user_id = user.id
			WHERE user.user_id = {userId}
			AND user.provider_id = {providerId}
          """).on('userId -> userId, 'providerId -> providerId).as(BookmarkBean.simple *) map addTagsToBookmark
    }
  }

  def findByIdForUser(bookmarkId: Long, userId: String, providerId: String): BookmarkBeanWithTags = {
    DB.withConnection { implicit connection =>
      val b = SQL("""
				  SELECT bookmark.id, bookmark.title, bookmark.date, bookmark.description, bookmark.user_id, bookmark.link_id, link.link
				  FROM bookmark
				  INNER JOIN link ON bookmark.link_id = link.id
				  INNER JOIN user ON bookmark.user_id = user.id
				  WHERE bookmark.id = {bookmarkId} 
		          AND  user.user_id = {userId}
				  AND user.provider_id = {providerId}
				  """).on('bookmarkId -> bookmarkId, 'userId -> userId, 'providerId -> providerId).as(BookmarkBean.simple.singleOpt)
      b match { case Some(x) => addTagsToBookmark(x) }
    }

  }
}

object BookmarkBean {

  implicit val bookmarkWrites = new Writes[BookmarkBean] {
    def writes(c: BookmarkBean): JsValue = {

      def n = c.id match {
        case Id(refererId) => refererId.toString
        case _ => ""
      }
      Json.obj(
        "id" -> n,
        "link" -> c.link.link,
        "title" -> c.title,
        "description" -> c.description,
        "date" -> c.date)
    }
  }

  val simple = {
    get[Pk[Long]]("bookmark.id") ~
      get[String]("bookmark.title") ~
      get[Date]("bookmark.date") ~
      get[String]("bookmark.description") ~
      get[Long]("bookmark.user_id") ~
      get[Long]("bookmark.link_id") ~
      get[String]("link.link") map {
        case id ~ title ~ date ~ description ~ userId ~ linkId ~ link => BookmarkBean(id, title, date, description, userId, linkId, LinkBean(Id(linkId), link))
      }
  }

  def findById(id: Long): Option[BookmarkBean] = {
    DB.withConnection { implicit connection =>
      SQL("select bookmark.`id`, bookmark.title, bookmark.`date`, bookmark.`description`, bookmark.`user_id`, bookmark.`link_id`, link.link from bookmark inner join link in link.id = bookmark.link_id where id = {id}").on('id -> id).as(BookmarkBean.simple.singleOpt)
    }
  }

  def save(link: String, title: String, tags: String, description: String, identity: Identity) {
    val user = UserBean.findByIdentity(identity)
    user match {
      case Some(x) => {
        val linkBean = LinkBean.createOrRetrieve(link)

        DB.withConnection { implicit connection =>
          SQL("""
        INSERT INTO bookmark (`title`, `description`, `user_id`, `link_id`)
        VALUES ({title}, {description}, {user_id}, {link_id})
        """)
            .on('title -> title, 'description -> description, 'user_id -> x.id, 'link_id -> linkBean.id)
            .executeInsert()
        }
      }
      case _ => throw new NoSuchElementException
    }
  }
}