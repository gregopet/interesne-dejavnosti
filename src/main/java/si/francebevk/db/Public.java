/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.impl.SchemaImpl;

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
import si.francebevk.db.udt.NamedStrings;
import si.francebevk.db.udt.TimeSlot;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -72243996;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * Contains activities children can participate in
     */
    public final Activity ACTIVITY = si.francebevk.db.tables.Activity.ACTIVITY;

    /**
     * Records the activity related to a pupil
     */
    public final ActivityLog ACTIVITY_LOG = si.francebevk.db.tables.ActivityLog.ACTIVITY_LOG;

    /**
     * All the various slots belonging to activities
     */
    public final ActivitySlots ACTIVITY_SLOTS = si.francebevk.db.tables.ActivitySlots.ACTIVITY_SLOTS;

    /**
     * The table <code>public.authorized_companion</code>.
     */
    public final AuthorizedCompanion AUTHORIZED_COMPANION = si.francebevk.db.tables.AuthorizedCompanion.AUTHORIZED_COMPANION;

    /**
     * A review of hourly activity per-class
     */
    public final DeparturesHourlyReport DEPARTURES_HOURLY_REPORT = si.francebevk.db.tables.DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT;

    /**
     * Contains errors that may have happened during the process
     */
    public final ErrorLog ERROR_LOG = si.francebevk.db.tables.ErrorLog.ERROR_LOG;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = si.francebevk.db.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * Contains a single pupil
     */
    public final Pupil PUPIL = si.francebevk.db.tables.Pupil.PUPIL;

    /**
     * A mapping table between pupils and their chosen activities
     */
    public final PupilActivity PUPIL_ACTIVITY = si.francebevk.db.tables.PupilActivity.PUPIL_ACTIVITY;

    /**
     * Pupil's departures as a proper table to be used in reports
     */
    public final PupilDepartures PUPIL_DEPARTURES = si.francebevk.db.tables.PupilDepartures.PUPIL_DEPARTURES;

    /**
     * A class of pupils
     */
    public final PupilGroup PUPIL_GROUP = si.francebevk.db.tables.PupilGroup.PUPIL_GROUP;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        List result = new ArrayList();
        result.addAll(getSequences0());
        return result;
    }

    private final List<Sequence<?>> getSequences0() {
        return Arrays.<Sequence<?>>asList(
            Sequences.ACTIVITY_ID_SEQ,
            Sequences.ACTIVITY_LOG_ID_SEQ,
            Sequences.AUTHORIZED_COMPANION_ID_SEQ,
            Sequences.ERROR_LOG_ID_SEQ,
            Sequences.PUPIL_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Activity.ACTIVITY,
            ActivityLog.ACTIVITY_LOG,
            ActivitySlots.ACTIVITY_SLOTS,
            AuthorizedCompanion.AUTHORIZED_COMPANION,
            DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT,
            ErrorLog.ERROR_LOG,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            Pupil.PUPIL,
            PupilActivity.PUPIL_ACTIVITY,
            PupilDepartures.PUPIL_DEPARTURES,
            PupilGroup.PUPIL_GROUP);
    }

    @Override
    public final List<UDT<?>> getUDTs() {
        List result = new ArrayList();
        result.addAll(getUDTs0());
        return result;
    }

    private final List<UDT<?>> getUDTs0() {
        return Arrays.<UDT<?>>asList(
            NamedStrings.NAMED_STRINGS,
            TimeSlot.TIME_SLOT);
    }
}
