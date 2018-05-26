//= require time

Vue.component('paragraphs', {
	render: function(createElement) {
		return createElement('div', _.map(this.text.split('\\n'), function(txt) {
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
                        mon: state.mon,
                        tue: state.tue,
                        wed: state.wed,
                        thu: state.thu,
                        fri: state.fri
                    }
                },
                methods: {
                    isSelected: function(group) {
                        return _.find(this.pupilGroups, group);
                    },
                    select: function(group) {
                        this.pupilGroups.push(group);
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
                    save: function() {
                        // selected activity IDs
                        var selectedActivityIds = _.map(this.pupilGroups, function(group) { return group.id })
                        var payload = {
                            extendedStay: this.extendedStay,
                            selectedActivities: selectedActivityIds,
                            mon: this.leaveTimes.mon,
                            tue: this.leaveTimes.tue,
                            wed: this.leaveTimes.wed,
                            thu: this.leaveTimes.thu,
                            fri: this.leaveTimes.fri
                         }

                        fetch("/store", {
                            body: JSON.stringify(payload),
                            cache: 'no-cache',
                            credentials: 'include',
                            method: 'POST',
                            headers: {
                                'content-type': 'application/json'
                            }
                        }).then(function(response) {
                            if (response.status == 200) {
                                window.location.href = "/finish"
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
