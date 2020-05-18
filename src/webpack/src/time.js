export function timeSlotGroupsOverlap(group1, group2) {
    return _.find(group1, function(takenTime) {
        return _.find(group2, function(wantedTime) {
            return timeSlotsOverlap(takenTime, wantedTime);
        })
    })
}

export function timeSlotsOverlap(slot1, slot2) {
    if (slot1.day != slot2.day) return false;
    return slot1.from < slot2.to && slot1.to > slot2.from
}

export function formatMinutes(value) {
    if (!value) return '';
    var hours = Math.floor(value / 60);
    var minutes = value % 60;
    var padding = (minutes < 10) ? "0" : ""
    return "" + hours + ":" + padding + minutes	
}

export function formatDay(day) {
    if (day == 'monday') return 'ponedeljek'
    if (day == 'tuesday') return 'torek'
    if (day == 'wednesday') return 'sreda'
    if (day == 'thursday') return 'četrtek'
    if (day == 'friday') return 'petek'
}