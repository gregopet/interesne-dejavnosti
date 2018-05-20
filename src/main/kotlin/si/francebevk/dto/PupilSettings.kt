package si.francebevk.dto

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property selectedActivities The IDs of activities this pupil had selected
 */
class PupilSettings(
    var selectedActivities: List<Long>
)