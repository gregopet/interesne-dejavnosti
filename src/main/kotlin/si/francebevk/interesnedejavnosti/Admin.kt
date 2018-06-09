package si.francebevk.interesnedejavnosti

import admin.PupilList
import org.jooq.impl.DSL.select
import org.jooq.util.postgres.PostgresDSL
import org.pac4j.core.authorization.Authorizer
import org.pac4j.core.context.WebContext
import org.pac4j.http.client.indirect.IndirectBasicAuthClient
import org.pac4j.http.profile.HttpProfile
import org.slf4j.LoggerFactory
import ratpack.form.Form
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.pac4j.RatpackPac4j
import si.francebevk.db.Tables.*
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.ratpack.async
import si.francebevk.ratpack.await
import si.francebevk.ratpack.jooq
import si.francebevk.ratpack.route
import si.francebevk.viewmodel.PupilWithActivities

/**
 * Contains various administration functions.
 */
object Admin : Action<Chain> {

    private val LOG = LoggerFactory.getLogger(Admin::class.java)
    private val basicClient = IndirectBasicAuthClient(ConfigAuthenticator).apply {
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

        get { summary(it) }


        post("welcome-emails", ::sendEmails)
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
            .orderBy(PUPIL.PUPIL_GROUP, PUPIL.NAME)
            .fetch {
                PupilWithActivities(it.into(PUPIL), it.getValue(0) as Array<Long>)
            }
        }

        val activities = await {
            ctx.jooq.selectFrom(ACTIVITY).fetch().intoMap(ActivityRecord::getId)
        }

        ctx.render(PupilList.template(pupils, activities))
    }


    fun sendEmails(ctx: Context) = ctx.async {
        val emailConfig = ctx.get(EmailConfig::class.java)
        val fileConfig = ctx.get(FileConfig::class.java)
        await {
            ctx.jooq.select(PUPIL.ID, PUPIL.EMAILS, PUPIL.ACCESS_CODE, PUPIL.NAME, PUPIL.PUPIL_GROUP, PUPIL_GROUP.YEAR)
            .from(PUPIL)
            .join(PUPIL_GROUP).on(PUPIL.PUPIL_GROUP.eq(PUPIL_GROUP.NAME))
            .orderBy(PUPIL.ID)
            .fetch { pupil ->
                if (pupil.getValue(PUPIL.EMAILS).isNotEmpty()) {
                    LOG.info("Sending welcome email to pupil ${pupil.getValue(PUPIL.ID)} ${pupil.getValue(PUPIL.NAME)} at emails ${pupil.getValue(PUPIL.EMAILS).joinToString()}")
                    EmailDispatch.sendWelcomeEmail(
                        to = pupil.getValue(PUPIL.EMAILS),
                        pupilId = pupil.getValue(PUPIL.ID),
                        pupilName = pupil.getValue(PUPIL.NAME),
                        accessCode = pupil.getValue(PUPIL.ACCESS_CODE),
                        pupilClass = MainPage.translatePupilClass(pupil.getValue(PUPIL.PUPIL_GROUP), pupil.getValue(PUPIL_GROUP.YEAR)),
                        leaveTimesRelevant = MainPage.leaveTimesRelevant(pupil.getValue(PUPIL_GROUP.YEAR)),
                        config = emailConfig,
                        fileConfig = fileConfig,
                        jooq = ctx.jooq
                    )
                } else {
                    LOG.warn("Not sending email for pupil ${pupil.getValue(PUPIL.ID)} because they have no email defined!")
                }
            }
        }
        LOG.info("All emails sent!")
        ctx.response.send("OK")
    }
}