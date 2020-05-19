import 'bootstrap/dist/css/bootstrap.min.css';
import Vue from 'vue';
import { formatMinutes, formatDay, timeSlotGroupsOverlap, timeSlotsOverlap } from './time';
import * as _ from 'lodash';
import * as $ from 'jquery';
import 'bootstrap/js/dist/modal.js';

type EventType = 'login' |'abort' | 'submit' | 'failed_submit';

interface EditablePupil {
    id: number;
    access_code?: string;
    first_name: string;
    last_name: string;
    pupil_group: string;
    emails: string[];
}

interface PupilInterfaceActivity {
    id: number;
    pupil_id: number;
    time: string;
    admin_user: boolean;
    type: EventType;
    details: string | null;
}

var isKlass = /[1-9][A-Za-z]/
var isYear = /[1-9]/
var isWhitespace = /^\W*$/

function zPad(length: number, value: number): string {
    var strValue = '' + value;
    while (strValue.length < length) {
        strValue = "0" + strValue
    }
    return strValue;
}

Vue.filter('date', function(dateStr: string): string {
    var date = new Date(dateStr);
    return '' +
        zPad(2, date.getDate()) + "." + zPad(2, (date.getMonth() + 1)) + "." + zPad(4, date.getFullYear())
        + " " +
        zPad(2, date.getHours()) + ":" + zPad(2, date.getMinutes())
        ;
});

Vue.filter('event_type', function(type: EventType): string {
    switch(type) {
        case 'login': return 'Odprl'
        case 'abort': return 'Odnehal'
        case 'submit': return 'Oddal'
        case 'failed_submit': return 'Ni uspel oddati'
    }
});

new Vue({
    el: '#pupilList',
    data: {
        search: '',
        pupilEvents: [],
        pupilEventsForPupil: "",
        editedPupil: null,
        errorLoadingPupil: false,
        savingPupil: true,
        extendedView: false
    },
    methods: {
        matchesFilter: function(klass: string, firstName: string, lastName: string): boolean {
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
                        if (firstName.toLowerCase().indexOf(term) >= 0) {
                            return true;
                        }
                        if (lastName.toLowerCase().indexOf(term) >= 0) {
                            return true;
                        }
                    }
                }
                return false;
            }
        },

        showEvents: function(pupilId: number, name: string) {
            var vue = this;
            fetch('/admin/activity/' + pupilId, { cache: 'no-cache', credentials: 'include' }).then(function(response) {
                if (response.ok) return response.json();
            }).then(function(json) {
                vue.pupilEvents = json;
                vue.pupilEventsForPupil = name;
                $(vue.$refs.activityDialog).modal()
            });
        },

        eventClass: function(event: PupilInterfaceActivity) {
            var adminType = event.admin_user ? "text-danger " : "";
            switch(event.type) {
                case 'login': return adminType;
                case 'abort': return adminType + 'table-active';
                case 'submit': return adminType + 'table-success';
                case 'failed_submit': return adminType + 'table-danger';
            }
        },

        /** Closes the pupil editing dialog */
        closeDialog: function() {
            // TODO
        },

        /** Opens the pupil editing dialog */
        editPupil: function(pupilId: number) {
            var vue = this;
            vue.editedPupil = null;
            vue.loadingPupil = true;
            vue.savingPupil = false;
            fetch('/admin/pupil-editor/' + pupilId, { cache: 'no-cache', credentials: 'include' }).then(function(response) {
                if (response.ok) {
                    response.json().then( function(pupil: EditablePupil) {
                        while(pupil.emails.length < 2) {
                            pupil.emails.push("");
                        }
                        vue.editedPupil = pupil;
                    });
                }
            });
            $(vue.$refs.pupilEditDialog).modal()
        },
        savePupil: function(editedPupil: EditablePupil) {
            var vue = this;
            vue.savingPupil = true;
            fetch('/admin/pupil-editor/' + editedPupil.id, {
                cache: 'no-cache',
                credentials: 'include',
                method: 'POST',
                headers: new Headers({'content-type': 'application/json'}),
                body: JSON.stringify(editedPupil),
            }).then(function(response) {
                if (response.ok) {
                    $(vue.$refs.pupilEditDialog).modal('hide');
                    window.location.reload(false);
                } else {
                    vue.errorLoadingPupil = true;
                }
            })
        }
    }
});