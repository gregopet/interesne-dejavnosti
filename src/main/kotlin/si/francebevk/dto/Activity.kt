package si.francebevk.dto

class Activity(
    var id: Long,
    var name: String,
    var description: String,
    var leader: String,
    var times: List<TimeSlot>
)