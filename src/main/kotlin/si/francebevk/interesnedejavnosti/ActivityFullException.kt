package si.francebevk.interesnedejavnosti

import si.francebevk.db.tables.records.ActivityRecord

class ActivityFullException(val activities: List<ActivityRecord>) : RuntimeException("Activities with too many pupils detected!") {
}