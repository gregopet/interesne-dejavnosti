function timeSlotGroupsOverlap(group1, group2) {
    return _.find(group1, function(takenTime) {
        return _.find(group2, function(wantedTime) {
            return timeSlotsOverlap(takenTime, wantedTime);
        })
    })
}

function timeSlotsOverlap(slot1, slot2) {
    if (slot1.day != slot2.day) return false;
    return slot1.from < slot2.to && slot1.to > slot2.from
}

function formatMinutes(value) {
    if (!value) return '';
    var hours = Math.floor(value / 60);
    var minutes = value % 60;
    var padding = (minutes < 10) ? "0" : ""
    return "" + hours + ":" + padding + minutes	
}
