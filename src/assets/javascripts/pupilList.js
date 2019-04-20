var isKlass = /[1-9][A-Za-z]/
var isYear = /[1-9]/
var isWhitespace = /^\W*$/


new Vue({
    el: '#app',
    data: {
        search: ''
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
        }
    }
});