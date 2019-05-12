package si.francebevk.interesnedejavnosti

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Only allow a limited number of activities to be chosen before a certain date.
 *
 * @property limit Number of activities to limit pupils to in the initial phase
 */
class TwoPhaseProcess(val limit: Int, endAt: String, timezone: String) {

    /** The moment when the two-phase process ends and there are no more limits */
    val end = LocalDateTime.parse(endAt).atZone(ZoneId.of(timezone)).toInstant()

    /** A formatted description of the date when the two-phase process ends  */
    val endDateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(endAt))

    /** A formatted description of the time when the two-phase process ends */
    val endTimeString = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("sl")).format(LocalDateTime.parse(endAt))

    /** Is the two phase process currently still in effect? */
    val isInEffect get() = end.isAfter(Instant.now())
}