/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.udt.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UDTRecordImpl;

import si.francebevk.db.enums.DayOfWeek;
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
public class TimeSlotRecord extends UDTRecordImpl<TimeSlotRecord> implements Record3<DayOfWeek, Short, Short> {

    private static final long serialVersionUID = -521102830;

    /**
     * Setter for <code>public.time_slot.day</code>.
     */
    public void setDay(DayOfWeek value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.time_slot.day</code>.
     */
    public DayOfWeek getDay() {
        return (DayOfWeek) get(0);
    }

    /**
     * Setter for <code>public.time_slot.start_minutes</code>.
     */
    public void setStartMinutes(Short value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.time_slot.start_minutes</code>.
     */
    public Short getStartMinutes() {
        return (Short) get(1);
    }

    /**
     * Setter for <code>public.time_slot.end_minutes</code>.
     */
    public void setEndMinutes(Short value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.time_slot.end_minutes</code>.
     */
    public Short getEndMinutes() {
        return (Short) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<DayOfWeek, Short, Short> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<DayOfWeek, Short, Short> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<DayOfWeek> field1() {
        return TimeSlot.DAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field2() {
        return TimeSlot.START_MINUTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field3() {
        return TimeSlot.END_MINUTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DayOfWeek component1() {
        return getDay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component2() {
        return getStartMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component3() {
        return getEndMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DayOfWeek value1() {
        return getDay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value2() {
        return getStartMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value3() {
        return getEndMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord value1(DayOfWeek value) {
        setDay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord value2(Short value) {
        setStartMinutes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord value3(Short value) {
        setEndMinutes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord values(DayOfWeek value1, Short value2, Short value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TimeSlotRecord
     */
    public TimeSlotRecord() {
        super(TimeSlot.TIME_SLOT);
    }

    /**
     * Create a detached, initialised TimeSlotRecord
     */
    public TimeSlotRecord(DayOfWeek day, Short startMinutes, Short endMinutes) {
        super(TimeSlot.TIME_SLOT);

        set(0, day);
        set(1, startMinutes);
        set(2, endMinutes);
    }
}