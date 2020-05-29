package si.francebevk.dto

/**
 * Pupil settings that differ per day
 * @property leaveTime The pupil leave time for that day or null if pupil will not be in the school's jurisdiction at all
 * @property morningSnack Did the pupil apply for the morning snack?
 * @property lunch Did the pupil apply for school lunch?
 * @property afternoonSnack Did the pupil apply for the afternoon snack?
 */
class PupilDaySettings(
    var leaveTime: Short?,
    var morningSnack: Boolean = false,
    var lunch: Boolean = false,
    var afternoonSnack: Boolean = false
)