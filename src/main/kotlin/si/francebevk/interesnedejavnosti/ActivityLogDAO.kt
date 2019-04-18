package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.ACTIVITY_LOG
import si.francebevk.db.enums.ActivityLogType
import java.time.OffsetDateTime

object ActivityLogDAO {

    fun insertEvent(type: ActivityLogType, pupilId: Long, wasAdmin: Boolean, details: String?, jooq: DSLContext) = with(ACTIVITY_LOG) {
        jooq
            .insertInto(ACTIVITY_LOG)
            .set(PUPIL_ID, pupilId)
            .set(TYPE, type)
            .set(TIME, OffsetDateTime.now())
            .set(ADMIN_USER, wasAdmin)
            .set(DETAILS, details)
            .execute()
    }
}