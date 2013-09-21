import play.api._

import models._
import anorm._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    InitialData.insert()
  }
}

object InitialData {
  def insert() = {
//    if (User.findAll.isEmpty) {
//      Seq(User(NotAssigned, "manuel.leduc@sopragroup.com", "sopra123")).foreach(User.create)
//      Seq(User(NotAssigned, "manuel.leduc@gmail.com", "azerty")).foreach(User.create)
//    }
  }
}