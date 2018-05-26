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
import si.francebevk.dto.PupilState
import si.francebevk.dto.TimeSlot
import si.francebevk.ratpack.*

/**
 * The main activity setting page.
 */
object MainPage : Action<Chain> {

    override fun execute(t: Chain) = t.route {
        get { html(it) }
        get("state") { pupilState(it) }
        post("store") { store(it) }
        get("finish") { endHtml(it) }
        get("vacancy") { vacancy(it) }
    }

    private fun leaveTimesRelevant(year: Short) = year < 6

    private fun html(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        ctx.render(Main.template(pupil.name, pupil.pupilGroup, leaveTimesRelevant(klass.year)))
    }

    private fun endHtml(ctx: Context) {
        RatpackPac4j.logout(ctx).then {
            ctx.render(Finish.template())
        }
    }

    /** Lists all selected activities */
    private fun pupilState(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }
        val klassRecord = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val selected = await { ActivityDAO.getSelectedActivityIds(ctx.user.id.toLong(), ctx.jooq) }
        val activities = await { ActivityDAO.getActivitiesForClass(klassRecord.year, ctx.jooq) }
        val activitiesPayload = activities.map { it.toDTO(selected.contains(it.id)) }

        val payload = PupilState(
            activitiesPayload,
            pupil.extendedStay,
            pupil.leaveMon,
            pupil.leaveTue,
            pupil.leaveWed,
            pupil.leaveThu,
            pupil.leaveFri
        )

        ctx.renderJson(payload)
    }

    /** Store the student's selection */
    private fun store(ctx: Context) = ctx.async {
        val payload = ctx.parse(PupilSettings::class.java).await()
        val pupilId = ctx.user.id.toLong()
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }

        try {
            await {
                ctx.jooq.withTransaction { t ->
                    ActivityDAO.storeSelectedActivityIds(pupilId, payload.selectedActivities, t)
                    if (payload.extendedStay) {
                        PupilDAO.storeLeaveTimes(payload.mon, payload.tue, payload.wed, payload.thu, payload.fri, pupilId, ctx.jooq)
                    } else {
                        PupilDAO.storeNonParticipation(pupilId, ctx.jooq)
                    }
                }
            }

            // send confirmation email
            val pupilActivities = await {
                ActivityDAO.getSelectedActivitiesForPupil(pupilId, ctx.jooq)
            }.map { it.toDTO(true) }
            await {
                EmailDispatch.sendConfirmationMail(
                        "gregap@gmail.com",
                        ctx.pupilName,
                        ctx.pupilClass,
                        payload,
                        pupilActivities,
                        leaveTimesRelevant(klass.year),
                        ctx.get(EmailConfig::class.java)
                )
            }
            ctx.response.send("OK")
        } catch(ex: ActivityFullException) {
            ctx.response.status(409)
            ctx.renderJson(ex.activities.map { it.name })
        }
    }

    private fun vacancy(ctx: Context) = ctx.async {
        val vacancy = await { ActivityDAO.getVacancyForPupil(ctx.user.id.toLong(), ctx.jooq) }
        ctx.renderJson(vacancy)
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
        }, isSelected, cost)

}