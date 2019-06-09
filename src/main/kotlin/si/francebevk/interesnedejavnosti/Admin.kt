package si.francebevk.interesnedejavnosti

import admin.*
import org.jooq.JSONFormat
import org.jooq.impl.DSL.*
import org.jooq.util.postgres.PostgresDSL
import org.pac4j.core.authorization.Authorizer
import org.pac4j.http.client.indirect.IndirectBasicAuthClient
import org.pac4j.http.profile.HttpProfile
import org.slf4j.LoggerFactory
import ratpack.exec.Blocking
import ratpack.func.Action
import ratpack.func.Block
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.jackson.Jackson.fromJson
import ratpack.jackson.Jackson.jsonNode
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.Tables.*
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.db.tables.records.PupilRecord
import si.francebevk.ratpack.async
import si.francebevk.ratpack.await
import si.francebevk.ratpack.jooq
import si.francebevk.ratpack.route
import si.francebevk.viewmodel.ActivityStats
import si.francebevk.viewmodel.PupilWithActivities

/**
 * Contains various administration functions.
 */
class Admin(config: AdminConfig) : Action<Chain> {

    private val LOG = LoggerFactory.getLogger(Admin::class.java)
    private val basicClient = IndirectBasicAuthClient(ConfigAuthenticator(config.username, config.password)).apply {
        realmName = "Osnovna šola Franceta Bevka"
    }

    override fun execute(t: Chain) = t.route {

        // Database authentication (=parents) is no good here!
        all { ctx ->
            RatpackPac4j.userProfile(ctx).then {
                if (it.isPresent && it.get().id != null) {
                    RatpackPac4j.logout(ctx).then { ctx.next() }
                } else {
                    ctx.next()
                }
            }
        }

        // Authentication required from here on, and make sure _again_ no database users (parents) can come here!
        all(RatpackPac4j.authenticator(basicClient))
        all(RatpackPac4j.requireAuth(IndirectBasicAuthClient::class.java, Authorizer<HttpProfile> { _, profile -> profile.id == null }))

        get(::summary)
        get("by-activity", ::summaryByActivity)
        get("by-hours", ::summaryHoursDaily)
        get("stats", ::stats)
        get("planner", ::planningYaml)
        get("activity/:pupilId:\\d+") { ctx -> activity(ctx.allPathTokens.get("pupilId")!!.toLong(), ctx) }
        path("pupil-editor/:pupilId:\\d+") { it.byMethod { m ->
            m.get { ctx -> getPupil(ctx.allPathTokens.get("pupilId")!!.toLong(), ctx) }
            m.post { ctx -> savePupil(ctx.allPathTokens.get("pupilId")!!.toLong(), ctx) }
        } }
        path("welcome-emails/:vsi?") { it.byMethod { m ->
            m.get { ctx -> showEmails(ctx.allPathTokens.get("vsi").isNullOrBlank(), ctx) }
            m.post { ctx -> sendEmails(ctx.allPathTokens.get("vsi").isNullOrBlank(), ctx) }
        } }
        path("reopening-emails/:vsi?") { it.byMethod { m ->
            m.get { ctx -> showReopeningEmails(ctx.allPathTokens.get("vsi").isNullOrBlank(), ctx) }
            m.post { ctx -> sendReopeningEmails(ctx.allPathTokens.get("vsi").isNullOrBlank(), ctx) }
        } }

        // Admins can emulate parents
        prefix("pupil/:pupilId:\\d+") { chain ->
            chain.all { ctx ->
                val pupilId = ctx.allPathTokens.get("pupilId")!!.toLong()
                val pupil = PupilDAO.getPupilById(pupilId, ctx.jooq)
                if (pupil == null) {
                    ctx.response.status(404).send("Učenca nismo našli")
                } else {
                    ctx.request.add(createUserProfile(pupil))
                    ctx.next()
                }
            }
            chain.insert(MainPage)
        }
    }

