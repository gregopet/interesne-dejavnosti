package si.francebevk.interesnedejavnosti

import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.enums.DayOfWeek
import si.francebevk.dto.Activity
import si.francebevk.dto.TimeSlot
import si.francebevk.ratpack.jooq
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

    /** Lists all selected activities */
    private fun activities(ctx: Context) {
        val klass = ctx.user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String
        val klassRecord = ClassDAO.getClassByName(klass, ctx.jooq)
        val activities = ActivityDAO.getActivitiesForClass(klassRecord.year, ctx.jooq)
        val selected = ActivityDAO.getSelectedActivityIds(ctx.user.id.toLong(), ctx.jooq)
        val payload = activities.map {
            Activity(it.id, it.name, it.description, it.leader, it.slots.map { slot ->
                TimeSlot(translateDay(slot.day), slot.startMinutes.toInt(), slot.endMinutes.toInt())
            }, selected.contains(it.id))
        }
        ctx.renderJson(payload)
    }

    private fun translateDay(dow: DayOfWeek) = when(dow) {
        DayOfWeek.monday -> "Ponedeljek"
        DayOfWeek.tuesday -> "Torek"
        DayOfWeek.wednesday -> "Sreda"
        DayOfWeek.thursday -> "Četrtek"
        DayOfWeek.friday -> "Petek"
        DayOfWeek.saturday -> "Sobota"
        DayOfWeek.sunday -> "Nedelja"
    }
}