/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables.records;


import java.time.OffsetDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;

import si.francebevk.db.tables.Pupil;


/**
 * Contains a single pupil
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PupilRecord extends UpdatableRecordImpl<PupilRecord> implements Record12<Long, String, String, String, Boolean, Short, Short, Short, Short, Short, String, OffsetDateTime> {

    private static final long serialVersionUID = 1859218770;

    /**
     * Setter for <code>public.pupil.id</code>. The pupil's unique ID
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pupil.id</code>. The pupil's unique ID
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pupil.name</code>. The pupil's name
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pupil.name</code>. The pupil's name
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.pupil.pupil_group</code>. The pupil's class
     */
    public void setPupilGroup(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pupil.pupil_group</code>. The pupil's class
     */
    public String getPupilGroup() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.pupil.access_code</code>. The pupil's access code required to access their profile
     */
    public void setAccessCode(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.pupil.access_code</code>. The pupil's access code required to access their profile
     */
    public String getAccessCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.pupil.extended_stay</code>. Indicates choice for pupil's inclusion in extended stay
     */
    public void setExtendedStay(Boolean value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.pupil.extended_stay</code>. Indicates choice for pupil's inclusion in extended stay
     */
    public Boolean getExtendedStay() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>public.pupil.leave_mon</code>. Indicates the minutes of day at which pupil will leave school on monday (null means student is not participating on that day)
     */
    public void setLeaveMon(Short value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.pupil.leave_mon</code>. Indicates the minutes of day at which pupil will leave school on monday (null means student is not participating on that day)
     */
    public Short getLeaveMon() {
        return (Short) get(5);
    }

    /**
     * Setter for <code>public.pupil.leave_tue</code>. Indicates the minutes of day at which pupil will leave school on tuesday (null means student is not participating on that day)
     */
    public void setLeaveTue(Short value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.pupil.leave_tue</code>. Indicates the minutes of day at which pupil will leave school on tuesday (null means student is not participating on that day)
     */
    public Short getLeaveTue() {
        return (Short) get(6);
    }

    /**
     * Setter for <code>public.pupil.leave_wed</code>. Indicates the minutes of day at which pupil will leave school on wednesday (null means student is not participating on that day)
     */
    public void setLeaveWed(Short value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.pupil.leave_wed</code>. Indicates the minutes of day at which pupil will leave school on wednesday (null means student is not participating on that day)
     */
    public Short getLeaveWed() {
        return (Short) get(7);
    }

    /**
     * Setter for <code>public.pupil.leave_thu</code>. Indicates the minutes of day at which pupil will leave school on thursday (null means student is not participating on that day)
     */
    public void setLeaveThu(Short value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.pupil.leave_thu</code>. Indicates the minutes of day at which pupil will leave school on thursday (null means student is not participating on that day)
     */
    public Short getLeaveThu() {
        return (Short) get(8);
    }

    /**
     * Setter for <code>public.pupil.leave_fri</code>. Indicates the minutes of day at which pupil will leave school on friday (null means student is not participating on that day)
     */
    public void setLeaveFri(Short value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.pupil.leave_fri</code>. Indicates the minutes of day at which pupil will leave school on friday (null means student is not participating on that day)
     */
    public Short getLeaveFri() {
        return (Short) get(9);
    }

    /**
     * Setter for <code>public.pupil.email</code>. The contact email via which the pupil may be contacted
     */
    public void setEmail(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.pupil.email</code>. The contact email via which the pupil may be contacted
     */
    public String getEmail() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.pupil.last_login</code>.
     */
    public void setLastLogin(OffsetDateTime value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.pupil.last_login</code>.
     */
    public OffsetDateTime getLastLogin() {
        return (OffsetDateTime) get(11);
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
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, String, String, String, Boolean, Short, Short, Short, Short, Short, String, OffsetDateTime> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, String, String, String, Boolean, Short, Short, Short, Short, Short, String, OffsetDateTime> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Pupil.PUPIL.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Pupil.PUPIL.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Pupil.PUPIL.PUPIL_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Pupil.PUPIL.ACCESS_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Pupil.PUPIL.EXTENDED_STAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field6() {
        return Pupil.PUPIL.LEAVE_MON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field7() {
        return Pupil.PUPIL.LEAVE_TUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field8() {
        return Pupil.PUPIL.LEAVE_WED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field9() {
        return Pupil.PUPIL.LEAVE_THU;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field10() {
        return Pupil.PUPIL.LEAVE_FRI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return Pupil.PUPIL.EMAIL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<OffsetDateTime> field12() {
        return Pupil.PUPIL.LAST_LOGIN;
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
        return getPupilGroup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getAccessCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component5() {
        return getExtendedStay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component6() {
        return getLeaveMon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component7() {
        return getLeaveTue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component8() {
        return getLeaveWed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component9() {
        return getLeaveThu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component10() {
        return getLeaveFri();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime component12() {
        return getLastLogin();
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
        return getPupilGroup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getAccessCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getExtendedStay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value6() {
        return getLeaveMon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value7() {
        return getLeaveTue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value8() {
        return getLeaveWed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value9() {
        return getLeaveThu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value10() {
        return getLeaveFri();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime value12() {
        return getLastLogin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value3(String value) {
        setPupilGroup(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value4(String value) {
        setAccessCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value5(Boolean value) {
        setExtendedStay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value6(Short value) {
        setLeaveMon(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value7(Short value) {
        setLeaveTue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value8(Short value) {
        setLeaveWed(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value9(Short value) {
        setLeaveThu(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value10(Short value) {
        setLeaveFri(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value11(String value) {
        setEmail(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord value12(OffsetDateTime value) {
        setLastLogin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PupilRecord values(Long value1, String value2, String value3, String value4, Boolean value5, Short value6, Short value7, Short value8, Short value9, Short value10, String value11, OffsetDateTime value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PupilRecord
     */
    public PupilRecord() {
        super(Pupil.PUPIL);
    }

    /**
     * Create a detached, initialised PupilRecord
     */
    public PupilRecord(Long id, String name, String pupilGroup, String accessCode, Boolean extendedStay, Short leaveMon, Short leaveTue, Short leaveWed, Short leaveThu, Short leaveFri, String email, OffsetDateTime lastLogin) {
        super(Pupil.PUPIL);

        set(0, id);
        set(1, name);
        set(2, pupilGroup);
        set(3, accessCode);
        set(4, extendedStay);
        set(5, leaveMon);
        set(6, leaveTue);
        set(7, leaveWed);
        set(8, leaveThu);
        set(9, leaveFri);
        set(10, email);
        set(11, lastLogin);
    }
}
