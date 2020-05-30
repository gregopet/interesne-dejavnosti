package si.francebevk.interesnedejavnosti

import ratpack.handling.Context
import si.francebevk.db.enums.AuthorizedPersonType
import si.francebevk.db.enums.DayOfWeek
import si.francebevk.db.tables.records.ActivityRecord
import si.francebevk.db.tables.records.AuthorizedCompanionRecord

/** Formats a minutes spec into a human readable time */
val Short.minuteTimeFormat get() = "${Math.floor(this / 60.0).toInt()}:${(this % 60).toString().padStart(2, '0')}"

/** Parses a human-readable time string into number of minutes since midnight */
val String.toMinutes: Int get() {
    val (hours, minutes) = split(":", limit = 2).map(String::toInt)
    return hours * 60 + minutes
}

/** Extracts the pupil's name from the context */
val Context.pupilName get() = user.getAttribute(DbAuthenticator.PUPIL_NAME) as String

/** Extracts the pupil's class from the context */
val Context.pupilClass get() = user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String

val DayOfWeek.shortSlo get() = when(this) {
    DayOfWeek.monday -> "pon"
    DayOfWeek.tuesday -> "tor"
    DayOfWeek.wednesday -> "sre"
    DayOfWeek.thursday -> "čet"
    DayOfWeek.friday -> "pet"
    DayOfWeek.saturday -> "sob"
    DayOfWeek.sunday -> "ned"
}

val DayOfWeek.fullSlo get() = when(this) {
    DayOfWeek.monday -> "Ponedeljek"
    DayOfWeek.tuesday -> "Torek"
    DayOfWeek.wednesday -> "Sreda"
    DayOfWeek.thursday -> "Četrtek"
    DayOfWeek.friday -> "Petek"
    DayOfWeek.saturday -> "Sobota"
    DayOfWeek.sunday -> "Nedelja"
}

fun shortActivityName(rec: ActivityRecord) =
    "${rec.name} (${rec.slots.map { "${it.day.shortSlo} ${it.startMinutes.minuteTimeFormat}" }.joinToString()})"

fun longActivityPeriods(rec: ActivityRecord) =
    "${rec.slots.map { "${it.day.fullSlo} ${it.startMinutes.minuteTimeFormat}" }.joinToString()}"

val AuthorizedCompanionRecord.sloType get() = when(this.type) {
    AuthorizedPersonType.parent -> "oče/mati"
    AuthorizedPersonType.aunt_uncle -> "teta/stric"
    AuthorizedPersonType.grandparent -> "babica/dedek"
    AuthorizedPersonType.sibling -> "sestra/brat"
    AuthorizedPersonType.other -> ""
}