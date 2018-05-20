package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.ACTIVITY

object ActivityDAO {

    fun getActivitiesForClass(className: String, jooq: DSLContext) = with(ACTIVITY) {
        jooq.selectFrom(ACTIVITY).fetch()
    }

}