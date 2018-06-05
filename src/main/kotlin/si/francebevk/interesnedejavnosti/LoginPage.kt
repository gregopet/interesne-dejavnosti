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

        val beforeStart = if (MainPage.isBeforeStart) MainPage.formattedStartDate else null
        val beforeStartHour = if (MainPage.isBeforeStart) MainPage.formattedStartTime else null
        val afterEnd = if (MainPage.isAfterEnd) MainPage.formattedEndDate else null
        val afterEndHour = if (MainPage.isAfterEnd) MainPage.formattedEndTime else null

        ctx.render(Login.template(message, beforeStart, beforeStartHour, afterEnd, afterEndHour))
    }
}