package si.francebevk.interesnedejavnosti

/** Formats a minutes spec into a human readable time */
val Int.minuteTimeFormat get() = "${Math.floor(this / 60.0).toInt()}:${(this % 60).toString().padStart(2, '0')}"