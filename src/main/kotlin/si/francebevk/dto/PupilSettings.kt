package si.francebevk.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import si.francebevk.interesnedejavnosti.minuteTimeFormat

/**
 * Contains the pupil settings sent by the client that need to be saved into the database.
 * @property extended_stay Indicates choice for pupil''s inclusion in extended stay
 * @property selectedActivities The IDs of activities this pupil had selected
 * @property notifyViaEmail When admins edit the pupil profiles they can specify the notification emails not to be sent out (for parents they are always sent out)
 * @property canLeaveAlone Is this pupil allowed to leave without an acompanying person?
 * @property morningWatchArrival When will this kid arrive into morning watch? (null means no morning watch)
 */
class PupilSettings(
    var extendedStay: Boolean,
    var selectedActivities: List<Long>,
    var monday: PupilDaySettings,
    var tuesday: PupilDaySettings,
    var wednesday: PupilDaySettings,
    var thursday: PupilDaySettings,
    var friday: PupilDaySettings,
    var notifyViaEmail: Boolean,
    var authorizedPersons: List<AuthorizedPerson>?,
    var canLeaveAlone: Boolean,
    var morningWatchArrival: Short?,
    var orderTextbooks: Boolean
) {
    @get:JsonIgnore
    val monNice get() = format(monday.leaveTime)

    @get:JsonIgnore
    val tueNice get() = format(tuesday.leaveTime)

    @get:JsonIgnore
    val wedNice get() = format(wednesday.leaveTime)

    @get:JsonIgnore
    val thuNice get() = format(thursday.leaveTime)

    @get:JsonIgnore
    val friNice get() = format(friday.leaveTime)

    fun disableAfternoonSnacks() {
        monday.afternoonSnack = false
        tuesday.afternoonSnack = false
        wednesday.afternoonSnack = false
        thursday.afternoonSnack = false
        friday.afternoonSnack = false
    }

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