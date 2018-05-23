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
            var app = new Vue({
                el: '#app',
                data: {
                    leaveTimeRange: [840, 900, 960, 1020],
                    groups: state.activities,
                    currentGroup: state.activities[0],
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
        })
    }
)
.catch(function(err) {
    alert("Prišlo je do napake: " + err)
})
