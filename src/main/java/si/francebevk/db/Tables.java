/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db;


import javax.annotation.Generated;

import si.francebevk.db.tables.Activity;
import si.francebevk.db.tables.ActivityLog;
import si.francebevk.db.tables.ActivitySlots;
import si.francebevk.db.tables.AuthorizedCompanion;
import si.francebevk.db.tables.DeparturesHourlyReport;
import si.francebevk.db.tables.ErrorLog;
import si.francebevk.db.tables.FlywaySchemaHistory;
import si.francebevk.db.tables.Pupil;
import si.francebevk.db.tables.PupilActivity;
import si.francebevk.db.tables.PupilDepartures;
import si.francebevk.db.tables.PupilGroup;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * Contains activities children can participate in
     */
    public static final Activity ACTIVITY = si.francebevk.db.tables.Activity.ACTIVITY;

    /**
     * Records the activity related to a pupil
     */
    public static final ActivityLog ACTIVITY_LOG = si.francebevk.db.tables.ActivityLog.ACTIVITY_LOG;

    /**
     * All the various slots belonging to activities
     */
    public static final ActivitySlots ACTIVITY_SLOTS = si.francebevk.db.tables.ActivitySlots.ACTIVITY_SLOTS;

    /**
     * The table <code>public.authorized_companion</code>.
     */
    public static final AuthorizedCompanion AUTHORIZED_COMPANION = si.francebevk.db.tables.AuthorizedCompanion.AUTHORIZED_COMPANION;

    /**
     * A review of hourly activity per-class
     */
    public static final DeparturesHourlyReport DEPARTURES_HOURLY_REPORT = si.francebevk.db.tables.DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT;

    /**
     * Contains errors that may have happened during the process
     */
    public static final ErrorLog ERROR_LOG = si.francebevk.db.tables.ErrorLog.ERROR_LOG;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = si.francebevk.db.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * Contains a single pupil
     */
    public static final Pupil PUPIL = si.francebevk.db.tables.Pupil.PUPIL;

    /**
     * A mapping table between pupils and their chosen activities
     */
    public static final PupilActivity PUPIL_ACTIVITY = si.francebevk.db.tables.PupilActivity.PUPIL_ACTIVITY;

    /**
     * Pupil's departures as a proper table to be used in reports
     */
    public static final PupilDepartures PUPIL_DEPARTURES = si.francebevk.db.tables.PupilDepartures.PUPIL_DEPARTURES;

    /**
     * A class of pupils
     */
    public static final PupilGroup PUPIL_GROUP = si.francebevk.db.tables.PupilGroup.PUPIL_GROUP;
}
