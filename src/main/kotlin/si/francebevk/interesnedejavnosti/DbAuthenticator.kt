package si.francebevk.interesnedejavnosti

import com.google.common.util.concurrent.RateLimiter
import org.jooq.DSLContext
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.AccountNotFoundException
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.profile.UserProfile
import org.slf4j.LoggerFactory
import ratpack.handling.Context
import si.francebevk.db.Tables.PUPIL
import si.francebevk.db.enums.ActivityLogType
import si.francebevk.db.tables.records.PupilRecord

/**
 * Authenticates pupils (not admins) via their secret code.
 */
class DbAuthenticator(private val jooq: DSLContext) : Authenticator<UsernamePasswordCredentials> {


    companion object {
        const val PUPIL_NAME = "PUPIL_NAME"
        const val PUPIL_CLASS = "PUPIL_CLASS"

        /** Don't allow password attempts to be attempted too often - that way a password guesser can DDOS the server but cannot guess the password */
        private val ATTEMPT_LIMITER = RateLimiter.create(10.0)

        private val LOG = LoggerFactory.getLogger(DbAuthenticator::class.java)
    }

    override fun validate(credentials: UsernamePasswordCredentials, c: WebContext) = with (PUPIL) {
        LOG.trace("Validating code {}", credentials.password)
        ATTEMPT_LIMITER.acquire(1)
        val user = PupilDAO.getPupilByCode(credentials.password.toLowerCase().trim(), jooq)
        when {
            user == null -> throw AccountNotFoundException("Unknown user")
            else         -> {
                ActivityLogDAO.insertEvent(ActivityLogType.login, user.id.toLong(), false, null, jooq)
                credentials.userProfile = createUserProfile(user)
            }
        }
    }
}

fun createUserProfile(user: PupilRecord) = CommonProfile().also {
    it.setId(user.id.toString())
    it.addAttribute(DbAuthenticator.PUPIL_NAME, "${user.firstName} ${user.lastName}")
    it.addAttribute(DbAuthenticator.PUPIL_CLASS, user.pupilGroup)
}

val Context.user get() = get(UserProfile::class.java) as CommonProfile