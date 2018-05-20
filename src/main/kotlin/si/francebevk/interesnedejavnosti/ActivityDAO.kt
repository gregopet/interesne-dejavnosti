package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.ACTIVITY

object ActivityDAO {

    fun getActivitiesForClass(year: Short, jooq: DSLContext) = with(ACTIVITY) {
        jooq
        .selectFrom(ACTIVITY)
        .where(AVAILABLE_TO_YEARS.contains(arrayOf(year)))
        .fetch()
    }

}