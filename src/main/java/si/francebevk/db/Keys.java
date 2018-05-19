/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;

import si.francebevk.db.tables.Pupil;
import si.francebevk.db.tables.PupilGroup;
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

    public static final Identity<PupilRecord, Long> IDENTITY_PUPIL = Identities0.IDENTITY_PUPIL;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PupilRecord> PUPIL_PKEY = UniqueKeys0.PUPIL_PKEY;
    public static final UniqueKey<PupilGroupRecord> PUPIL_GROUP_PKEY = UniqueKeys0.PUPIL_GROUP_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<PupilRecord, PupilGroupRecord> PUPIL__PUPIL_PUPIL_GROUP_FKEY = ForeignKeys0.PUPIL__PUPIL_PUPIL_GROUP_FKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<PupilRecord, Long> IDENTITY_PUPIL = createIdentity(Pupil.PUPIL, Pupil.PUPIL.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<PupilRecord> PUPIL_PKEY = createUniqueKey(Pupil.PUPIL, "pupil_pkey", Pupil.PUPIL.ID);
        public static final UniqueKey<PupilGroupRecord> PUPIL_GROUP_PKEY = createUniqueKey(PupilGroup.PUPIL_GROUP, "pupil_group_pkey", PupilGroup.PUPIL_GROUP.NAME);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<PupilRecord, PupilGroupRecord> PUPIL__PUPIL_PUPIL_GROUP_FKEY = createForeignKey(si.francebevk.db.Keys.PUPIL_GROUP_PKEY, Pupil.PUPIL, "pupil__pupil_pupil_group_fkey", Pupil.PUPIL.PUPIL_GROUP);
    }
}
