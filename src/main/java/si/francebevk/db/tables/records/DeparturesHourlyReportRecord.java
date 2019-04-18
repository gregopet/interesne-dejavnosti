/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;

import si.francebevk.db.enums.DayOfWeek;
import si.francebevk.db.tables.DeparturesHourlyReport;
import si.francebevk.db.udt.records.NamedStringsRecord;


/**
 * A review of hourly activity per-class
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DeparturesHourlyReportRecord extends TableRecordImpl<DeparturesHourlyReportRecord> implements Record6<String, DayOfWeek, Short, String[], String[], NamedStringsRecord[]> {

    private static final long serialVersionUID = -562130256;

    /**
     * Setter for <code>public.departures_hourly_report.pupil_group</code>.
     */
    public void setPupilGroup(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.pupil_group</code>.
     */
    public String getPupilGroup() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.departures_hourly_report.day</code>.
     */
    public void setDay(DayOfWeek value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.day</code>.
     */
    public DayOfWeek getDay() {
        return (DayOfWeek) get(1);
    }

    /**
     * Setter for <code>public.departures_hourly_report.leave_minutes</code>.
     */
    public void setLeaveMinutes(Short value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.leave_minutes</code>.
     */
    public Short getLeaveMinutes() {
        return (Short) get(2);
    }

    /**
     * Setter for <code>public.departures_hourly_report.still_here</code>.
     */
    public void setStillHere(String... value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.still_here</code>.
     */
    public String[] getStillHere() {
        return (String[]) get(3);
    }

    /**
     * Setter for <code>public.departures_hourly_report.leave</code>.
     */
    public void setLeave(String... value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.leave</code>.
     */
    public String[] getLeave() {
        return (String[]) get(4);
    }

    /**
     * Setter for <code>public.departures_hourly_report.at_activities</code>.
     */
    public void setAtActivities(NamedStringsRecord... value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.departures_hourly_report.at_activities</code>.
     */
    public NamedStringsRecord[] getAtActivities() {
        return (NamedStringsRecord[]) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, DayOfWeek, Short, String[], String[], NamedStringsRecord[]> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, DayOfWeek, Short, String[], String[], NamedStringsRecord[]> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.PUPIL_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<DayOfWeek> field2() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.DAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field3() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.LEAVE_MINUTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field4() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.STILL_HERE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field5() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.LEAVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<NamedStringsRecord[]> field6() {
        return DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT.AT_ACTIVITIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getPupilGroup();
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
    public String[] component4() {
        return getStillHere();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] component5() {
        return getLeave();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamedStringsRecord[] component6() {
        return getAtActivities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getPupilGroup();
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
    public String[] value4() {
        return getStillHere();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] value5() {
        return getLeave();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamedStringsRecord[] value6() {
        return getAtActivities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value1(String value) {
        setPupilGroup(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value2(DayOfWeek value) {
        setDay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value3(Short value) {
        setLeaveMinutes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value4(String... value) {
        setStillHere(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value5(String... value) {
        setLeave(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord value6(NamedStringsRecord... value) {
        setAtActivities(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeparturesHourlyReportRecord values(String value1, DayOfWeek value2, Short value3, String[] value4, String[] value5, NamedStringsRecord[] value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DeparturesHourlyReportRecord
     */
    public DeparturesHourlyReportRecord() {
        super(DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT);
    }

    /**
     * Create a detached, initialised DeparturesHourlyReportRecord
     */
    public DeparturesHourlyReportRecord(String pupilGroup, DayOfWeek day, Short leaveMinutes, String[] stillHere, String[] leave, NamedStringsRecord[] atActivities) {
        super(DeparturesHourlyReport.DEPARTURES_HOURLY_REPORT);

        set(0, pupilGroup);
        set(1, day);
        set(2, leaveMinutes);
        set(3, stillHere);
        set(4, leave);
        set(5, atActivities);
    }
}
