package si.francebevk.interesnedejavnosti

import ratpack.handling.Context
import si.francebevk.db.enums.DayOfWeek
import si.francebevk.db.tables.records.ActivityRecord

/** Formats a minutes spec into a human readable time */
val Short.minuteTimeFormat get() = "${Math.floor(this / 60.0).toInt()}:${(this % 60).toString().padStart(2, '0')}"

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

fun shortActivityName(rec: ActivityRecord) =
    "${rec.name} (${rec.slots.map { "${it.day.shortSlo} ${it.startMinutes.minuteTimeFormat}" }.joinToString()})"