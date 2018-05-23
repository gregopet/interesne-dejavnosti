package si.francebevk.dto

import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property mon The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tue The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wed The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thu The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property fri The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 */
class PupilSettings(
    var extendedStay: Boolean,
    var selectedActivities: List<Long>,
    var mon: Short?,
    var tue: Short?,
    var wed: Short?,
    var thu: Short?,
    var fri: Short?
) {
    val monNice get() = format(mon)
    val tueNice get() = format(tue)
    val wedNice get() = format(wed)
    val thuNice get() = format(thu)
    val friNice get() = format(fri)


    private fun format(time: Short?): String =
        if (time == null) "odide takoj po pouku"
        else time.minuteTimeFormat
}