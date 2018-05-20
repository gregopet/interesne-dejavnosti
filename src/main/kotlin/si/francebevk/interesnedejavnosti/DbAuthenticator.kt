package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import org.pac4j.http.credentials.UsernamePasswordCredentials
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator
import org.pac4j.core.exception.AccountNotFoundException
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.profile.UserProfile
import org.pac4j.http.profile.HttpProfile
import org.slf4j.LoggerFactory
import ratpack.handling.Context
import si.francebevk.db.Tables.PUPIL

class DbAuthenticator(private val jooq: DSLContext) : UsernamePasswordAuthenticator {


    companion object {
        const val PUPIL_NAME = "PUPIL_NAME"
        const val PUPIL_CLASS = "PUPIL_CLASS"

        private val LOG = LoggerFactory.getLogger(DbAuthenticator::class.java)
    }

    override fun validate(credentials: UsernamePasswordCredentials) = with (PUPIL) {
        LOG.trace("Validating code {}", credentials.password)
        val user = PupilDAO.getPupilByCode(credentials.password, jooq)
        when {
            user == null                                              -> throw AccountNotFoundException("Unknown user")
            else                                                      -> {
                credentials.userProfile = HttpProfile().also {
                    it.setId(user.id.toString())
                    it.addAttribute(PUPIL_NAME, user.name)
                    it.addAttribute(PUPIL_CLASS, user.pupilGroup)
                }
            }
        }
    }
}

val Context.user get() = get(UserProfile::class.java) as HttpProfile