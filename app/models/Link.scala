package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Link(id:Pk[Long], link:String)

object Link {

}