package si.francebevk.interesnedejavnosti

import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.enums.DayOfWeek
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.db.withTransaction
import si.francebevk.dto.Activity
import si.francebevk.dto.PupilSettings
import si.francebevk.dto.TimeSlot
import si.francebevk.ratpack.*

/**
 * The main activity setting page.
 */
object MainPage : Action<Chain> {

    override fun execute(t: Chain) = t.route {
        get { html(it) }
        get("activities") { activities(it) }
        post("store") { store(it) }
        get("finish") { endHtml(it) }
    }


    private fun html(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        ctx.render(Main.template(pupil.name, pupil.pupilGroup, klass.year < 6))
    }

    private fun endHtml(ctx: Context) {
        RatpackPac4j.logout(ctx).then {
            ctx.render(Finish.template())
        }
    }

    /** Lists all selected activities */
    private fun activities(ctx: Context) = ctx.async {
        val klassRecord = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val activities = await { ActivityDAO.getActivitiesForClass(klassRecord.year, ctx.jooq) }
        val selected = await { ActivityDAO.getSelectedActivityIds(ctx.user.id.toLong(), ctx.jooq) }
        val payload = activities.map { it.toDTO(selected.contains(it.id)) }
        ctx.renderJson(payload)
    }

    /** Store the student's selection */
    private fun store(ctx: Context) = ctx.async {
        val payload = ctx.parse(PupilSettings::class.java).await()
        await {
            ctx.jooq.withTransaction {  t ->
                ActivityDAO.storeSelectedActivityIds(ctx.user.id.toLong(), payload.selectedActivities, t)
            }
        }

        // send confirmation email
        val pupilActivities = await {
            ActivityDAO.getSelectedActivitiesForPupil(ctx.user.id.toLong(), ctx.jooq)
        }.map { it.toDTO(true) }
        await {
            EmailDispatch.sendConfirmationMail(
                "gregap@gmail.com",
                ctx.user.getAttribute(DbAuthenticator.PUPIL_NAME) as String,
                ctx.user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String,
                payload,
                pupilActivities,
                ctx.get(EmailConfig::class.java)
            )
        }
        ctx.response.send("OK")
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

    private fun ActivityRecord.toDTO(isSelected: Boolean) =
        Activity(id, name, description, leader, slots.map { slot ->
            TimeSlot(translateDay(slot.day), slot.startMinutes, slot.endMinutes)
        }, isSelected)

}