package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.ACTIVITY
import si.francebevk.db.Tables.PUPIL_ACTIVITY

object ActivityDAO {

    fun getActivitiesForClass(year: Short, jooq: DSLContext) = with(ACTIVITY) {
        jooq
        .selectFrom(ACTIVITY)
        .where(AVAILABLE_TO_YEARS.contains(arrayOf(year)))
        .fetch()
    }

    fun getSelectedActivityIds(pupilId: Long, jooq: DSLContext) = with(PUPIL_ACTIVITY) {
        jooq.select(ACTIVITY_ID).from(PUPIL_ACTIVITY).where(PUPIL_ID.eq(pupilId)).fetch().map { it.value1() }.toSet()
    }

    fun storeSelectedActivityIds(pupilId: Long, activityIds: List<Long>, trans: DSLContext) = with(PUPIL_ACTIVITY) {
        trans.deleteFrom(PUPIL_ACTIVITY).where(PUPIL_ID.eq(pupilId)).execute()
        trans.insertInto(PUPIL_ACTIVITY, PUPIL_ID, ACTIVITY_ID).also { query ->
            activityIds.forEach { actId -> query.values(pupilId, actId ) }
        }
        .execute()
    }

}