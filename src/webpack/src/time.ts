/** Find if any of the time slots from group1 and group2 overlap */
export function timeSlotGroupsOverlap(group1: Rest.TimeSlot[], group2: Rest.TimeSlot[]): boolean {
    const overlapping = group1.find( (takenTime) => 
        group2.find( (wantedTime) => 
            timeSlotsOverlap(takenTime, wantedTime)
        )
    )
    return !!overlapping;
}

/** Do the two time slots overlap? */
export function timeSlotsOverlap(slot1: Rest.TimeSlot, slot2: Rest.TimeSlot): boolean {
    if (slot1.day != slot2.day) return false;
    return slot1.from < slot2.to && slot1.to > slot2.from
}

export function formatMinutes(value: number): string {
    if (!value) return '';
    var hours = Math.floor(value / 60);
    var minutes = value % 60;
    var padding = (minutes < 10) ? "0" : ""
    return "" + hours + ":" + padding + minutes	
}

export function formatDay(day: string): string {
    if (day == 'monday') return 'ponedeljek';
    if (day == 'tuesday') return 'torek';
    if (day == 'wednesday') return 'sreda';
    if (day == 'thursday') return 'četrtek';
    if (day == 'friday') return 'petek';
    throw new Error(`Unknown day '${day}'`);
}