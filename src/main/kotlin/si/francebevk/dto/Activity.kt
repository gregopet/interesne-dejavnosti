package si.francebevk.dto

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