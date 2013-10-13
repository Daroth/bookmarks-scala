package models

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db._
import securesocial.core.Identity
import securesocial.core._
import securesocial.core.SocialUser
import securesocial.core.PasswordInfo
import securesocial.core.AuthenticationMethod
import securesocial.core.OAuth1Info
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo
import securesocial.core.OAuth1Info
import securesocial.core.OAuth1Info

case class UserBean(idPk: Pk[Long], id: UserId, firstName: String, lastName: String, fullName: String, email: Option[String],
  avatarUrl: Option[String], authMethod: AuthenticationMethod,
  oAuth1Info: Option[OAuth1Info] = None,
  oAuth2Info: Option[OAuth2Info] = None,
  passwordInfo: Option[PasswordInfo] = None) extends Identity

object UserBean {

  val simple = {
    get[Pk[Long]]("user.id") ~
      get[String]("user.user_id") ~
      get[String]("user.provider_id") ~
      get[String]("user.first_name") ~
      get[String]("user.last_name") ~
      get[String]("user.full_name") ~
      get[Option[String]]("user.email") ~
      get[Option[String]]("user.avatar_url") ~
      get[String]("user.auth_method") ~
      get[String]("user.hasher") ~
      get[String]("user.password") ~
      get[Option[String]]("user.salt") map {
        case id ~ userId ~ providerId ~ firstName ~ lastName ~ fullName ~ email ~ avatarUrl ~ authMethod ~ hasher ~ password ~ salt => {
          val aAuthMethod = AuthenticationMethod(authMethod)
          val oauth1 = aAuthMethod match {
            case AuthenticationMethod.OAuth1 => {
              OAuth1Bean.findById(id) match {
                case Some(x) => Some(OAuth1Info(x.token, x.secret))
                case _ => None

              }
            }
            case _ => None
          }
          val oauth2 = aAuthMethod match {
            case AuthenticationMethod.OAuth1 => {
              OAuth2Bean.findById(id) match {
                case Some(x) => Some(OAuth2Info(x.accessToken, x.tokenType, x.expiresIn, x.refreshToken))
                case _ => None

              }
            }
            case _ => None
          }
          UserBean(id, UserId(userId, providerId), firstName, lastName, fullName, email, avatarUrl, aAuthMethod, oauth1, oauth2, Some(PasswordInfo(hasher, password, salt)))
        }
      }
  }

  def save(user: Identity): UserBean = {
    val hasher = user.passwordInfo match {
      case Some(x) => x.hasher
      case _ => None
    }
    val password = user.passwordInfo match {
      case Some(x) => x.password
      case _ => None
    }
    val salt = user.passwordInfo match {
      case Some(x) => x.salt
      case _ => None
    }

    DB.withConnection { implicit connection =>
      val userById = findByUserId(user.id.id)
      userById match {
        case Some(_) =>
          SQL("""
            UPDATE user SET provider_id={provider_id}, first_name={first_name}, last_name={last_name}, full_name={full_name},
        		email={email}, avatar_url={avatar_url}, auth_method={auth_method}, hasher={hasher}, password={password}, salt={salt}
            WHERE user_id={user_id}
            """)
            .on('user_id -> user.id.id, 'provider_id -> user.id.providerId, 'first_name -> user.firstName, 'last_name -> user.lastName, 'full_name -> user.fullName, 'email -> user.email, 'avatar_url -> user.avatarUrl, 'auth_method -> user.authMethod.method, 'hasher -> hasher, 'password -> password, 'salt -> salt)
            .executeUpdate()
          findByUserId(user.id.id) match { case Some(x) => x }
        case _ =>
          val id = SQL("""
        		INSERT INTO user(user_id, provider_id, first_name, last_name, full_name, email, avatar_url, auth_method, hasher, password, salt) VALUES
        		({user_id}, {provider_id}, {first_name}, {last_name}, {full_name}, {email}, {avatar_url}, {auth_method}, {hasher}, {password}, {salt})
        		""")
            .on('user_id -> user.id.id, 'provider_id -> user.id.providerId, 'first_name -> user.firstName, 'last_name -> user.lastName, 'full_name -> user.fullName, 'email -> user.email, 'avatar_url -> user.avatarUrl, 'auth_method -> user.authMethod.method, 'hasher -> hasher, 'password -> password, 'salt -> salt)
            .executeInsert()
          id match {
            case Some(x) => UserBean.findById(x) match { case Some(x) => x }
          }
      }
    }
  }

  def findById(id: Long): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE id = {id}").on(
        'id -> id).as(UserBean.simple.singleOpt)
    }
  }

  def findByUserId(id: String): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE user_id = {id}").on(
        'id -> id).as(UserBean.simple.singleOpt)
    }
  }
  def findByEmailAndProvider(email: String, providerId: String): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE email = {email} and provider_id = {providerId}").on(
        'email -> email, 'providerId -> providerId).as(UserBean.simple.singleOpt)
    }
  }

  def findAll: Seq[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(UserBean.simple *)
    }
  }

  def findByEmail(email: String): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE email = {email}").on(
        'email -> email).as(UserBean.simple.singleOpt)
    }
  }

  def findByIdentity(user: Identity): Option[UserBean] = findByIdentity(user.id.id, user.id.providerId)

  def findByIdentity(userId: String, providerId: String): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user WHERE user_id = {userId} and provider_id = {providerId}").on(
        'userId -> userId, 'providerId -> providerId).as(UserBean.simple.singleOpt)
    }
  }

  def authenticate(email: String, password: String): Option[UserBean] = {
    DB.withConnection { implicit connection =>
      SQL("""
          select * from user 
          where email = {email} 
          and password = {password}
      """).on('email -> email, 'password -> password).as(UserBean.simple.singleOpt)
    }
  }
}