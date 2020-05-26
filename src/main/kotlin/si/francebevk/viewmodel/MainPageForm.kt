package si.francebevk.viewmodel

/**
 * All information needed to render the main page form.
 * @property pupilName Full name of the pupil for whom we are rendering the form
 * @property pupilClass The full class name of the pupil
 * @property pupilHasExtendedStay Are leave times still relevant for this child or are they too old?
 * @property closeDate The date (already formatted) when applications close
 * @property closeHour The time (already formatted) when applications close on [closeDate]
 * @property morningWatchTimes The possible times at which parents can drop off their children in the morning
 * @property isInFirstPhase Are we in the first part of the process where pupils are limited to X ordinary activities?
 * @property firstPhaseLimit X, the number of ordinary activities a pupil may choose during the first phase
 * @property firstPhaseEndDate The date (already formatted) when first phase will end
 * @property firstPhaseEndTime The time (already formatted) when first phase will end on [firstPhaseEndDate]
 * @property isAdminRequest Is this form actually being rendered for an admin who will apply the pupil?
 * @property askForMorningWatch Should we ask if this pupil needs morning care or are they already too old?
 * @property askForSelfLeave Should we ask if this pupil is allowed to leave the school on their own?
 */
class MainPageForm(
    val pupilName: String,
    val pupilClass: String,
    val pupilHasExtendedStay: Boolean,
    val closeDate: String,
    val closeHour: String,
    val morningWatchTimes: List<Int>,
    val isInFirstPhase: Boolean,
    val firstPhaseLimit: Int?,
    val firstPhaseEndDate: String,
    val firstPhaseEndTime: String,
    val isAdminRequest: Boolean,
    val askForSelfLeave: Boolean,
    val askForMorningWatch: Boolean
)