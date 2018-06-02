package si.francebevk.dto

import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Represents an activity time slot.
 * @property day Name of the day of this time slot
 * @property from Starting time of the activity, in minutes from midnight
 * @property to Ending time of the activity, in minutes from midnight
 */
class TimeSlot (
    var day: String,
    var from: Short,
    var to: Short
) {
    override fun toString(): String =
        "$daySlovenian: ${from.minuteTimeFormat} - ${to.minuteTimeFormat}"

    val daySlovenian get() = when (day) {
        "monday"    -> "ponedeljek"
        "tuesday"   -> "torek"
        "wednesday" -> "sreda"
        "thursday"  -> "četrtek"
        "friday"    -> "petek"
        else        -> day
    }
}