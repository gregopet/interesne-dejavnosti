package si.francebevk.interesnedejavnosti

import org.slf4j.LoggerFactory
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.enums.ActivityLogType
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.db.withTransaction
import si.francebevk.dto.*
import si.francebevk.ratpack.*
import java.time.Instant;

/**
 * The main activity setting page.
 * The entire Chain requires a HttpProfile object to be present in the request representing the user we are getting
 */
object MainPage : Action<Chain> {

    private val LOG = LoggerFactory.getLogger(MainPage::class.java)

    override fun execute(t: Chain) = t.route {
        get { html(it) }
        get("state") { pupilState(it) }
        post("store") { store(it) }
        get("finish") { endHtml(it) }
        get("vacancy") { vacancy(it) }
    }

    /** Are the leave times relevant for children going to this year */
    fun leaveTimesRelevant(year: Short) = year < 6

    /** Translates the pupil's class if the proper name can't be used directly */
    fun translatePupilClass(name: String, year: Short) = if (year > 1) name else "prvi razred"

    /** Is this page displayed via admin authentication (i.e. an admin is editing a pupil)? */
    private fun isAdminRequest(ctx: Context) = ctx.request.uri.startsWith(prefix = "/admin/", ignoreCase = true)

    private fun html(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }!!
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val deadline = ctx.get(Deadlines::class.java)
        val twoPhaseProcess = ctx.get(TwoPhaseProcess::class.java)
        ctx.render(Main.template(
            "${pupil.firstName} ${pupil.lastName}",
            translatePupilClass(pupil.pupilGroup, klass.year),
            leaveTimesRelevant(klass.year),
            deadline.endDateString,
            deadline.endTimeString,
            twoPhaseProcess.isInEffect,
            twoPhaseProcess.limit,
            twoPhaseProcess.endDateString,
            twoPhaseProcess.endTimeString,
            isAdminRequest(ctx))
        )
    }

    private fun endHtml(ctx: Context) {
        RatpackPac4j.logout(ctx).then {
            ctx.render(Finish.template())
        }
    }

    /** Lists all selected activities */
    private fun pupilState(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }!!
        val klassRecord = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val selected = await { ActivityDAO.getSelectedActivityIds(ctx.user.id.toLong(), ctx.jooq) }
        val activities = await { ActivityDAO.getActivitiesForClass(klassRecord.year, ctx.jooq) }
        val activitiesPayload = activities.map { it.toDTO(selected.contains(it.id)) }
        val twoPhaseProcess = ctx.get(TwoPhaseProcess::class.java)
        val authorizedPersons = await { PupilDAO.fetchAuthorizedPersons(ctx.user.id.toLong(), ctx.jooq) }

        val payload = PupilState(
            activitiesPayload,
            pupil.extendedStay,
            pupil.leaveMon,
            pupil.leaveTue,
            pupil.leaveWed,
            pupil.leaveThu,
            pupil.leaveFri,
            twoPhaseProcess.limit,
            twoPhaseProcess.end.toEpochMilli(),
            authorizedPersons
        )

        ctx.renderJson(payload)
    }

    /** Store the student's selection */
    private fun store(ctx: Context) = ctx.async {
        val payload = ctx.parse(PupilSettings::class.java).await()
        val pupilId = ctx.user.id.toLong()
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }!!
        val authorizedPersons = payload.authorizedPersons?.filter { !it.name.isNullOrBlank() } ?: emptyList()

        val twoPhaseProcess = ctx.get(TwoPhaseProcess::class.java)
        if (twoPhaseProcess.isInEffect) {
            val selectedLimitedActivities = await { ActivityDAO.limitedActivityCount(payload.selectedActivities, ctx.jooq) }
            if (selectedLimitedActivities > twoPhaseProcess.limit) {
                ctx.response.status(400).send("Izbrali ste preveč aktivnosti z omejitvijo prijav - prosimo vas, da se omejite samo na ${twoPhaseProcess.limit} aktivnosti take vrste!")
                return@async null
            }
        }

        if (isAdminRequest(ctx)) {
            LOG.info("Administrator storing activities for pupil ${pupil.id}")
        } else {
            LOG.info("Storing activities for pupil ${pupil.id}")
        }

        val description = StringBuilder()
        payload.appendDescriptionTo(description)

        try {

            // Try to save selection - this method may terminate with an exception!
            await {
                ctx.jooq.withTransaction { t ->
                    ActivityDAO.storeSelectedActivityIds(pupilId, payload.selectedActivities, t)
                    if (payload.extendedStay) {
                        PupilDAO.storeLeaveTimes(payload.monday, payload.tuesday, payload.wednesday, payload.thursday, payload.friday, pupilId, t)
                    } else {
                        PupilDAO.storeNonParticipation(pupilId, t)
                    }
                    PupilDAO.storeAuthorizedPersons(authorizedPersons, pupilId, t)
                }
            }

            // Get what the user had selected (with names and everything - we need it for logging!)
            val pupilActivities = await {
                ActivityDAO.getSelectedActivitiesForPupil(pupilId, ctx.jooq)
            }.map { it.toDTO(true) }

            pupilActivities.appendDescriptionTo(description.append("DEJAVNOSTI: "))
            ActivityLogDAO.insertEvent(ActivityLogType.submit, pupilId, isAdminRequest(ctx), description.toString(), ctx.jooq)

            if (payload.notifyViaEmail) {
                await {
                    EmailDispatch.sendConfirmationMail(
                        pupil.emails,
                        pupilId,
                        ctx.jooq,
                        ctx.pupilName,
                        translatePupilClass(ctx.pupilClass, klass.year),
                        payload,
                        pupilActivities,
                        leaveTimesRelevant(klass.year),
                        ctx.get(EmailConfig::class.java),
                        authorizedPersons
                    )
                }
            } else {
                LOG.info("Saved activities for pupil ${pupil.id} without sending an email!")
            }

            ctx.response.send("OK")
            null

        } catch(ex: ActivityFullException) {
            ctx.response.status(409)
            ctx.renderJson(ex.activities.map { it.name })
            ex.appendDescriptionTo(description.append("; "))
            ActivityLogDAO.insertEvent(ActivityLogType.failed_submit, pupilId, isAdminRequest(ctx), description.toString(), ctx.jooq)
        }
    }

    private fun vacancy(ctx: Context) = ctx.async {
        val vacancy = await { ActivityDAO.getVacancyForPupil(ctx.user.id.toLong(), ctx.jooq) }
        ctx.renderJson(vacancy)
    }

    private fun ActivityRecord.toDTO(isSelected: Boolean) =
        Activity(id, name, description, leader, slots.map { slot ->
            TimeSlot(slot.day.literal, slot.startMinutes, slot.endMinutes)
        }, isSelected, cost)
}