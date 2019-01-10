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

        val deadlines = ctx.get(Deadlines::class.java)
        val beforeStart = if (deadlines.isBeforeStart) deadlines.startDateString else null
        val beforeStartHour = if (deadlines.isBeforeStart) deadlines.startTimeString else null
        val afterEnd = if (deadlines.isAfterEnd) deadlines.endDateString else null
        val afterEndHour = if (deadlines.isAfterEnd) deadlines.endTimeString else null

        ctx.render(Login.template(message, beforeStart, beforeStartHour, afterEnd, afterEndHour))
    }
}