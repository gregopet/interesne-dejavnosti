package si.francebevk.viewmodel

import si.francebevk.db.tables.records.PupilRecord

class PupilWithActivities(val pupil: PupilRecord, val activities: Array<Long>)