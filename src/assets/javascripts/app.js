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

Vue.filter('minuteTime', function(value) {
    if (!value) return '';
    var hours = Math.floor(value / 60);
    var minutes = value % 60;
    var padding = (minutes < 10) ? "0" : ""
    return "" + hours + ":" + padding + minutes
})

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
                    }
                }
            });
        })
    }
)
.catch(function(err) {
    alert("Prišlo je do napake: " + err)
})
