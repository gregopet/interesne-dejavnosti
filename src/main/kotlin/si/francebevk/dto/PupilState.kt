package si.francebevk.dto

/**
 * Contains the initial pupil state for the Javascript editor.
 * @property activities A list of all activities available to the student
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property mon The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tue The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wed The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thu The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property fri The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 */
class PupilState(
    var activities: List<Activity>,
    var extendedStay: Boolean,
    var mon: Short?,
    var tue: Short?,
    var wed: Short?,
    var thu: Short?,
    var fri: Short?
)