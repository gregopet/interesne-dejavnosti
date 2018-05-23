package si.francebevk.interesnedejavnosti

import ratpack.handling.Context

/** Formats a minutes spec into a human readable time */
val Int.minuteTimeFormat get() = "${Math.floor(this / 60.0).toInt()}:${(this % 60).toString().padStart(2, '0')}"
/** Extracts the pupil's name from the context */
val Context.pupilName get() = user.getAttribute(DbAuthenticator.PUPIL_NAME) as String

/** Extracts the pupil's class from the context */
val Context.pupilClass get() = user.getAttribute(DbAuthenticator.PUPIL_CLASS) as String