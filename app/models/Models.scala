package models


case class UserAuthResponse(userID: java.util.UUID,
                            emailAddress: String,
                            password: String,
                            active: Boolean,
                            realName: String,
                            lastLogin: Option[java.sql.Timestamp],
                            failedLoginAttempts: Int,
                            loginDisallowedUntil: Option[java.sql.Timestamp],
                            isPrimaryEmailAddress: Boolean,
                            emailAddressVerified: Boolean)