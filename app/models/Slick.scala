package models.slick


import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import models._

object schema {

  object LoginByEmailAddress extends Table[(java.util.UUID, String, String,
                                            Boolean, Option[String], java.sql.Timestamp,
                                            Int, java.sql.Timestamp, Boolean, Boolean)]("login_by_email_address") {
    def user_id = column[java.util.UUID]("user_id")
    def email_address = column[String]("email_address")
    def password = column[String]("password")
    def active = column[Boolean]("active")
    def real_name = column[Option[String]]("real_name")
    def last_login = column[java.sql.Timestamp]("last_login")
    def failed_login_attempts = column[Int]("failed_login_attempts")
    def login_disallowed_until = column[java.sql.Timestamp]("login_disallowed_until")
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
                              loginDisallowedUntil: Option[java.sql.Timestamp]) = ???

  def updateSuccessForUser(userID: java.util.UUID,
                           loginTime: Option[java.sql.Timestamp]) = ???

}
