package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import org.pac4j.http.credentials.UsernamePasswordCredentials
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator
import org.pac4j.core.exception.AccountNotFoundException
import org.pac4j.core.profile.UserProfile
import org.pac4j.http.profile.HttpProfile
import org.slf4j.LoggerFactory
import ratpack.handling.Context
import si.francebevk.db.Tables.PUPIL

class DbAuthenticator(private val jooq: DSLContext) : UsernamePasswordAuthenticator {

    companion object {
        private val LOG = LoggerFactory.getLogger(DbAuthenticator::class.java)
    }

    override fun validate(credentials: UsernamePasswordCredentials) = with (PUPIL) {
        LOG.trace("Validating code {}", credentials.password)
        val user =
            jooq
            .selectFrom(PUPIL)
            .where(ACCESS_CODE.eq(credentials.password))
            .fetchOne()
        when {
            user == null                                              -> throw AccountNotFoundException("Unknown user")
            else                                                      -> {
                credentials.userProfile = HttpProfile().also {
                    it.setId(user.id.toString())
                }
            }
        }
    }
}

val Context.user get() = get(UserProfile::class.java) as HttpProfile