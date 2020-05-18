package si.francebevk.interesnedejavnosti

import com.google.common.util.concurrent.RateLimiter
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.BadCredentialsException
import org.pac4j.core.credentials.UsernamePasswordCredentials
import java.security.MessageDigest
import java.util.*

/**
 * A simple authenticator that is configured with usernames & passwords manually.
 */
class ConfigAuthenticator(private val username: String, password: String) : Authenticator<UsernamePasswordCredentials> {

    val PASSWORD = password.md5


    /** Don't allow password attempts to be attempted too often - that way a password guesser can DDOS the server but cannot guess the password */
    private val ATTEMPT_LIMITER = RateLimiter.create(1.0)

    override fun validate(credentials: UsernamePasswordCredentials, c: WebContext) {
        ATTEMPT_LIMITER.acquire(1)
        if (
            credentials.username != username ||
            credentials.password == null ||
            !Arrays.equals(credentials.password.md5, PASSWORD)
        ) {
            throw BadCredentialsException("Bad credentials")
       }
    }

    private val String.md5 get() = MessageDigest.getInstance("MD5").digest(this.toByteArray())
}