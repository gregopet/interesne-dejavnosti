package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.PUPIL_GROUP

object ClassDAO {

    fun getClassByName(name: String, jooq: DSLContext) = with(PUPIL_GROUP) {
        jooq.selectFrom(PUPIL_GROUP).where(NAME.eq(name)).fetchOne()
    }
}