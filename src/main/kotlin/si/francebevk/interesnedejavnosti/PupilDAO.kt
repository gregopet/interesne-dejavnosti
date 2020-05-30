package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import si.francebevk.db.Tables.AUTHORIZED_COMPANION
import si.francebevk.db.Tables.PUPIL
import si.francebevk.db.enums.AuthorizedPersonType
import si.francebevk.db.tables.AuthorizedCompanion
import si.francebevk.db.tables.records.AuthorizedCompanionRecord
import si.francebevk.db.tables.records.PupilRecord
import si.francebevk.dto.AuthorizedPerson
import si.francebevk.dto.PupilSettings
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
    fun storeLeaveTimes(settings: PupilSettings, pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, settings.monday.leaveTime)
        .set(LEAVE_TUE, settings.tuesday.leaveTime)
        .set(LEAVE_WED, settings.wednesday.leaveTime)
        .set(LEAVE_THU, settings.thursday.leaveTime)
        .set(LEAVE_FRI, settings.friday.leaveTime)
        .set(MORNING_SNACK_MON, settings.monday.morningSnack)
        .set(MORNING_SNACK_TUE, settings.tuesday.morningSnack)
        .set(MORNING_SNACK_WED, settings.wednesday.morningSnack)
        .set(MORNING_SNACK_THU, settings.thursday.morningSnack)
        .set(MORNING_SNACK_FRI, settings.friday.morningSnack)
        .set(LUNCH_MON, settings.monday.lunch)
        .set(LUNCH_TUE, settings.tuesday.lunch)
        .set(LUNCH_WED, settings.wednesday.lunch)
        .set(LUNCH_THU, settings.thursday.lunch)
        .set(LUNCH_FRI, settings.friday.lunch)
        .set(AFTERNOON_SNACK_MON, settings.monday.afternoonSnack)
        .set(AFTERNOON_SNACK_TUE, settings.tuesday.afternoonSnack)
        .set(AFTERNOON_SNACK_WED, settings.wednesday.afternoonSnack)
        .set(AFTERNOON_SNACK_THU, settings.thursday.afternoonSnack)
        .set(AFTERNOON_SNACK_FRI, settings.friday.afternoonSnack)
        .set(EXTENDED_STAY, true)
        .set(CAN_LEAVE_ALONE, settings.canLeaveAlone)
        .set(MORNING_CARE_ARRIVAL, settings.morningWatchArrival)
        .set(ORDER_TEXTBOOKS, settings.orderTextbooks)
        .where(ID.eq(pupilId))
        .execute()
    }

    /**
     * Indicates that the pupil will not be involved in after school stay.
     */
    fun storeNonParticipation(settings: PupilSettings, pupilId: Long, jooq: DSLContext) = with(PUPIL) {
        jooq
        .update(PUPIL)
        .set(LEAVE_MON, null as Short?)
        .set(LEAVE_TUE, null as Short?)
        .set(LEAVE_WED, null as Short?)
        .set(LEAVE_THU, null as Short?)
        .set(LEAVE_FRI, null as Short?)
        .set(MORNING_SNACK_MON, settings.monday.morningSnack)
        .set(MORNING_SNACK_TUE, settings.tuesday.morningSnack)
        .set(MORNING_SNACK_WED, settings.wednesday.morningSnack)
        .set(MORNING_SNACK_THU, settings.thursday.morningSnack)
        .set(MORNING_SNACK_FRI, settings.friday.morningSnack)
        .set(LUNCH_MON, settings.monday.lunch)
        .set(LUNCH_TUE, settings.tuesday.lunch)
        .set(LUNCH_WED, settings.wednesday.lunch)
        .set(LUNCH_THU, settings.thursday.lunch)
        .set(LUNCH_FRI, settings.friday.lunch)
        .set(AFTERNOON_SNACK_MON, false)
        .set(AFTERNOON_SNACK_TUE, false)
        .set(AFTERNOON_SNACK_WED, false)
        .set(AFTERNOON_SNACK_THU, false)
        .set(AFTERNOON_SNACK_FRI, false)
        .set(EXTENDED_STAY, false)
        .set(CAN_LEAVE_ALONE, settings.canLeaveAlone)
        .set(MORNING_CARE_ARRIVAL, settings.morningWatchArrival)
        .set(ORDER_TEXTBOOKS, settings.orderTextbooks)
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