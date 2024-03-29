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

function formatDay(day) {
    if (day == 'monday') return 'ponedeljek'
    if (day == 'tuesday') return 'torek'
    if (day == 'wednesday') return 'sreda'
    if (day == 'thursday') return 'četrtek'
    if (day == 'friday') return 'petek'
}
//= require time

Vue.component('paragraphs', {
	render: function(createElement) {
	console.log(this.text.split('\\n'))
		return createElement('div', _.map(this.text.split('\n'), function(txt) {
			return createElement('p', txt);
		}));
	},
	props: {
		text: {
			type: String,
			required: true
		}
	}
});

Vue.filter('minuteTime', formatMinutes)
Vue.filter('day', formatDay)

fetch("state?rnd=" + Math.floor(Math.random() * Math.floor(1000000)), { credentials: 'include', cache: 'no-cache' } )
.then(
    function(response) {
        response.json().then(function(state) {
            var sortedActivities = _.sortBy(state.activities, function(a) { return a.name.toLowerCase() })
            _.each(sortedActivities, function(act) { 
                act.freePlaces = 3
                act.currentlyMine = null
            })
            var leaveTimeRange = [775, 825, 875, 930, 980, 1020]

            var app = new Vue({
                el: '#app',
                data: {

                    // Times students can pick as leave times
                    leaveTimeRange: leaveTimeRange,

                    // Total range of available activities
                    groups: sortedActivities,

                    // What activity is currently displayed in the UI?
                    currentGroup: sortedActivities[0],

                    // What activities did the pupil already select?
                    pupilGroups: _.filter(state.activities, function(group) { return group.chosen }),

                    // Is pupil staying in extended stay (explicit consent..)
                    extendedStay: state.extendedStay,

                    // Is pupil still young enough to be able to be in extended stay?
                    extendedStayPossible: !!document.getElementById('extended-stay-card'),

                    // What leave times did the student have pre-selected?
                    leaveTimes: {
                        monday: state.monday || leaveTimeRange[0],
                        tuesday: state.tuesday || leaveTimeRange[0],
                        wednesday: state.wednesday || leaveTimeRange[0],
                        thursday: state.thursday || leaveTimeRange[0],
                        friday: state.friday || leaveTimeRange[0]
                    },

                    // Are we currently displaying a conflict?
                    // Contains an array of time coflicts with following fields: activity, day, timeHome, leaveTime,
                    // timeActivity, fixedTimeHome (see confirmNoLeaveTimesActivityConflicts)
                    conflicts: null,

                    // Should the buttons be blocked because the form is still processing?
                    formIsSending: false
                },
                watch: {
                    // Check for conflicts when users change leave times
                    'leaveTimes.monday': function(x, y) { this.confirmNoLeaveTimesActivityConflicts() },
                    'leaveTimes.tuesday': function(x, y) { this.confirmNoLeaveTimesActivityConflicts() },
                    'leaveTimes.wednesday': function(x, y) { this.confirmNoLeaveTimesActivityConflicts() },
                    'leaveTimes.thursday': function(x, y) { this.confirmNoLeaveTimesActivityConflicts() },
                    'leaveTimes.friday': function(x, y) { this.confirmNoLeaveTimesActivityConflicts() },
                },
                methods: {
                    logout: function() {
                        if (confirm("Ste prepričani da želite zapreti stran, ne da bi shranili spremembe?")) {
                            window.location.href = "login.html"
                        }
                    },
                    isSelected: function(group) {
                        return _.find(this.pupilGroups, group);
                    },
                    select: function(group) {
                        this.pupilGroups.push(group);
                        this.confirmNoLeaveTimesActivityConflicts()
                    },
                    deselect: function(group) {
                        this.pupilGroups = _.without(this.pupilGroups, group);
                    },
                    hasConflictWithActivity: function(activity) {
                        var conflicting = _.find(this.pupilGroups, function(selectedGroup) {
                            return timeSlotGroupsOverlap(selectedGroup.times, activity.times)
                        })
                        return conflicting ? conflicting.name : null
                    },
                    // returns true if there were no conflicts
                    confirmNoLeaveTimesActivityConflicts: function() {
                        if (!this.extendedStayPossible) return true;

                         // go through all selected activities
                         for (var a = 0; a < this.pupilGroups.length; a++) {
                            var activity = this.pupilGroups[a]

                            // go through leave times
                            if (activity.times) {
                                for (var s = 0; s < activity.times.length; s++) {
                                    var slot = activity.times[s];
                                    var leaveTime = this.extendedStay ? this.leaveTimes[slot.day] : null;

                                    // fix discrepancy - last leave time is at 17:00 but activities last until 17:05! So we subtract 5 minutes - shouldn't harm with non-edge cases!
                                    if (leaveTime == null || leaveTime < slot.to - 5) {
                                        var fixedTimeHome = _.find(this.leaveTimeRange, function(t) { return t >= slot.to })

                                        // other fix required for discrepancy (selector for leaving times doesn't know about 17:05!)
                                        if (fixedTimeHome == null && slot.to == 1025) fixedTimeHome = 1020

                                        this.conflicts = { activity: activity, day: slot.day, timeHome: leaveTime, timeActivity: slot.to, fixedTimeHome: fixedTimeHome }
                                        $('#conflictModal').modal({keyboard: false, backdrop: 'static'})
                                        return // process conflicts one by one!
                                    }
                                }
                            }
                         }
                         $('#conflictModal').modal('hide') // no return? hide dialog, no conflicts
                    },

                    resolveConflictRemoveActivity: function(activity) {
                        this.deselect(activity)

                        // check for further conflicts

                        this.confirmNoLeaveTimesActivityConflicts()
                    },
                    resolveConflictUpdateLeaveTime: function(day, leaveTime) {
                        this.leaveTimes[day] = leaveTime
                        if (this.extendedStayPossible) {
                            this.extendedStay = true
                        }

                        // check for further conflicts
                        this.confirmNoLeaveTimesActivityConflicts()
                    },

                    save: function() {
                        window.location.href = "finish.html"
                    }
                }
            });

            // Vacancy checks
            function checkVacancy() {
                fetch("vacancy", {
                    credentials: 'include',
                    cache: 'no-cache',
                    headers: new Headers({
                        "X-Requested-With": "XMLHttpRequest"
                    })
                }).then(function(response) {
                    if (response.status == 401) {
                        alert("Žal je prišlo do neznane napake, vaša seja je bila prekinjena! Prosimo vas, da se ponovno prijavite!")
                        window.location.href = "login-form"
                    }
                    else if (response.status == 200) {
                        response.json().then(function(payload) {
                            _.each(app.groups, function(act) {
                                var status = _.find(payload, function(p) { return p.id == act.id })
                                if (status) {
                                    act.freePlaces = status.free
                                    act.currentlyMine = status.currentlyMine
                                }
                            });
                        })
                    }
                })
            }
            checkVacancy()
            setInterval(checkVacancy, 5000)

        })
    }
)
.catch(function(err) {
    alert("Prišlo je do napake: " + err)
})

