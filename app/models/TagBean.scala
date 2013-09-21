package models

import anorm.Pk

case class TagBean(id: Pk[Long], name: String)

object TagBean {

}