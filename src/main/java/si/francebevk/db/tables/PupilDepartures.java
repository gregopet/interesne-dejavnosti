/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import si.francebevk.db.Public;
import si.francebevk.db.enums.DayOfWeek;
import si.francebevk.db.tables.records.PupilDeparturesRecord;


/**
 * Pupil's departures as a proper table to be used in reports
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PupilDepartures extends TableImpl<PupilDeparturesRecord> {

    private static final long serialVersionUID = -1883691244;

    /**
     * The reference instance of <code>public.pupil_departures</code>
     */
    public static final PupilDepartures PUPIL_DEPARTURES = new PupilDepartures();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PupilDeparturesRecord> getRecordType() {
        return PupilDeparturesRecord.class;
    }

    /**
     * The column <code>public.pupil_departures.pupil_id</code>.
     */
    public final TableField<PupilDeparturesRecord, Long> PUPIL_ID = createField("pupil_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pupil_departures.day</code>.
     */
    public final TableField<PupilDeparturesRecord, DayOfWeek> DAY = createField("day", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(si.francebevk.db.enums.DayOfWeek.class), this, "");

    /**
     * The column <code>public.pupil_departures.leave_minutes</code>.
     */
    public final TableField<PupilDeparturesRecord, Short> LEAVE_MINUTES = createField("leave_minutes", org.jooq.impl.SQLDataType.SMALLINT, this, "");

    /**
     * Create a <code>public.pupil_departures</code> table reference
     */
    public PupilDepartures() {
        this(DSL.name("pupil_departures"), null);
    }

    /**
     * Create an aliased <code>public.pupil_departures</code> table reference
     */
    public PupilDepartures(String alias) {
        this(DSL.name(alias), PUPIL_DEPARTURES);
    }

    /**
     * Create an aliased <code>public.pupil_departures</code> table reference
     */
    public PupilDepartures(Name alias) {
        this(alias, PUPIL_DEPARTURES);
    }

    private PupilDepartures(Name alias, Table<PupilDeparturesRecord> aliased) {
        this(alias, aliased, null);
    }

    private PupilDepartures(Name alias, Table<PupilDeparturesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "Pupil's departures as a proper table to be used in reports");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDepartures as(String alias) {
        return new PupilDepartures(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDepartures as(Name alias) {
        return new PupilDepartures(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PupilDepartures rename(String name) {
        return new PupilDepartures(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PupilDepartures rename(Name name) {
        return new PupilDepartures(name, null);
    }
}