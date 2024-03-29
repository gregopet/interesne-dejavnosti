/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;

import si.francebevk.db.tables.Activity;
import si.francebevk.db.tables.ActivityLog;
import si.francebevk.db.tables.AuthorizedCompanion;
import si.francebevk.db.tables.ErrorLog;
import si.francebevk.db.tables.FlywaySchemaHistory;
import si.francebevk.db.tables.Pupil;
import si.francebevk.db.tables.PupilActivity;
import si.francebevk.db.tables.PupilGroup;
import si.francebevk.db.tables.records.ActivityLogRecord;
import si.francebevk.db.tables.records.ActivityRecord;
import si.francebevk.db.tables.records.AuthorizedCompanionRecord;
import si.francebevk.db.tables.records.ErrorLogRecord;
import si.francebevk.db.tables.records.FlywaySchemaHistoryRecord;
import si.francebevk.db.tables.records.PupilActivityRecord;
import si.francebevk.db.tables.records.PupilGroupRecord;
import si.francebevk.db.tables.records.PupilRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<ActivityRecord, Long> IDENTITY_ACTIVITY = Identities0.IDENTITY_ACTIVITY;
    public static final Identity<ActivityLogRecord, Long> IDENTITY_ACTIVITY_LOG = Identities0.IDENTITY_ACTIVITY_LOG;
    public static final Identity<AuthorizedCompanionRecord, Integer> IDENTITY_AUTHORIZED_COMPANION = Identities0.IDENTITY_AUTHORIZED_COMPANION;
    public static final Identity<ErrorLogRecord, Long> IDENTITY_ERROR_LOG = Identities0.IDENTITY_ERROR_LOG;
    public static final Identity<PupilRecord, Long> IDENTITY_PUPIL = Identities0.IDENTITY_PUPIL;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ActivityRecord> ACTIVITY_PKEY = UniqueKeys0.ACTIVITY_PKEY;
    public static final UniqueKey<ActivityLogRecord> ACTIVITY_LOG_PKEY = UniqueKeys0.ACTIVITY_LOG_PKEY;
    public static final UniqueKey<AuthorizedCompanionRecord> AUTHORIZED_COMPANION_PKEY = UniqueKeys0.AUTHORIZED_COMPANION_PKEY;
    public static final UniqueKey<ErrorLogRecord> ERROR_LOG_PKEY = UniqueKeys0.ERROR_LOG_PKEY;
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;
    public static final UniqueKey<PupilRecord> PUPIL_PKEY = UniqueKeys0.PUPIL_PKEY;
    public static final UniqueKey<PupilActivityRecord> PUPIL_ACTIVITY_PKEY = UniqueKeys0.PUPIL_ACTIVITY_PKEY;
    public static final UniqueKey<PupilGroupRecord> PUPIL_GROUP_PKEY = UniqueKeys0.PUPIL_GROUP_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ActivityLogRecord, PupilRecord> ACTIVITY_LOG__ACTIVITY_LOG_PUPIL_ID_FKEY = ForeignKeys0.ACTIVITY_LOG__ACTIVITY_LOG_PUPIL_ID_FKEY;
    public static final ForeignKey<AuthorizedCompanionRecord, PupilRecord> AUTHORIZED_COMPANION__AUTHORIZED_COMPANION_PUPIL_ID_FKEY = ForeignKeys0.AUTHORIZED_COMPANION__AUTHORIZED_COMPANION_PUPIL_ID_FKEY;
    public static final ForeignKey<ErrorLogRecord, PupilRecord> ERROR_LOG__ERROR_LOG_PUPIL_ID_FKEY = ForeignKeys0.ERROR_LOG__ERROR_LOG_PUPIL_ID_FKEY;
    public static final ForeignKey<PupilRecord, PupilGroupRecord> PUPIL__PUPIL_PUPIL_GROUP_FKEY = ForeignKeys0.PUPIL__PUPIL_PUPIL_GROUP_FKEY;
    public static final ForeignKey<PupilActivityRecord, PupilRecord> PUPIL_ACTIVITY__PUPIL_ACTIVITY_PUPIL_ID_FKEY = ForeignKeys0.PUPIL_ACTIVITY__PUPIL_ACTIVITY_PUPIL_ID_FKEY;
    public static final ForeignKey<PupilActivityRecord, ActivityRecord> PUPIL_ACTIVITY__PUPIL_ACTIVITY_ACTIVITY_ID_FKEY = ForeignKeys0.PUPIL_ACTIVITY__PUPIL_ACTIVITY_ACTIVITY_ID_FKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<ActivityRecord, Long> IDENTITY_ACTIVITY = createIdentity(Activity.ACTIVITY, Activity.ACTIVITY.ID);
        public static Identity<ActivityLogRecord, Long> IDENTITY_ACTIVITY_LOG = createIdentity(ActivityLog.ACTIVITY_LOG, ActivityLog.ACTIVITY_LOG.ID);
        public static Identity<AuthorizedCompanionRecord, Integer> IDENTITY_AUTHORIZED_COMPANION = createIdentity(AuthorizedCompanion.AUTHORIZED_COMPANION, AuthorizedCompanion.AUTHORIZED_COMPANION.ID);
        public static Identity<ErrorLogRecord, Long> IDENTITY_ERROR_LOG = createIdentity(ErrorLog.ERROR_LOG, ErrorLog.ERROR_LOG.ID);
        public static Identity<PupilRecord, Long> IDENTITY_PUPIL = createIdentity(Pupil.PUPIL, Pupil.PUPIL.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<ActivityRecord> ACTIVITY_PKEY = createUniqueKey(Activity.ACTIVITY, "activity_pkey", Activity.ACTIVITY.ID);
        public static final UniqueKey<ActivityLogRecord> ACTIVITY_LOG_PKEY = createUniqueKey(ActivityLog.ACTIVITY_LOG, "activity_log_pkey", ActivityLog.ACTIVITY_LOG.ID);
        public static final UniqueKey<AuthorizedCompanionRecord> AUTHORIZED_COMPANION_PKEY = createUniqueKey(AuthorizedCompanion.AUTHORIZED_COMPANION, "authorized_companion_pkey", AuthorizedCompanion.AUTHORIZED_COMPANION.ID);
        public static final UniqueKey<ErrorLogRecord> ERROR_LOG_PKEY = createUniqueKey(ErrorLog.ERROR_LOG, "error_log_pkey", ErrorLog.ERROR_LOG.ID);
        public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK);
        public static final UniqueKey<PupilRecord> PUPIL_PKEY = createUniqueKey(Pupil.PUPIL, "pupil_pkey", Pupil.PUPIL.ID);
        public static final UniqueKey<PupilActivityRecord> PUPIL_ACTIVITY_PKEY = createUniqueKey(PupilActivity.PUPIL_ACTIVITY, "pupil_activity_pkey", PupilActivity.PUPIL_ACTIVITY.ACTIVITY_ID, PupilActivity.PUPIL_ACTIVITY.PUPIL_ID);
        public static final UniqueKey<PupilGroupRecord> PUPIL_GROUP_PKEY = createUniqueKey(PupilGroup.PUPIL_GROUP, "pupil_group_pkey", PupilGroup.PUPIL_GROUP.NAME);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<ActivityLogRecord, PupilRecord> ACTIVITY_LOG__ACTIVITY_LOG_PUPIL_ID_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_PKEY, ActivityLog.ACTIVITY_LOG, "activity_log__activity_log_pupil_id_fkey", ActivityLog.ACTIVITY_LOG.PUPIL_ID);
        public static final ForeignKey<AuthorizedCompanionRecord, PupilRecord> AUTHORIZED_COMPANION__AUTHORIZED_COMPANION_PUPIL_ID_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_PKEY, AuthorizedCompanion.AUTHORIZED_COMPANION, "authorized_companion__authorized_companion_pupil_id_fkey", AuthorizedCompanion.AUTHORIZED_COMPANION.PUPIL_ID);
        public static final ForeignKey<ErrorLogRecord, PupilRecord> ERROR_LOG__ERROR_LOG_PUPIL_ID_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_PKEY, ErrorLog.ERROR_LOG, "error_log__error_log_pupil_id_fkey", ErrorLog.ERROR_LOG.PUPIL_ID);
        public static final ForeignKey<PupilRecord, PupilGroupRecord> PUPIL__PUPIL_PUPIL_GROUP_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_GROUP_PKEY, Pupil.PUPIL, "pupil__pupil_pupil_group_fkey", Pupil.PUPIL.PUPIL_GROUP);
        public static final ForeignKey<PupilActivityRecord, PupilRecord> PUPIL_ACTIVITY__PUPIL_ACTIVITY_PUPIL_ID_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_PKEY, PupilActivity.PUPIL_ACTIVITY, "pupil_activity__pupil_activity_pupil_id_fkey", PupilActivity.PUPIL_ACTIVITY.PUPIL_ID);
        public static final ForeignKey<PupilActivityRecord, ActivityRecord> PUPIL_ACTIVITY__PUPIL_ACTIVITY_ACTIVITY_ID_FKEY = createForeignKey(si.francebevk.db.Keys.ACTIVITY_PKEY, PupilActivity.PUPIL_ACTIVITY, "pupil_activity__pupil_activity_activity_id_fkey", PupilActivity.PUPIL_ACTIVITY.ACTIVITY_ID);
    }
}
