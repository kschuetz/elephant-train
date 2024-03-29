package models


case class UserAuthResponse(userID: java.util.UUID,
                            emailAddress: String,
                            password: String,
                            active: Boolean,
                            realName: Option[String],
                            lastLogin: Option[java.sql.Timestamp],
                            failedLoginAttempts: Int,
                            loginDisallowedUntil: Option[java.sql.Timestamp],
                            isPrimaryEmailAddress: Boolean,
                            emailAddressVerified: Boolean)


case class AuthenticatedUser(userID: java.util.UUID,
                             persistentLogin: Boolean,
                             token: String)