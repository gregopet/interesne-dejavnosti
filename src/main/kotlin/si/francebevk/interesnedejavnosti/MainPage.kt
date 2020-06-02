package si.francebevk.interesnedejavnosti

import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.direct.DirectFormClient
import org.pac4j.http.client.indirect.FormClient
import org.slf4j.LoggerFactory
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.enums.ActivityLogType
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.db.tables.records.PupilGroupRecord
import si.francebevk.db.tables.records.PupilRecord
import si.francebevk.db.withTransaction
import si.francebevk.dto.*
import si.francebevk.ratpack.*
import si.francebevk.viewmodel.MainPageForm
import java.time.Instant;

/**
 * The main activity setting page.
 * The entire Chain requires a HttpProfile object to be present in the request representing the user we are getting
 */
object MainPage : Action<Chain> {

    private val LOG = LoggerFactory.getLogger(MainPage::class.java)

    override fun execute(t: Chain) = t.route {
        all { ctx ->
            // Only use the client that redirects to login form when user requests one of the main pages;
            // for Ajax requests, don't, because the endpoint might get stored by the auth framework and then
            // some poor user will try logging in again and will get an AJAX blurb
            val isMainPage = (ctx.request.path == "")
            val isFinishPost = (ctx.request.path == "store")
            if (isMainPage || isFinishPost) {
                ctx.insert(RatpackPac4j.requireAuth<UsernamePasswordCredentials, CommonProfile>(FormClient::class.java))
            } else {
                RatpackPac4j.userProfile(ctx).then { profile ->
                    if (profile.isPresent()) {
                        ctx.next()
                    } else {
                        ctx.response.status(400).send("Not logged in!")
                    }
                }
            }
        }

        all { ctx ->
            // Pre-prepare a permissions object for downstream
            RatpackPac4j.userProfile(ctx).then { profile ->
                val profileObj = profile.get()
                ctx.request.add(profileObj)
                ctx.next()
            }
        }
        get { html(it) }
        get("state") { pupilState(it) }
        post("store") { store(it) }
        get("finish") { endHtml(it) }
        get("vacancy") { vacancy(it) }
    }

    /** Are the leave times relevant for children going to this year */
    fun leaveTimesRelevant(year: Short) = year < 6

    /** Do we need to ask whether this child can leave school alone? */
    fun askForLeaveAlonePermission(year: Short) = leaveTimesRelevant(year) && year > 1

    /** Do we need to ask whether this child wants to stay in morning watch */
    fun askForMorningWatch(year: Short) = year <= 3;

    /** Can the students use school textbooks (i.e. they don't get them for free anyway)? */
    fun askForTextbooks(year: Short) = year >= 4

    /** Translates the pupil's class if the proper name can't be used directly */
    fun translatePupilClass(name: String, year: Short) = if (year > 1) name else "prvi razred"

    /** The times kids can be brought in for morning watch */
    val morningWatchTimes = arrayOf("6:15", "6:30", "6:45", "7:00", "7:15", "7:45", "8:00").map(String::toMinutes)

    /** The times kids can leave school */
    val leaveTimes = arrayOf("12:50", "13:40", "14:30", "15:30", "16:15", "17:00").map(String::toMinutes)

    /** Time of the afternoon snack - pupils can only have it if they leave later */
    val afternoonSnackTime = "15:20".toMinutes.toShort()

    /** Is this page displayed via admin authentication (i.e. an admin is editing a pupil)? */
    private fun isAdminRequest(ctx: Context) = ctx.request.uri.startsWith(prefix = "/admin/", ignoreCase = true)

    private fun html(ctx: Context) = ctx.async {
        val pupil = await { PupilDAO.getPupilById(ctx.user.id.toLong(), ctx.jooq) }!!
        val klass = await { ClassDAO.getClassByName(ctx.pupilClass, ctx.jooq) }
        val viewModel = getPupilPossibilities(pupil, klass, ctx)
        val viewModelJson = ctx.get(com.fasterxml.jackson.databind.ObjectMapper::class.java).writerWithDefaultPrettyPrinter().writeValueAsString(viewModel);

        ctx.render(Main.template(viewModel, viewModelJson))
    }

    private fun getPupilPossibilities(pupil: PupilRecord, klass: PupilGroupRecord, ctx: Context): MainPageForm {
        val deadline = ctx.get(Deadlines::class.java)
        val twoPhaseProcess = ctx.get(TwoPhaseProcess::class.java)
        val viewModel = MainPageForm(
            pupilName = "${pupil.firstName} ${pupil.lastName}",
            pupilClass = translatePupilClass(pupil.pupilGroup, klass.year),
            pupilHasExtendedStay = leaveTimesRelevant(klass.year),
            closeDate = deadline.endDateString,
            closeHour = deadline.endTimeString,
            morningWatchTimes = morningWatchTimes,
            leaveTimes = leaveTimes,
            isInFirstPhase = twoPhaseProcess.isInEffect,
            firstPhaseLimit = twoPhaseProcess.limit,
            firstPhaseEndDate = twoPhaseProcess.endDateString,
            firstPhaseEndTime = twoPhaseProcess.endTimeString,
            isAdminRequest = isAdminRequest(ctx),
            askForSelfLeave = askForLeaveAlonePermission(klass.year),
            askForMorningWatch = askForMorningWatch(klass.year),
            askForTextbooks = askForTextbooks(klass.year),
            afternoonSnackTime = afternoonSnackTime
        )
        return viewModel
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
            PupilDaySettings(leaveTime = pupil.leaveMon, morningSnack = pupil.morningSnackMon, lunch = pupil.lunchMon, afternoonSnack = pupil.afternoonSnackMon),
            PupilDaySettings(leaveTime = pupil.leaveTue, morningSnack = pupil.morningSnackTue, lunch = pupil.lunchTue, afternoonSnack = pupil.afternoonSnackTue),
            PupilDaySettings(leaveTime = pupil.leaveWed, morningSnack = pupil.morningSnackWed, lunch = pupil.lunchWed, afternoonSnack = pupil.afternoonSnackWed),
            PupilDaySettings(leaveTime = pupil.leaveThu, morningSnack = pupil.morningSnackThu, lunch = pupil.lunchThu, afternoonSnack = pupil.afternoonSnackThu),
            PupilDaySettings(leaveTime = pupil.leaveFri, morningSnack = pupil.morningSnackFri, lunch = pupil.lunchFri, afternoonSnack = pupil.afternoonSnackFri),
            twoPhaseProcess.limit,
            twoPhaseProcess.end.toEpochMilli(),
            authorizedPersons,
            pupil.canLeaveAlone,
            pupil.morningCareArrival,
            pupil.orderTextbooks
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

        // avoid all sorts of checks and workarounds on the frontend for this
        if (!leaveTimesRelevant(klass.year) || !payload.extendedStay) {
            payload.disableAfternoonSnacks()
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
                        PupilDAO.storeLeaveTimes(payload, pupilId, t)
                    } else {
                        PupilDAO.storeNonParticipation(payload, pupilId, t)
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
                        getPupilPossibilities(pupil, klass, ctx),
                        payload,
                        pupilActivities,
                        ctx.get(EmailConfig::class.java)
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