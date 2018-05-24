package si.francebevk.interesnedejavnosti

import Login
import org.slf4j.LoggerFactory
import ratpack.handling.Context
import ratpack.handling.Handler

object LoginPage : Handler {

    val LOG = LoggerFactory.getLogger(LoginPage.javaClass)


    override fun handle(ctx: Context) {
        ctx.byMethod { m ->
            m.get { ctx -> renderLoginForm(ctx) }
        }
    }

    /**
     * Render a login form, optionally displaying the given error [message].
     */
    fun renderLoginForm(ctx: Context) {
        val message = when(ctx.request.queryParams["error"]) {
            null                       -> null
            "AccountNotFoundException" -> "Koda, ki ste jo vnesli, je napačna!"
            else                       -> "Napaka: ${ctx.request.queryParams["error"]}".also {
                LOG.error("Authentication error ${ctx.request.queryParams["error"]}")
            }
        }
        ctx.render(Login.template(message))
    }
}