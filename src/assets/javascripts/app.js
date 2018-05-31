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

fetch("/state", { credentials: 'include' } )
.then(
    function(response) {
        response.json().then(function(state) {
            var sortedActivities = _.sortBy(state.activities, function(a) { return a.name.toLowerCase() })
            _.each(sortedActivities, function(act) { 
                act.freePlaces = 3
                act.currentlyMine = null
            })


            var app = new Vue({
                el: '#app',
                data: {
                    leaveTimeRange: [775, 825, 875, 930, 980, 1020],
                    groups: sortedActivities,
                    currentGroup: sortedActivities[0],
                    pupilGroups: _.filter(state.activities, function(group) { return group.chosen }),
                    extendedStay: state.extendedStay,
                    leaveTimes: {
                        monday: state.monday,
                        tuesday: state.tuesday,
                        wednesday: state.wednesday,
                        thursday: state.thursday,
                        friday: state.friday
                    },
                    conflicts: null, //an array of time coflicts with following fields: activity, day, timeHome, leaveTime, timeActivity, fixedTimeHome (see confirmNoLeaveTimesActivityConflicts)
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
                            window.location.href = "/logout"
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
                        if (!this.extendedStay) return true;

                         // go through all selected activities
                         for (var a = 0; a < this.pupilGroups.length; a++) {
                            var activity = this.pupilGroups[a]

                            // go through leave times
                            if (activity.times) {
                                for (var s = 0; s < activity.times.length; s++) {
                                    var slot = activity.times[s];
                                    var leaveTime = this.leaveTimes[slot.day]
                                    if (leaveTime == null || leaveTime < slot.to) {
                                        var fixedTimeHome = _.find(this.leaveTimeRange, function(t) { return t >= slot.to })
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

                        // check for further conflicts
                        this.confirmNoLeaveTimesActivityConflicts()
                    },

                    save: function() {
                        // selected activity IDs
                        var selectedActivityIds = _.map(this.pupilGroups, function(group) { return group.id })
                        var payload = {
                            extendedStay: this.extendedStay,
                            selectedActivities: selectedActivityIds,
                            monday: this.leaveTimes.monday,
                            tuesday: this.leaveTimes.tuesday,
                            wednesday: this.leaveTimes.wednesday,
                            thursday: this.leaveTimes.thursday,
                            friday: this.leaveTimes.friday
                         }
                        this.formIsSending = true

                        fetch("/store", {
                            body: JSON.stringify(payload),
                            cache: 'no-cache',
                            credentials: 'include',
                            method: 'POST',
                            headers: {
                                'content-type': 'application/json'
                            }
                        }).then(function(response) {
                            this.formIsSending = false
                            if (response.status == 200) {
                                window.location.href = "/finish"
                            }
                            else if (response.status = 409) {
                                response.json().then(function(offending) {
                                    var taken = offending.join(", ")
                                    window.alert(
                                        "Žal so naslednje aktivnosti že polno zasedene: " + taken + "\n\n" +
                                        "Prosimo vas, da jih odstranite iz spiska izbranih dejavnosti in ponovno poizkusite potrditi vnos!"
                                    )

                                })
                            }
                        })
                    }
                }
            });

            // Vacancy checks
            function checkVacancy() {
                fetch("/vacancy", {
                    credentials: 'include',
                    cache: 'no-cache'
                }).then(function(response) {
                    if (response.status == 200) {
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
