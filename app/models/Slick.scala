package models.slick


import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import models._




object schema {

  object LoginByEmailAddress extends Table[(java.util.UUID, String, String,
                                            Boolean, Option[String], Option[java.sql.Timestamp],
                                            Int, Option[java.sql.Timestamp], Boolean, Boolean)]("login_by_email_address") {
    def user_id = column[java.util.UUID]("user_id")
    def email_address = column[String]("email_address")
    def password = column[String]("password")
    def active = column[Boolean]("active")
    def real_name = column[Option[String]]("real_name")
    def last_login = column[Option[java.sql.Timestamp]]("last_login")
    def failed_login_attempts = column[Int]("failed_login_attempts")
    def login_disallowed_until = column[Option[java.sql.Timestamp]]("login_disallowed_until")
    def is_primary_email_address = column[Boolean]("is_primary_email_address")
    def email_address_verified = column[Boolean]("email_address_verified")

    def * = user_id ~ email_address ~ password ~ active ~
            real_name ~ last_login ~ failed_login_attempts ~
            login_disallowed_until ~ is_primary_email_address ~ email_address_verified
  }

}


object SlickLoginDAO {

  import schema._

  def updateThrottlingForUser(userID: java.util.UUID,
                              failedAttempts: Int,
                              loginDisallowedUntil: Option[java.sql.Timestamp]) = DB.withSession { implicit c =>
    val q = for {
      user <- LoginByEmailAddress if user.user_id is userID
    } yield user.failed_login_attempts ~ user.login_disallowed_until

    q.update((failedAttempts, loginDisallowedUntil))

  }

  def updateSuccessForUser(userID: java.util.UUID,
                           loginTime: Option[java.sql.Timestamp]) = DB.withSession { implicit c =>
    val q = for {
      user <- LoginByEmailAddress if user.user_id is userID
    } yield user.last_login ~ user.failed_login_attempts ~ user.login_disallowed_until

    q.update((loginTime, 0, None))

  }


  private val emailQuery = for {
    email <- Parameters[String]
    user <- LoginByEmailAddress if user.email_address === email
  } yield (user.user_id, user.password, user.active, user.real_name,
           user.failed_login_attempts, user.login_disallowed_until,
           user.is_primary_email_address, user.email_address_verified)

  def userLoginByEmailAddress(emailAddress: String): Option[UserAuthResponse] = DB.withSession { implicit c =>
    val normalizedEmail = emailAddress.toLowerCase

    emailQuery(normalizedEmail).firstOption.map { row =>
      UserAuthResponse(row._1, normalizedEmail, row._2, row._3,
                       row._4, None, row._5, row._6, row._7, row._8)

    }
  }



}


