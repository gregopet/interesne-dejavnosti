package si.francebevk.interesnedejavnosti

import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.jooq.util.postgres.PostgresDSL
import si.francebevk.db.Tables.*
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.dto.ActivityVacancy

object ActivityDAO {

    fun getActivitiesForClass(year: Short, jooq: DSLContext) = with(ACTIVITY) {
        jooq
        .selectFrom(ACTIVITY)
        .where(AVAILABLE_TO_YEARS.contains(arrayOf(year)))
        .fetch()
    }

    fun getVacancyForPupil(pupilId: Long, jooq: DSLContext): List<ActivityVacancy> {
        val existsClause = with(PUPIL_ACTIVITY.`as`("myactivity")) {
            select().from(this).where(PUPIL_ID.eq(pupilId), ACTIVITY_ID.eq(ACTIVITY.ID))
        }

        return jooq
            .select(ACTIVITY.ID, ACTIVITY.MAX_PUPILS, count(PUPIL_ACTIVITY.PUPIL_ID), field(exists(existsClause)))
            .from(ACTIVITY)
            .leftJoin(PUPIL_ACTIVITY).on(PUPIL_ACTIVITY.ACTIVITY_ID.eq(ACTIVITY.ID))
            .join(PUPIL).on(PUPIL.ID.eq(pupilId))
            .join(PUPIL_GROUP).on(PUPIL.PUPIL_GROUP.eq(PUPIL_GROUP.NAME))
            .where(
                ACTIVITY.AVAILABLE_TO_YEARS.contains(PostgresDSL.array(PUPIL_GROUP.YEAR))
            )
            .groupBy(ACTIVITY.ID, ACTIVITY.SLOTS)
            .fetch { ActivityVacancy(it.value1(), it.value2() - it.value3(), it.value4()) }
    }

    fun getSelectedActivityIds(pupilId: Long, jooq: DSLContext) = with(PUPIL_ACTIVITY) {
        jooq.select(ACTIVITY_ID).from(PUPIL_ACTIVITY).where(PUPIL_ID.eq(pupilId)).fetch().map { it.value1() }.toSet()
    }

    fun getSelectedActivitiesForPupil(pupilId: Long, jooq: DSLContext): List<ActivityRecord> =
        jooq.select(*ACTIVITY.fields())
        .from(ACTIVITY)
        .join(PUPIL_ACTIVITY).on(ACTIVITY.ID.eq(PUPIL_ACTIVITY.ACTIVITY_ID))
        .where(PUPIL_ACTIVITY.PUPIL_ID.eq(pupilId))
        .fetch()
        .into(ACTIVITY)

    /** Counts how many activites (of those passed in) have limited membership */
    fun limitedActivityCount(activities: List<Long>, trans: DSLContext): Int = with(ACTIVITY) {
        trans.select(count()).from(ACTIVITY).where(
            ID.`in`(activities),
            MAX_PUPILS.lt(100)
        ).fetchOne().value1()
    }

    /**
     * Stores selected activities for the pupil and returns any activities that may have too many pupils registered
     * already. Rolls back the transaction in case the registration could not be made and throws an exception.
     *
     * @throws ActivityFullException when one or more activites were full already!
     */
    fun storeSelectedActivityIds(pupilId: Long, activityIds: List<Long>, trans: DSLContext): Unit = with(PUPIL_ACTIVITY) {
        // lock selected activities
        trans.select(ACTIVITY.ID).from(ACTIVITY).where(ACTIVITY.ID.`in`(*activityIds.toTypedArray())).orderBy(ACTIVITY.ID).forUpdate()

        trans.deleteFrom(PUPIL_ACTIVITY).where(PUPIL_ID.eq(pupilId)).execute()

        trans.insertInto(PUPIL_ACTIVITY, PUPIL_ID, ACTIVITY_ID).also { query ->
            activityIds.forEach { actId -> query.values(pupilId, actId) }
        }.execute()

        val overdrawn = trans
        .select(*ACTIVITY.fields())
        .from(ACTIVITY)
        .where(
                selectCount().from(PUPIL_ACTIVITY).where(PUPIL_ACTIVITY.ACTIVITY_ID.eq(ACTIVITY.ID)).asField<Short>().gt(ACTIVITY.MAX_PUPILS)
        )
        .fetch()
        .into(ACTIVITY)

        if (overdrawn.isNotEmpty) throw ActivityFullException(overdrawn)
    }

}
