package si.francebevk.interesnedejavnosti

import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
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
    }


    private fun html(ctx: Context) {
        ctx.render(Main.template(ctx.user.getAttribute(DbAuthenticator.PUPIL_NAME)  as String, ctx.user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String))
    }

    private fun activities(ctx: Context) {
        val judo = Activity(
            1, "Judo", "Judo je hudo", "Jožef Kokolj", listOf(
                TimeSlot("Ponedeljek", "15:00", "17:00"),
                TimeSlot("Sreda", "17:00", "18:00")
            ))
        val strik = Activity(
            2, "Štrikanje", "Zaštrikano", "Manica Hudobivnik", listOf(
                TimeSlot("Ponedeljek", "16:00", "17:00"),
                TimeSlot("Četrtek", "14:00", "15:00")
            ))
        val tek = Activity(
                2, "Tek", "Hitro", "Zelo Hitra", listOf(
                TimeSlot("Torek", "16:00", "17:00"),
                TimeSlot("Četrtek", "14:00", "15:00")
        ))

        ctx.renderJson(listOf(judo, strik, tek))
    }
}