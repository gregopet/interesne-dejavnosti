package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.AUTHORIZED_COMPANION
import si.francebevk.db.Tables.PUPIL
import si.francebevk.db.enums.AuthorizedPersonType
import si.francebevk.db.tables.AuthorizedCompanion
import si.francebevk.db.tables.records.AuthorizedCompanionRecord
import si.francebevk.db.tables.records.PupilRecord
import si.francebevk.dto.AuthorizedPerson
import java.time.OffsetDateTime

object PupilDAO {

    fun getPupilById(pupilId: Long, jooq: DSLContext): PupilRecord? = with(PUPIL) {
        jooq
        .selectFrom(PUPIL)
        .where(ID.eq(pupilId))
        .fetchOne()
    }

    fun getPupilByCode(code: String, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(PUPIL.LAST_LOGIN, OffsetDateTime.now())
        .where(ACCESS_CODE.eq(code))
        .returning()
        .fetchOne()
    }

    /**
     * Indicates that the pupil will be involved in after school stay, at specified times.
     */
    fun storeLeaveTimes(mon: Short?, tue: Short?, wed: Short?, thu: Short?, fri: Short?, canLeaveAlone: Boolean, morningWatchArrival: Short?, pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, mon)
        .set(LEAVE_TUE, tue)
        .set(LEAVE_WED, wed)
        .set(LEAVE_THU, thu)
        .set(LEAVE_FRI, fri)
        .set(EXTENDED_STAY, true)
        .set(CAN_LEAVE_ALONE, canLeaveAlone)
        .set(MORNING_CARE_ARRIVAL, morningWatchArrival)
        .where(ID.eq(pupilId))
        .execute()
    }

    /**
     * Indicates that the pupil will not be involved in after school stay.
     */
    fun storeNonParticipation(canLeaveAlone: Boolean, morningWatchArrival: Short?, pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, null as Short?)
        .set(LEAVE_TUE, null as Short?)
        .set(LEAVE_WED, null as Short?)
        .set(LEAVE_THU, null as Short?)
        .set(LEAVE_FRI, null as Short?)
        .set(EXTENDED_STAY, false)
        .set(CAN_LEAVE_ALONE, canLeaveAlone)
        .set(MORNING_CARE_ARRIVAL, morningWatchArrival)
        .where(ID.eq(pupilId))
        .execute()
    }

    /** Updates the pupil with the [pupilId], marking that the email was sent */
    fun updateEmailSent(pupilId: Long, jooq: DSLContext) {
        jooq.update(PUPIL).set(PUPIL.WELCOME_EMAIL_SENT, true).where(PUPIL.ID.eq(pupilId)).execute()
    }

    /** Fetch all the persons authorized to pick this pupil up from school */
    fun fetchAuthorizedPersons(pupilId: Long, jooq: DSLContext): List<AuthorizedPerson> = with(AUTHORIZED_COMPANION) {
        jooq.select(NAME, TYPE).from(AUTHORIZED_COMPANION).where(PUPIL_ID.eq(pupilId)).orderBy(ID).fetch {
            AuthorizedPerson(it.value1(), it.value2())
        }
    }

    /** Store who can pick up the pupil */
    fun storeAuthorizedPersons(authorizedPersons: List<AuthorizedPerson>, pupilId: Long, jooq: DSLContext) = with(AUTHORIZED_COMPANION) {
        jooq.deleteFrom(AUTHORIZED_COMPANION).where(PUPIL_ID.eq(pupilId)).execute()
        if (authorizedPersons.isNotEmpty()) {
            jooq.insertInto(AUTHORIZED_COMPANION, PUPIL_ID, NAME, TYPE)
            .apply {
                authorizedPersons.forEach { person ->
                    values(pupilId, person.name ?: "", person.type ?: AuthorizedPersonType.other)
                }
            }
            .execute()
        }
    }
}