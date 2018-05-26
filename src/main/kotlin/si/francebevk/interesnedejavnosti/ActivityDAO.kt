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

    fun storeSelectedActivityIds(pupilId: Long, activityIds: List<Long>, trans: DSLContext) = with(PUPIL_ACTIVITY) {
        trans.deleteFrom(PUPIL_ACTIVITY).where(PUPIL_ID.eq(pupilId)).execute()
        trans.insertInto(PUPIL_ACTIVITY, PUPIL_ID, ACTIVITY_ID).also { query ->
            activityIds.forEach { actId -> query.values(pupilId, actId ) }
        }
        .execute()
    }

}
