package si.francebevk.interesnedejavnosti

import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import ratpack.pac4j.internal.Pac4jAuthenticator
import si.francebevk.dto.Activity
import si.francebevk.dto.TimeSlot
import si.francebevk.ratpack.renderJson
import si.francebevk.ratpack.route

/**
 * The main activity setting page.
 */
object MainPage : Action<Chain> {

    override fun execute(t: Chain) = t.route {
        get { html(it) }
        get("activities") { activities(it) }
        get("finish") { endHtml(it) }
    }


    private fun html(ctx: Context) {
        ctx.render(Main.template(ctx.user.getAttribute(DbAuthenticator.PUPIL_NAME)  as String, ctx.user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String))
    }

    private fun endHtml(ctx: Context) {
        RatpackPac4j.logout(ctx).then {
            ctx.render(Finish.template())
        }
    }

    private fun activities(ctx: Context) {
        val judo = Activity(
            1, "Judo", "Judo je hudo", "Jožef Kokolj", listOf(
                TimeSlot("Ponedeljek", 900, 1020),
                TimeSlot("Sreda", 840, 1020)
            ))
        val strik = Activity(
            2, "Štrikanje", "Zaštrikano", "Manica Hudobivnik", listOf(
                TimeSlot("Ponedeljek", 960, 1020),
                TimeSlot("Četrtek", 840, 900)
            ))
        val tek = Activity(
                3, "Tek", "Hitro", "Zelo Hitra", listOf(
                TimeSlot("Torek", 960, 1019),
                TimeSlot("Četrtek", 840, 900)
        ))
        val drsanje = Activity(
            4, "Drsanje", "Z nami ne boste nadrsali", "Ledenko Drsič", listOf(
            TimeSlot("Ponedeljek", 840, 900)
        ))

        ctx.renderJson(listOf(judo, strik, tek, drsanje))
    }
}