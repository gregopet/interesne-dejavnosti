package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.PUPIL

object PupilDAO {

    fun getPupilByCode(code: String, jooq: DSLContext) = with(PUPIL) {
        jooq
        .selectFrom(PUPIL)
        .where(ACCESS_CODE.eq(code))
        .fetchOne()
    }
}