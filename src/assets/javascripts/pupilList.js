var isKlass = /[1-9][A-Za-z]/
var isYear = /[1-9]/
var isWhitespace = /^\W*$/

function zPad(length, value) {
    var strValue = '' + value;
    while (strValue.length < length) {
        strValue = "0" + strValue
    }
    return strValue;
}

Vue.filter('date', function(dateStr) {
    var date = new Date(dateStr);
    return '' +
        zPad(2, date.getDate()) + "." + zPad(2, (date.getMonth() + 1)) + "." + zPad(4, date.getYear() + 1900)
        + " " +
        zPad(2, date.getHours()) + ":" + zPad(2, date.getMinutes())
        ;
});

Vue.filter('event_type', function(type) {
    switch(type) {
        case 'login': return 'Odprl'
        case 'abort': return 'Odnehal'
        case 'submit': return 'Oddal'
        case 'failed_submit': return 'Ni uspel oddati'
    }
});

new Vue({
    el: '#app',
    data: {
        search: '',
        pupilEvents: [],
        pupilEventsForPupil: ""
    },
    methods: {
        matchesFilter: function(klass, name) {
            if (this.search == null || this.search.length == 0 || isWhitespace.test(this.search)) {
                // no filter
                return true;
            } else {
                var terms = this.search.split(" ");
                for (var i = 0; i < terms.length; i++) {
                    var term = terms[i];
                    if (isWhitespace.test(term)) {
                        continue;
                    } else if (isKlass.test(term)) {
                        if (klass.toLowerCase() === term.toLowerCase()) {
                            return true;
                        }
                    } else if (isYear.test(term)) {
                        if (klass[0] == term) {
                            return true;
                        }
                    } else {
                        if (name.toLowerCase().indexOf(term) >= 0) {
                            return true;
                        }
                    }
                }
                return false;
            }
        },

        showEvents: function(pupilId, name) {
            var vue = this;
            fetch('/admin/activity/' + pupilId, { cache: 'no-cache', credentials: 'include' }).then(function(response) {
                if (response.ok) return response.json();
            }).then(function(json) {
                vue.pupilEvents = json;
                vue.pupilEventsForPupil = name;
                $(vue.$refs.activityDialog).modal()
            });
        },

        eventClass: function(event) {
            var adminType = event.admin_user ? "text-danger " : "";
            switch(event.type) {
                case 'login': return adminType;
                case 'abort': return adminType + 'table-active';
                case 'submit': return adminType + 'table-success';
                case 'failed_submit': return adminType + 'table-danger';
            }
        }
    }
});