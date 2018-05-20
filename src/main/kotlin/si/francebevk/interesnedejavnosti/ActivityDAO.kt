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

}