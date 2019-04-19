package si.francebevk.dto

import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Descriptor of an activity
 * @property id The canonical ID of this activity
 * @property name A short name for this activity
 * @property description A textual description of this activity
 * @property times The times at which this activity takes place
 * @property leader Name of the person leading this activity.
 * @property chosen Did a student have this ability selected?
 * @property cost The price for this activity
 */
class Activity(
    var id: Long,
    var name: String,
    var description: String,
    var leader: String,
    var times: List<TimeSlot>,
    var chosen: Boolean,
    var cost: String?
)

/** Describes the list of activities in a human-readable way, adding to a [description] */
fun List<Activity>.appendDescriptionTo(description: StringBuilder) {
    if (this.isNotEmpty()) {
        description.append("dejavnosti: ")
        this.forEachIndexed { actIdx, act ->
            if (actIdx > 0) {
                description.append(", ")
            }
            description.append(act.name)
            if (act.times.isNotEmpty()) {
                description.append(" ( ")
                act.times.forEachIndexed { timeIdx, time ->
                    if (timeIdx != 0) {
                        description.append(", ")
                    }
                    description.append(time.daySlovenianShort).append(" ").append(time.from.minuteTimeFormat)
                }
                description.append(" )")
            }
        }
    } else {
        description.append("nima dejavnosti")
    }
}