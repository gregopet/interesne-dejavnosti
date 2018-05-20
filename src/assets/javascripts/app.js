//= require time

Vue.component('paragraphs', {
	render: function(createElement) {
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
                    pupilGroups: []
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
                            return timeSlotsOverlap(selectedGroup.times, activity.times)
                        })
                        return conflicting ? conflicting.name : null
                    }
                }
            });
        })
    }
)
.catch(function(err) {
    alert("Prišlo je do napake: " + err)
})
