package si.francebevk.dto

/**
 * A class for transferring activity occupancy.
 * @property id Id of the activity
 * @property free Number of free slots
 * @property currentlyMine Is the current pupil signed up for this class?
 */
class ActivityVacancy(var id: Long, var free: Int, var currentlyMine: Boolean)