    fun getPupil(pupilId: Long, ctx: Context) = ctx.async {
        val record = await { ctx.jooq.select(PUPIL.ID, PUPIL.ACCESS_CODE, PUPIL.FIRST_NAME, PUPIL.LAST_NAME, PUPIL.PUPIL_GROUP, PUPIL.EMAILS).from(PUPIL).where(PUPIL.ID.eq(pupilId)).fetchOne() }
        if (record == null) {
            ctx.response.status(404).send()
        } else {
            ctx.response.contentType("application/json").send(record.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS.recordFormat(JSONFormat.RecordFormat.OBJECT)))
        }
    }
    fun savePupil(pupilId: Long, ctx: Context) {
        ctx.parse(jsonNode()).then { pupil ->
            Blocking.exec {
                ctx.jooq.update(PUPIL)
                .set(PUPIL.FIRST_NAME, pupil.get("first_name").asText())
                .set(PUPIL.LAST_NAME, pupil.get("last_name").asText())
                .set(PUPIL.PUPIL_GROUP, pupil.get("pupil_group").asText())
                .set(PUPIL.EMAILS, pupil.get("emails").elements().asSequence().map { it.asText().trim() }.filter { !it.isBlank() }.asIterable().toList().toTypedArray())
                .where(PUPIL.ID.eq(pupilId))
                .execute()

                ctx.response.send()
            }
        }
    }

    /** Outputs a summary list of all the pupils and their chosen activities */
    fun summary(ctx: Context) = ctx.async {
        val subselect = PostgresDSL.array(
            select(ACTIVITY.ID)
            .from(ACTIVITY)
            .join(PUPIL_ACTIVITY).on(PUPIL_ACTIVITY.ACTIVITY_ID.eq(ACTIVITY.ID))
            .where(PUPIL_ACTIVITY.PUPIL_ID.eq(PUPIL.ID))
            .orderBy(ACTIVITY.NAME)
        )

        val pupils = await {
            ctx.jooq
            .select(subselect, *PUPIL.fields())
            .from(PUPIL)
            .orderBy(PUPIL.PUPIL_GROUP, PUPIL.LAST_NAME, PUPIL.FIRST_NAME)
            .fetch {
                PupilWithActivities(it.into(PUPIL), it.getValue(0) as Array<Long>)
            }
        }

        val activities = await {
            ctx.jooq.selectFrom(ACTIVITY).fetch().intoMap(ActivityRecord::getId)
        }

        val klasses = await {
            ctx.jooq.select(PUPIL_GROUP.NAME).from(PUPIL_GROUP).orderBy(PUPIL_GROUP.NAME).fetch(PUPIL_GROUP.NAME)
        }

        ctx.render(PupilList.template(pupils, activities, klasses))
    }

    fun summaryByActivity(ctx: Context) = ctx.async {
        val activities = await { ctx.jooq.selectFrom(ACTIVITY).orderBy(ACTIVITY.NAME).fetch() }
        val pupils = await {
            ctx.jooq
            .select()
            .from(PUPIL_ACTIVITY)
            .join(PUPIL).on(PUPIL_ACTIVITY.PUPIL_ID.eq(PUPIL.ID))
            .orderBy(PUPIL.PUPIL_GROUP, PUPIL.LAST_NAME, PUPIL.FIRST_NAME)
            .fetchGroups(PUPIL_ACTIVITY.ACTIVITY_ID) {
                it.into(PUPIL)
            }
        }

        ctx.render(PupilListByActivity.template(activities, pupils))
    }

    fun summaryHoursDaily(ctx: Context) = ctx.async {
        val reportLines = await { ctx.jooq.selectFrom(DEPARTURES_HOURLY_REPORT).fetch() }
        ctx.render(SummaryHoursDaily.template(reportLines))
    }


    /** Outputs a summary of how many pupils per activity have applied */
    fun stats(ctx: Context) = ctx.async {
        val activities = await {
            ctx.jooq
            .select(selectCount().from(PUPIL_ACTIVITY).where(PUPIL_ACTIVITY.ACTIVITY_ID.eq(ACTIVITY.ID)).asField<Int>())
            .select(*ACTIVITY.fields())
            .from(ACTIVITY)
            .orderBy(ACTIVITY.NAME)
            .fetch {
                ActivityStats(it.into(ACTIVITY), it.getValue(0) as Int)
            }
        }

        ctx.render(Stats.template(activities))
    }

    fun showEmails(sendToAll: Boolean, ctx: Context) = ctx.async {
        val condition = if (sendToAll) trueCondition() else PUPIL.WELCOME_EMAIL_SENT.eq(false)
        val emailsToSend = await {
            ctx.jooq.selectCount().from(PUPIL).where(condition).fetchOne().value1()
        }
        ctx.render(WelcomeEmails.template("vabilo v sistem", emailsToSend, "/" + ctx.request.path, sendToAll))
    }

    fun sendEmails(sendToAll: Boolean, ctx: Context) = ctx.async {
        val condition = if (sendToAll) trueCondition() else PUPIL.WELCOME_EMAIL_SENT.eq(false)
        val emailConfig = ctx.get(EmailConfig::class.java)
        val fileConfig = ctx.get(FileConfig::class.java)
        val failedPupils = ArrayList<String>()
        val noEmailPupils = ArrayList<String>()
        val sentTo = await {
            ctx.jooq.select(PUPIL.ID, PUPIL.EMAILS, PUPIL.ACCESS_CODE, PUPIL.FIRST_NAME, PUPIL.LAST_NAME, PUPIL.PUPIL_GROUP, PUPIL_GROUP.YEAR)
            .from(PUPIL)
            .join(PUPIL_GROUP).on(PUPIL.PUPIL_GROUP.eq(PUPIL_GROUP.NAME))
            .where(condition)
            .orderBy(PUPIL.ID)
            .fetch { pupil ->
                if (pupil.getValue(PUPIL.EMAILS).isNotEmpty()) {
                    LOG.info("Sending welcome email to pupil ${pupil.getValue(PUPIL.ID)} ${pupil.getValue(PUPIL.FIRST_NAME)} ${pupil.getValue(PUPIL.FIRST_NAME)} at emails ${pupil.getValue(PUPIL.EMAILS).joinToString()}")
                    if (EmailDispatch.sendWelcomeEmail(
                        to = pupil.getValue(PUPIL.EMAILS),
                        pupilId = pupil.getValue(PUPIL.ID),
                        pupilName = pupil.getValue(PUPIL.FIRST_NAME) + " " + pupil.getValue(PUPIL.LAST_NAME),
                        accessCode = pupil.getValue(PUPIL.ACCESS_CODE) ?: "<ni kode - kontaktirajte šolo>",
                        pupilClass = MainPage.translatePupilClass(pupil.getValue(PUPIL.PUPIL_GROUP), pupil.getValue(PUPIL_GROUP.YEAR)),
                        leaveTimesRelevant = MainPage.leaveTimesRelevant(pupil.getValue(PUPIL_GROUP.YEAR)),
                        config = emailConfig,
                        fileConfig = fileConfig,
                        deadlines = ctx.get(Deadlines::class.java),
                        jooq = ctx.jooq,
                        failedPupils = failedPupils
                    )) {
                        "${pupil.getValue(PUPIL.FIRST_NAME)} ${pupil.getValue(PUPIL.LAST_NAME)}: ${pupil.getValue(PUPIL.EMAILS).joinToString()}"
                    } else {
                        null
                    }
                } else {
                    LOG.warn("Not sending email for pupil ${pupil.getValue(PUPIL.ID)} because they have no email defined!")
                    noEmailPupils.add(pupil.getValue(PUPIL.FIRST_NAME) + " " + pupil.getValue(PUPIL.LAST_NAME))
                    null
                }
            }
        }.filterNotNull()
        LOG.info("All emails sent!")
        ctx.render(WelcomeEmailsResult.template(sentTo, failedPupils, noEmailPupils))
    }

    fun showReopeningEmails(sendToAll: Boolean, ctx: Context) = ctx.async {
        val condition = if (sendToAll) trueCondition() else PUPIL.WELCOME_EMAIL_SENT.eq(false)
        val emailsToSend = await {
            ctx.jooq.selectCount().from(PUPIL).where(condition).fetchOne().value1()
        }
        ctx.render(WelcomeEmails.template("ponovna otvoritev prijav", emailsToSend, "/" + ctx.request.path, sendToAll))
    }

    fun sendReopeningEmails(sendToAll: Boolean, ctx: Context) = ctx.async {
        val condition = if (sendToAll) trueCondition() else PUPIL.WELCOME_EMAIL_SENT.eq(false)
        val emailConfig = ctx.get(EmailConfig::class.java)
        val fileConfig = ctx.get(FileConfig::class.java)
        val failedPupils = ArrayList<String>()
        val noEmailPupils = ArrayList<String>()
        val sentTo = await {
            ctx.jooq.select(PUPIL.ID, PUPIL.EMAILS, PUPIL.ACCESS_CODE, PUPIL.FIRST_NAME, PUPIL.LAST_NAME, PUPIL.PUPIL_GROUP, PUPIL_GROUP.YEAR)
            .from(PUPIL)
            .join(PUPIL_GROUP).on(PUPIL.PUPIL_GROUP.eq(PUPIL_GROUP.NAME))
            .where(condition)
            .orderBy(PUPIL.ID)
            .fetch { pupil ->
                if (pupil.getValue(PUPIL.EMAILS).isNotEmpty()) {
                    LOG.info("Sending reopening email to pupil ${pupil.getValue(PUPIL.ID)} ${pupil.getValue(PUPIL.FIRST_NAME)} ${pupil.getValue(PUPIL.LAST_NAME)} at emails ${pupil.getValue(PUPIL.EMAILS).joinToString()}")
                    if (EmailDispatch.sendReopeningEmail(
                        to = pupil.getValue(PUPIL.EMAILS),
                        pupilId = pupil.getValue(PUPIL.ID),
                        pupilName = pupil.getValue(PUPIL.FIRST_NAME) + " " + pupil.getValue(PUPIL.LAST_NAME),
                        accessCode = pupil.getValue(PUPIL.ACCESS_CODE),
                        pupilClass = MainPage.translatePupilClass(pupil.getValue(PUPIL.PUPIL_GROUP), pupil.getValue(PUPIL_GROUP.YEAR)),
                        leaveTimesRelevant = MainPage.leaveTimesRelevant(pupil.getValue(PUPIL_GROUP.YEAR)),
                        config = emailConfig,
                        fileConfig = fileConfig,
                        jooq = ctx.jooq,
                        failedPupils = failedPupils
                    )) {
                        "${pupil.getValue(PUPIL.FIRST_NAME)} ${pupil.getValue(PUPIL.LAST_NAME)}: ${pupil.getValue(PUPIL.EMAILS).joinToString()}"
                    } else {
                        null
                    }
                } else {
                    LOG.warn("Not sending email for pupil ${pupil.getValue(PUPIL.ID)} because they have no email defined!")
                    noEmailPupils.add(pupil.getValue(PUPIL.FIRST_NAME) + " " + pupil.getValue(PUPIL.LAST_NAME))
                    null
                }
            }
        }.filterNotNull()
        LOG.info("All emails sent!")
        ctx.render(WelcomeEmailsResult.template(sentTo, failedPupils, noEmailPupils))
    }

    /**
     * Displays the login / storage activity for this pupil.
     */
    fun activity(pupilId: Long, ctx: Context) = ctx.async {
        val logs = await { ActivityLogDAO.getForPupil(pupilId, ctx.jooq) }
        ctx.response.contentType("application/json").send(
            logs.formatJSON(JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT).header(false))
        )
    }

    /**
     * Outputs a YAML file for the planner.
     */
    fun planningYaml(ctx: Context) = ctx.async {
        fun canonicalName(act: ActivityRecord) = "${act.name} (${act.id})"

        val output = StringBuilder()

        output.appendln("classes:")
        val classes = await { ctx.jooq.selectFrom(PUPIL_GROUP).fetch() }
        classes.forEach { output.appendln("- ${it.name}") }

        output.appendln("times:")
        output.appendln(
        """|- from: 13:45
           |  to: 14:35
           |- from: 14:35
           |  to: 15:20
           |- from: 15:30
           |  to: 16:15
           |- from: 16:20
           |  to: 17:05""".trimMargin("|")
        )

        output.appendln("activities:")
        val activities = await { ctx.jooq.selectFrom(ACTIVITY).orderBy(ACTIVITY.NAME).fetch() }
        activities.forEach { act ->
            output.appendln("- id: ${canonicalName(act)}")
            if (act.slots.isNotEmpty()) {
                output.appendln("  timeSlots:")
                act.slots.forEach { slot ->
                    output.appendln("  - day: ${slot.day.fullSlo}")
                    output.appendln("    from: ${slot.startMinutes.minuteTimeFormat}")
                    output.appendln("    to: ${slot.endMinutes.minuteTimeFormat}")
                }
            } else {
                output.appendln("  timeSlots: []")
            }
        }

        output.appendln("pupils:")
        val pupils = await { ctx.jooq.selectFrom(PUPIL).orderBy(PUPIL.LAST_NAME, PUPIL.FIRST_NAME).fetch() }
        val pupilActivities = await { ctx.jooq.selectFrom(PUPIL_ACTIVITY).fetch() }.groupBy { it.pupilId }
        val activityById = activities.associateBy { it.id }
        pupils.forEach { pupil ->
            output.appendln("- name: ${pupil.firstName} ${pupil.lastName}")
            output.appendln("  klass: ${pupil.pupilGroup}")

            val selectedActivities = pupilActivities.get(pupil.id)
            if (selectedActivities != null) {
                output.appendln("  activities:")
                selectedActivities.forEach { act ->
                    output.appendln("  - ${canonicalName(activityById[act.activityId]!!)}")
                }

            }

            output.appendln("  leaveTimes:")
            if (pupil.extendedStay) {
                output.appendln("    Ponedeljek: ${pupil.leaveMon?.minuteTimeFormat ?: ""}")
                output.appendln("    Torek: ${pupil.leaveTue?.minuteTimeFormat ?: ""}")
                output.appendln("    Sreda: ${pupil.leaveWed?.minuteTimeFormat ?: ""}")
                output.appendln("    Četrtek: ${pupil.leaveThu?.minuteTimeFormat ?: ""}")
                output.appendln("    Petek: ${pupil.leaveFri?.minuteTimeFormat ?: ""}")
            } else {
                output.appendln("    Ponedeljek:")
                output.appendln("    Torek:")
                output.appendln("    Sreda:")
                output.appendln("    Četrtek:")
                output.appendln("    Petek:")
            }
        }

        ctx.response.headers.add("Content-Disposition", "attachment; filename=\"2018.yaml\"")
        ctx.response.send(output.toString())
    }

}