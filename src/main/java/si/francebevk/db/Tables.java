/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db;


import javax.annotation.Generated;

import si.francebevk.db.tables.Activity;
import si.francebevk.db.tables.ErrorLog;
import si.francebevk.db.tables.Pupil;
import si.francebevk.db.tables.PupilActivity;
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
     * Contains errors that may have happened during the process
     */
    public static final ErrorLog ERROR_LOG = si.francebevk.db.tables.ErrorLog.ERROR_LOG;

    /**
     * Contains a single pupil
     */
    public static final Pupil PUPIL = si.francebevk.db.tables.Pupil.PUPIL;

    /**
     * A mapping table between pupils and their chosen activities
     */
    public static final PupilActivity PUPIL_ACTIVITY = si.francebevk.db.tables.PupilActivity.PUPIL_ACTIVITY;

    /**
     * A class of pupils
     */
    public static final PupilGroup PUPIL_GROUP = si.francebevk.db.tables.PupilGroup.PUPIL_GROUP;
}
