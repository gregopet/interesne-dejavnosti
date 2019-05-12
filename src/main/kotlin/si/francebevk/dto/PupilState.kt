package si.francebevk.dto

/**
 * Contains the initial pupil state for the Javascript editor.
 * @property activities A list of all activities available to the student
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property monday The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tuesday The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wednesday The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thursday The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property friday The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 * @property twoPhaseLimit The maximum amount of activities during the first phase of the process
 * @property twoPhaseEndMs The epoch milliseconds during which the two-phase process ends
 */
class PupilState(
    var activities: List<Activity>,
    var extendedStay: Boolean,
    var monday: Short?,
    var tuesday: Short?,
    var wednesday: Short?,
    var thursday: Short?,
    var friday: Short?,
    var twoPhaseLimit: Int,
    var twoPhaseEndMs: Long
)