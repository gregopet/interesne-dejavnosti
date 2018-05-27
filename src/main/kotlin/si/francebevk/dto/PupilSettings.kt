package si.francebevk.dto

import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property monday The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tuesday The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wednesday The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thursday The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property friday The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 */
class PupilSettings(
    var extendedStay: Boolean,
    var selectedActivities: List<Long>,
    var monday: Short?,
    var tuesday: Short?,
    var wednesday: Short?,
    var thursday: Short?,
    var friday: Short?
) {
    val monNice get() = format(monday)
    val tueNice get() = format(tuesday)
    val wedNice get() = format(wednesday)
    val thuNice get() = format(thursday)
    val friNice get() = format(friday)


    private fun format(time: Short?): String =
        if (time == null) "odide takoj po pouku"
        else time.minuteTimeFormat
}