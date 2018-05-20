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

fetch("/activities", { credentials: 'include' } )
.then(
    function(response) {
        response.json().then(function(groups) {
        console.log(groups)
            var app = new Vue({
                el: '#app',
                data: {
                    groups: groups,
                    currentGroup: groups[0],
                    pupilGroups: _.filter(groups, function(group) { return group.chosen })
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
                        var payload = { selectedActivities: selectedActivityIds }

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
