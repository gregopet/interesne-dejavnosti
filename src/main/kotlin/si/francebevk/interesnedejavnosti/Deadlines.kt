package si.francebevk.interesnedejavnosti

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/** Deadlines configured in the settings */
class Deadlines(startAt: String, endAt: String, timezone: String) {

    /** The time on which the sumitions open to the public */
    val start = LocalDateTime.parse(startAt).atZone(ZoneId.of(timezone)).toInstant()

    /** A formatted description of the date when the application opens */
    val startDateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(startAt))

    /** A formatted description of the time when the application opens */
    val startTimeString = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(startAt))


    /** The time on which the sumitions close to the public */
    val end = LocalDateTime.parse(endAt).atZone(ZoneId.of(timezone)).toInstant()

    /** A formatted description of the date when the application opens */
    val endDateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(endAt))

    /** A formatted description of the time when the application opens */
    val endTimeString = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(endAt))


    /** Are we still before the start date? */
    val isBeforeStart get() = start.isAfter(Instant.now())

    /** Are we already after the end date? */
    val isAfterEnd get() = end.isBefore(Instant.now())

}