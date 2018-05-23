package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.PUPIL

object PupilDAO {

    fun getPupilById(pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .selectFrom(PUPIL)
        .where(ID.eq(pupilId))
        .fetchOne()
    }

    fun getPupilByCode(code: String, jooq: DSLContext) = with(PUPIL) {
        jooq
        .selectFrom(PUPIL)
        .where(ACCESS_CODE.eq(code))
        .fetchOne()
    }

    /**
     * Indicates that the pupil will be involved in after school stay, at specified times.
     */
    fun storeLeaveTimes(mon: Short?, tue: Short?, wed: Short?, thu: Short?, fri: Short?, pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, mon)
        .set(LEAVE_TUE, tue)
        .set(LEAVE_WED, wed)
        .set(LEAVE_THU, thu)
        .set(LEAVE_FRI, fri)
        .set(EXTENDED_STAY, true)
        .where(ID.eq(pupilId))
        .execute()
    }

    /**
     * Indicates that the pupil will not be involved in after school stay.
     */
    fun storeNonParticipation(pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, null as Short?)
        .set(LEAVE_TUE, null as Short?)
        .set(LEAVE_WED, null as Short?)
        .set(LEAVE_THU, null as Short?)
        .set(LEAVE_FRI, null as Short?)
        .set(EXTENDED_STAY, false)
        .where(ID.eq(pupilId))
        .execute()
    }
}