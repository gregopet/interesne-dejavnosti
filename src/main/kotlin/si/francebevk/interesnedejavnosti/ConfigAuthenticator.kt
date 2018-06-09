package si.francebevk.interesnedejavnosti

import com.google.common.util.concurrent.RateLimiter
import org.pac4j.core.exception.BadCredentialsException
import org.pac4j.http.credentials.UsernamePasswordCredentials
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator
import org.pac4j.http.profile.HttpProfile
import java.security.MessageDigest
import java.util.*

/**
 * A simple authenticator that is configured with usernames & passwords manually.
 */
object ConfigAuthenticator : UsernamePasswordAuthenticator {

    const val USERNAME = "šola"
    val PASSWORD = "včeraj zeleno pošta zvoni".md5


    /** Don't allow password attempts to be attempted too often - that way a password guesser can DDOS the server but cannot guess the password */
    private val ATTEMPT_LIMITER = RateLimiter.create(1.0)

    override fun validate(credentials: UsernamePasswordCredentials) {
        if (
            credentials.username != USERNAME ||
            credentials.password == null ||
            !Arrays.equals(credentials.password.md5, PASSWORD)
        ) {
            throw BadCredentialsException("Bad credentials")
       } else {
            credentials.userProfile = HttpProfile()
        }
    }

    private val String.md5 get() = MessageDigest.getInstance("MD5").digest(this.toByteArray())
}