package si.francebevk.dto

class Activity(
    var id: Int,
    var name: String,
    var description: String,
    var leader: String,
    var times: List<TimeSlot>
)