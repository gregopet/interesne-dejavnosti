package si.francebevk.interesnedejavnosti

import login.Login
import ratpack.handling.Context
import ratpack.handling.Handler

object LoginPage : Handler {

    override fun handle(ctx: Context) {
        ctx.byMethod { m ->
            m.get { ctx -> renderLoginForm(null, ctx) }
        }
    }

    /**
     * Render a login form, optionally displaying the given error [message].
     */
    fun renderLoginForm(message: String?, ctx: Context) {
        ctx.render(Login.template(message))
    }
}