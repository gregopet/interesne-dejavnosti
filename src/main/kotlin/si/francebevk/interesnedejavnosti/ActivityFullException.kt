package si.francebevk.interesnedejavnosti

import si.francebevk.db.tables.records.ActivityRecord

class ActivityFullException(val activities: List<ActivityRecord>) : RuntimeException("Activities with too many pupils detected!") {

    fun appendDescriptionTo(description: StringBuilder) {
        description.append("PRIJAVA NI BILA SPREJETA, ker so bile polne naslednje dejavnosti:")
        activities.forEachIndexed { index, activityRecord ->
            if (index > 0) description.append(", ")
            description.append(activityRecord.name)
            if (activityRecord.slots != null && activityRecord.slots.isNotEmpty()) {
                description.append(" ( ")
                activityRecord.slots.forEachIndexed { slotIdx, slot ->
                    if (slotIdx > 0) description.append(", ")
                    description.append(slot.day.shortSlo).append(" ")
                    description.append(slot.startMinutes.minuteTimeFormat)
                }
                description.append(" )")
            }
        }
    }
}