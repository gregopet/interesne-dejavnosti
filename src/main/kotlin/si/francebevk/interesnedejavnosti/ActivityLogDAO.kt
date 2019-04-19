package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import si.francebevk.db.Tables.ACTIVITY_LOG
import si.francebevk.db.enums.ActivityLogType
import java.time.OffsetDateTime

object ActivityLogDAO {

    private val LOG = LoggerFactory.getLogger(ActivityLogDAO::class.java)

    fun insertEvent(type: ActivityLogType, pupilId: Long, wasAdmin: Boolean, details: String?, jooq: DSLContext) = with(ACTIVITY_LOG) {
        try {
            jooq
            .insertInto(ACTIVITY_LOG)
            .set(PUPIL_ID, pupilId)
            .set(TYPE, type)
            .set(TIME, OffsetDateTime.now())
            .set(ADMIN_USER, wasAdmin)
            .set(DETAILS, details)
            .execute()
        } catch (t: Throwable) {
            LOG.warn("Error saving activity log!", t)
            // Execution should now continue and should not fail because of a failed logging call!
        }
    }

    fun getForPupil(pupilId: Long, jooq: DSLContext) = with(ACTIVITY_LOG) {
        jooq.selectFrom(ACTIVITY_LOG).where(PUPIL_ID.eq(pupilId)).orderBy(TIME).fetch()
    }
}