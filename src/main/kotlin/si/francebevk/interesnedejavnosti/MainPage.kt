package si.francebevk.interesnedejavnosti

import org.slf4j.LoggerFactory
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
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * The main activity setting page.
 */
object MainPage : Action<Chain> {

    private val LOG = LoggerFactory.getLogger(MainPage::class.java)

    private val START_DATE = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2018, 6, 8), LocalTime.NOON), ZoneId.of("Europe/Ljubljana"))
    private val END_DATE = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2018, 6, 15), LocalTime.NOON), ZoneId.of("Europe/Ljubljana"))

    /** A formatted description of the date when the application opens */
    val formattedStartDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("sl")).format(START_DATE)

    /** A formatted description of the time when the application opens */
    val formattedStartTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("sl")).format(START_DATE)

    /** A formatted description of the date when the application closes */
    val formattedEndDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("sl")).format(END_DATE)

    /** A formatted description of the time when the application closes */
    val formattedEndTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("sl")).format(END_DATE)

    /** Are we still before the start date? */
    val isBeforeStart get() = START_DATE.isAfter(ZonedDateTime.now())

    /** Are we already after the end date? */
    val isAfterEnd get() = END_DATE.isBefore(ZonedDateTime.now())


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

    private fun html(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        ctx.render(Main.template(pupil.name, translatePupilClass(pupil.pupilGroup, klass.year), leaveTimesRelevant(klass.year), formattedEndDate, formattedEndTime))
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
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }

        LOG.info("Storing activities for pupil ${pupil.id}")

        try {
            await {
                ctx.jooq.withTransaction { t ->
                    ActivityDAO.storeSelectedActivityIds(pupilId, payload.selectedActivities, t)
                    if (payload.extendedStay) {
                        PupilDAO.storeLeaveTimes(payload.monday, payload.tuesday, payload.wednesday, payload.thursday, payload.friday, pupilId, ctx.jooq)
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
                        pupil.emails,
                        pupilId,
                        ctx.jooq,
                        ctx.pupilName,
                        translatePupilClass(ctx.pupilClass, klass.year),
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

    private fun ActivityRecord.toDTO(isSelected: Boolean) =
        Activity(id, name, description, leader, slots.map { slot ->
            TimeSlot(slot.day.literal, slot.startMinutes, slot.endMinutes)
        }, isSelected, cost)

}