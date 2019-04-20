/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;

import si.francebevk.db.enums.DayOfWeek;
import si.francebevk.db.tables.PupilDepartures;


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
public class PupilDeparturesRecord extends TableRecordImpl<PupilDeparturesRecord> implements Record3<Long, DayOfWeek, Short> {

    private static final long serialVersionUID = -153105748;

    /**
     * Setter for <code>public.pupil_departures.pupil_id</code>.
     */
    public void setPupilId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pupil_departures.pupil_id</code>.
     */
    public Long getPupilId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pupil_departures.day</code>.
     */
    public void setDay(DayOfWeek value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pupil_departures.day</code>.
     */
    public DayOfWeek getDay() {
        return (DayOfWeek) get(1);
    }

    /**
     * Setter for <code>public.pupil_departures.leave_minutes</code>.
     */
    public void setLeaveMinutes(Short value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pupil_departures.leave_minutes</code>.
     */
    public Short getLeaveMinutes() {
        return (Short) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, DayOfWeek, Short> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, DayOfWeek, Short> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return PupilDepartures.PUPIL_DEPARTURES.PUPIL_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<DayOfWeek> field2() {
        return PupilDepartures.PUPIL_DEPARTURES.DAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field3() {
        return PupilDepartures.PUPIL_DEPARTURES.LEAVE_MINUTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getPupilId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DayOfWeek component2() {
        return getDay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component3() {
        return getLeaveMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getPupilId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DayOfWeek value2() {
        return getDay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value3() {
        return getLeaveMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDeparturesRecord value1(Long value) {
        setPupilId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDeparturesRecord value2(DayOfWeek value) {
        setDay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDeparturesRecord value3(Short value) {
        setLeaveMinutes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilDeparturesRecord values(Long value1, DayOfWeek value2, Short value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PupilDeparturesRecord
     */
    public PupilDeparturesRecord() {
        super(PupilDepartures.PUPIL_DEPARTURES);
    }

    /**
     * Create a detached, initialised PupilDeparturesRecord
     */
    public PupilDeparturesRecord(Long pupilId, DayOfWeek day, Short leaveMinutes) {
        super(PupilDepartures.PUPIL_DEPARTURES);

        set(0, pupilId);
        set(1, day);
        set(2, leaveMinutes);
    }
}