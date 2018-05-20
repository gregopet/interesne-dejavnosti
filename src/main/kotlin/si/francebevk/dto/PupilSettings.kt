package si.francebevk.dto

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property mon The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tue The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wed The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thu The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property fri The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 */
class PupilSettings(
    var selectedActivities: List<Long>,
    var mon: Int?,
    var tue: Int?,
    var wed: Int?,
    var thu: Int?,
    var fri: Int?
)