package models

import anorm.Pk

case class Tag(id: Pk[Long], name: String)

object Tag {

}