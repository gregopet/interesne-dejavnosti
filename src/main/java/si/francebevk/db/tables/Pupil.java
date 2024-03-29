/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import si.francebevk.db.Keys;
import si.francebevk.db.Public;
import si.francebevk.db.tables.records.PupilRecord;


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
public class Pupil extends TableImpl<PupilRecord> {

    private static final long serialVersionUID = 1938022594;

    /**
     * The reference instance of <code>public.pupil</code>
     */
    public static final Pupil PUPIL = new Pupil();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PupilRecord> getRecordType() {
        return PupilRecord.class;
    }

    /**
     * The column <code>public.pupil.id</code>. The pupil's unique ID
     */
    public final TableField<PupilRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('pupil_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "The pupil's unique ID");

    /**
     * The column <code>public.pupil.pupil_group</code>. The pupil's class
     */
    public final TableField<PupilRecord, String> PUPIL_GROUP = createField("pupil_group", org.jooq.impl.SQLDataType.VARCHAR(2).nullable(false), this, "The pupil's class");

    /**
     * The column <code>public.pupil.access_code</code>. The pupil's access code required to access their profile
     */
    public final TableField<PupilRecord, String> ACCESS_CODE = createField("access_code", org.jooq.impl.SQLDataType.CLOB, this, "The pupil's access code required to access their profile");

    /**
     * The column <code>public.pupil.extended_stay</code>. Indicates choice for pupil's inclusion in extended stay
     */
    public final TableField<PupilRecord, Boolean> EXTENDED_STAY = createField("extended_stay", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "Indicates choice for pupil's inclusion in extended stay");

    /**
     * The column <code>public.pupil.leave_mon</code>. Indicates the minutes of day at which pupil will leave school on monday (null means student is not participating on that day)
     */
    public final TableField<PupilRecord, Short> LEAVE_MON = createField("leave_mon", org.jooq.impl.SQLDataType.SMALLINT, this, "Indicates the minutes of day at which pupil will leave school on monday (null means student is not participating on that day)");

    /**
     * The column <code>public.pupil.leave_tue</code>. Indicates the minutes of day at which pupil will leave school on tuesday (null means student is not participating on that day)
     */
    public final TableField<PupilRecord, Short> LEAVE_TUE = createField("leave_tue", org.jooq.impl.SQLDataType.SMALLINT, this, "Indicates the minutes of day at which pupil will leave school on tuesday (null means student is not participating on that day)");

    /**
     * The column <code>public.pupil.leave_wed</code>. Indicates the minutes of day at which pupil will leave school on wednesday (null means student is not participating on that day)
     */
    public final TableField<PupilRecord, Short> LEAVE_WED = createField("leave_wed", org.jooq.impl.SQLDataType.SMALLINT, this, "Indicates the minutes of day at which pupil will leave school on wednesday (null means student is not participating on that day)");

    /**
     * The column <code>public.pupil.leave_thu</code>. Indicates the minutes of day at which pupil will leave school on thursday (null means student is not participating on that day)
     */
    public final TableField<PupilRecord, Short> LEAVE_THU = createField("leave_thu", org.jooq.impl.SQLDataType.SMALLINT, this, "Indicates the minutes of day at which pupil will leave school on thursday (null means student is not participating on that day)");

    /**
     * The column <code>public.pupil.leave_fri</code>. Indicates the minutes of day at which pupil will leave school on friday (null means student is not participating on that day)
     */
    public final TableField<PupilRecord, Short> LEAVE_FRI = createField("leave_fri", org.jooq.impl.SQLDataType.SMALLINT, this, "Indicates the minutes of day at which pupil will leave school on friday (null means student is not participating on that day)");

    /**
     * The column <code>public.pupil.last_login</code>.
     */
    public final TableField<PupilRecord, OffsetDateTime> LAST_LOGIN = createField("last_login", org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE, this, "");

    /**
     * The column <code>public.pupil.emails</code>. All the emails to which messages for this pupil should be sent
     */
    public final TableField<PupilRecord, String[]> EMAILS = createField("emails", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "All the emails to which messages for this pupil should be sent");

    /**
     * The column <code>public.pupil.welcome_email_sent</code>. Set to true once an email was successfuly sent to this pupil
     */
    public final TableField<PupilRecord, Boolean> WELCOME_EMAIL_SENT = createField("welcome_email_sent", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "Set to true once an email was successfuly sent to this pupil");

    /**
     * The column <code>public.pupil.first_name</code>. First name of the pupil
     */
    public final TableField<PupilRecord, String> FIRST_NAME = createField("first_name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "First name of the pupil");

    /**
     * The column <code>public.pupil.last_name</code>. Last name of the pupil
     */
    public final TableField<PupilRecord, String> LAST_NAME = createField("last_name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "Last name of the pupil");

    /**
     * The column <code>public.pupil.can_leave_alone</code>. If true, younger pupils can still leave school without an escort
     */
    public final TableField<PupilRecord, Boolean> CAN_LEAVE_ALONE = createField("can_leave_alone", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "If true, younger pupils can still leave school without an escort");

    /**
     * The column <code>public.pupil.morning_care_arrival</code>. If not null then the pupil will arrive into morning care at this time
     */
    public final TableField<PupilRecord, Short> MORNING_CARE_ARRIVAL = createField("morning_care_arrival", org.jooq.impl.SQLDataType.SMALLINT, this, "If not null then the pupil will arrive into morning care at this time");

    /**
     * The column <code>public.pupil.order_textbooks</code>. Did the pupil order textbooks from the school's textbook fund?
     */
    public final TableField<PupilRecord, Boolean> ORDER_TEXTBOOKS = createField("order_textbooks", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "Did the pupil order textbooks from the school's textbook fund?");

    /**
     * The column <code>public.pupil.morning_snack_mon</code>.
     */
    public final TableField<PupilRecord, Boolean> MORNING_SNACK_MON = createField("morning_snack_mon", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.morning_snack_tue</code>.
     */
    public final TableField<PupilRecord, Boolean> MORNING_SNACK_TUE = createField("morning_snack_tue", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.morning_snack_wed</code>.
     */
    public final TableField<PupilRecord, Boolean> MORNING_SNACK_WED = createField("morning_snack_wed", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.morning_snack_thu</code>.
     */
    public final TableField<PupilRecord, Boolean> MORNING_SNACK_THU = createField("morning_snack_thu", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.morning_snack_fri</code>.
     */
    public final TableField<PupilRecord, Boolean> MORNING_SNACK_FRI = createField("morning_snack_fri", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.afternoon_snack_mon</code>.
     */
    public final TableField<PupilRecord, Boolean> AFTERNOON_SNACK_MON = createField("afternoon_snack_mon", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.afternoon_snack_tue</code>.
     */
    public final TableField<PupilRecord, Boolean> AFTERNOON_SNACK_TUE = createField("afternoon_snack_tue", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.afternoon_snack_wed</code>.
     */
    public final TableField<PupilRecord, Boolean> AFTERNOON_SNACK_WED = createField("afternoon_snack_wed", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.afternoon_snack_thu</code>.
     */
    public final TableField<PupilRecord, Boolean> AFTERNOON_SNACK_THU = createField("afternoon_snack_thu", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.afternoon_snack_fri</code>.
     */
    public final TableField<PupilRecord, Boolean> AFTERNOON_SNACK_FRI = createField("afternoon_snack_fri", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.lunch_mon</code>.
     */
    public final TableField<PupilRecord, Boolean> LUNCH_MON = createField("lunch_mon", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.lunch_tue</code>.
     */
    public final TableField<PupilRecord, Boolean> LUNCH_TUE = createField("lunch_tue", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.lunch_wed</code>.
     */
    public final TableField<PupilRecord, Boolean> LUNCH_WED = createField("lunch_wed", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.lunch_thu</code>.
     */
    public final TableField<PupilRecord, Boolean> LUNCH_THU = createField("lunch_thu", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pupil.lunch_fri</code>.
     */
    public final TableField<PupilRecord, Boolean> LUNCH_FRI = createField("lunch_fri", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>public.pupil</code> table reference
     */
    public Pupil() {
        this(DSL.name("pupil"), null);
    }

    /**
     * Create an aliased <code>public.pupil</code> table reference
     */
    public Pupil(String alias) {
        this(DSL.name(alias), PUPIL);
    }

    /**
     * Create an aliased <code>public.pupil</code> table reference
     */
    public Pupil(Name alias) {
        this(alias, PUPIL);
    }

    private Pupil(Name alias, Table<PupilRecord> aliased) {
        this(alias, aliased, null);
    }

    private Pupil(Name alias, Table<PupilRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "Contains a single pupil");
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
    public Identity<PupilRecord, Long> getIdentity() {
        return Keys.IDENTITY_PUPIL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PupilRecord> getPrimaryKey() {
        return Keys.PUPIL_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PupilRecord>> getKeys() {
        return Arrays.<UniqueKey<PupilRecord>>asList(Keys.PUPIL_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PupilRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PupilRecord, ?>>asList(Keys.PUPIL__PUPIL_PUPIL_GROUP_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pupil as(String alias) {
        return new Pupil(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pupil as(Name alias) {
        return new Pupil(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Pupil rename(String name) {
        return new Pupil(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Pupil rename(Name name) {
        return new Pupil(name, null);
    }
}
