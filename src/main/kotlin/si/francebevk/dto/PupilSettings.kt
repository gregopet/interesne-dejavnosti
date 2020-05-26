package si.francebevk.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property monday The pupil leave time on monday or null if pupil will not be in the school's jurisdiction at all
 * @property tuesday The pupil leave time on tuesday or null if pupil will not be in the school's jurisdiction at all
 * @property wednesday The pupil leave time on wednesday or null if pupil will not be in the school's jurisdiction at all
 * @property thursday The pupil leave time on thursday or null if pupil will not be in the school's jurisdiction at all
 * @property friday The pupil leave time on friday or null if pupil will not be in the school's jurisdiction at all
 * @property notifyViaEmail When admins edit the pupil profiles they can specify the notification emails not to be sent out (for parents they are always sent out)
 * @property canLeaveAlone Is this pupil allowed to leave without an acompanying person?
 * @property morningWatchArrival When will this kid arrive into morning watch? (null means no morning watch)
 */
class PupilSettings(
    var extendedStay: Boolean,
    var selectedActivities: List<Long>,
    var monday: Short?,
    var tuesday: Short?,
    var wednesday: Short?,
    var thursday: Short?,
    var friday: Short?,
    var notifyViaEmail: Boolean,
    var authorizedPersons: List<AuthorizedPerson>?,
    var canLeaveAlone: Boolean,
    var morningWatchArrival: Short?
) {
    @get:JsonIgnore
    val monNice get() = format(monday)

    @get:JsonIgnore
    val tueNice get() = format(tuesday)

    @get:JsonIgnore
    val wedNice get() = format(wednesday)

    @get:JsonIgnore
    val thuNice get() = format(thursday)

    @get:JsonIgnore
    val friNice get() = format(friday)


    private fun format(time: Short?): String =
        if (time == null) "odide takoj po pouku"
        else time.minuteTimeFormat

    fun appendDescriptionTo(description: StringBuilder) {
        if (!extendedStay) {
            description.append("NI v podaljšanem bivanju")
        } else {
            description.append("JE v podaljšanem bivanju")
            description.append(", pon ").append(monNice)
            description.append(", tor ").append(tueNice)
            description.append(", sre ").append(wedNice)
            description.append(", čet ").append(thuNice)
            description.append(", pet ").append(friNice)
        }
        description.append("; ")
        if (canLeaveAlone) {
            description.append("SME sam odditi domov; ")
        }
    }
}