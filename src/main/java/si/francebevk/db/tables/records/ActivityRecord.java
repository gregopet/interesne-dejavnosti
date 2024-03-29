/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import si.francebevk.db.tables.Activity;
import si.francebevk.db.udt.records.TimeSlotRecord;


/**
 * Contains activities children can participate in
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ActivityRecord extends UpdatableRecordImpl<ActivityRecord> implements Record8<Long, String, String, String, Short[], TimeSlotRecord[], String, Short> {

    private static final long serialVersionUID = -1914820426;

    /**
     * Setter for <code>public.activity.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.activity.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.activity.name</code>. Name of this activity
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.activity.name</code>. Name of this activity
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.activity.description</code>. The longer description of this activity
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.activity.description</code>. The longer description of this activity
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.activity.leader</code>. Name of the person leading this activity
     */
    public void setLeader(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.activity.leader</code>. Name of the person leading this activity
     */
    public String getLeader() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.activity.available_to_years</code>. All the student class years this activity is available to
     */
    public void setAvailableToYears(Short... value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.activity.available_to_years</code>. All the student class years this activity is available to
     */
    public Short[] getAvailableToYears() {
        return (Short[]) get(4);
    }

    /**
     * Setter for <code>public.activity.slots</code>. The times during which this activity takes place
     */
    public void setSlots(TimeSlotRecord... value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.activity.slots</code>. The times during which this activity takes place
     */
    public TimeSlotRecord[] getSlots() {
        return (TimeSlotRecord[]) get(5);
    }

    /**
     * Setter for <code>public.activity.cost</code>. A textual description of the cost for this activity
     */
    public void setCost(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.activity.cost</code>. A textual description of the cost for this activity
     */
    public String getCost() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.activity.max_pupils</code>. Maximum number of pupils for this activity
     */
    public void setMaxPupils(Short value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.activity.max_pupils</code>. Maximum number of pupils for this activity
     */
    public Short getMaxPupils() {
        return (Short) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, String, String, String, Short[], TimeSlotRecord[], String, Short> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, String, String, String, Short[], TimeSlotRecord[], String, Short> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Activity.ACTIVITY.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Activity.ACTIVITY.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Activity.ACTIVITY.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Activity.ACTIVITY.LEADER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short[]> field5() {
        return Activity.ACTIVITY.AVAILABLE_TO_YEARS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<TimeSlotRecord[]> field6() {
        return Activity.ACTIVITY.SLOTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Activity.ACTIVITY.COST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field8() {
        return Activity.ACTIVITY.MAX_PUPILS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getLeader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short[] component5() {
        return getAvailableToYears();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord[] component6() {
        return getSlots();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getCost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component8() {
        return getMaxPupils();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getLeader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short[] value5() {
        return getAvailableToYears();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSlotRecord[] value6() {
        return getSlots();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getCost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value8() {
        return getMaxPupils();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value3(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value4(String value) {
        setLeader(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value5(Short... value) {
        setAvailableToYears(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value6(TimeSlotRecord... value) {
        setSlots(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value7(String value) {
        setCost(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord value8(Short value) {
        setMaxPupils(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityRecord values(Long value1, String value2, String value3, String value4, Short[] value5, TimeSlotRecord[] value6, String value7, Short value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ActivityRecord
     */
    public ActivityRecord() {
        super(Activity.ACTIVITY);
    }

    /**
     * Create a detached, initialised ActivityRecord
     */
    public ActivityRecord(Long id, String name, String description, String leader, Short[] availableToYears, TimeSlotRecord[] slots, String cost, Short maxPupils) {
        super(Activity.ACTIVITY);

        set(0, id);
        set(1, name);
        set(2, description);
        set(3, leader);
        set(4, availableToYears);
        set(5, slots);
        set(6, cost);
        set(7, maxPupils);
    }
}
