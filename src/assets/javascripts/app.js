//= require time

Vue.filter('minuteTime', formatMinutes)
Vue.filter('day', formatDay)

fetch("state?rnd=" + Math.floor(Math.random() * Math.floor(1000000)), { credentials: 'include', cache: 'no-cache' } )
.then(
    function(response) {
        response.json().then(function(state) {
            var sortedActivities = _.sortBy(state.activities, function(a) { return a.name.toLowerCase() })
            _.each(sortedActivities, function(act) {
                // display something until the vacancy comes through
                act.freePlaces = 3
                act.currentlyMine = null
            })
            var leaveTimeRange = [775, 825, 875, 930, 980, 1020]
            if (!state.authorizedPersons || state.authorizedPersons.length == 0) {
                state.authorizedPersons = [{}]; // ensure one blank row ready for input
            }

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
                        monday: state.monday,
                        tuesday: state.tuesday,
                        wednesday: state.wednesday,
                        thursday: state.thursday,
                        friday: state.friday
                    },

                    // Are we currently displaying a conflict?
                    // Contains an array of time coflicts with following fields: activity, day, timeHome, leaveTime,
                    // timeActivity, fixedTimeHome (see confirmNoLeaveTimesActivityConflicts)
                    conflicts: null,

                    // Should the buttons be blocked because the form is still processing?
                    formIsSending: false,

                    // Admins can override email notification to parents
                    adminNotifyViaEmail: true,

                    // How many activities are we limited to in the first phase of the process?
                    twoPhaseProcessLimit: state.twoPhaseLimit,

                    // When will the two-phase process end?
                    twoPhaseProcessDeadline: new Date(state.twoPhaseEndMs),

                    // The current date that must be updated periodically (so the property becomes reactive)
                    currentDate: new Date(),

                    // Persons authorized to take children from school
                    authorizedPersons: state.authorizedPersons
                },
                computed: {
                    sendAdminEmailAsText: function() {
                        return this.adminNotifyViaEmail ? "obvesti starše preko e-pošte" : "NE obvesti staršev preko e-pošte"
                    },
                    selectedGroupsWithLimit: function() {
                        var limited = 0;
                        for (var a = 0; a < this.pupilGroups.length; a++) {
                            if (this.isGroupMembershipLimited(this.pupilGroups[a])) {
                                limited += 1;
                            }
                        }
                        return limited;
                    },
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
                    addPerson: function() {
                        this.authorizedPersons.push({});
                    },
                    removePerson: function(idx) {
                        this.authorizedPersons.splice(idx, 1);
                        if (this.authorizedPersons.length == 0) {
                            this.authorizedPersons.push({});
                        }
                    },
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
                    /** Returns true if this group really does have a limited membership count (we're just sending a large number otherwise as a legacy hack) */
                    isGroupMembershipLimited: function(group) {
                        return group.freePlaces < 100;
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
                        // selected activity IDs
                        var selectedActivityIds = _.map(this.pupilGroups, function(group) { return group.id })
                        var payload = {
                            extendedStay: this.extendedStay,
                            selectedActivities: selectedActivityIds,
                            monday: this.leaveTimes.monday,
                            tuesday: this.leaveTimes.tuesday,
                            wednesday: this.leaveTimes.wednesday,
                            thursday: this.leaveTimes.thursday,
                            friday: this.leaveTimes.friday,
                            notifyViaEmail: this.adminNotifyViaEmail,
                            authorizedPersons: this.authorizedPersons
                         }
                        this.formIsSending = true
                        var that = this

                        fetch("store", {
                            body: JSON.stringify(payload),
                            cache: 'no-cache',
                            credentials: 'include',
                            method: 'POST',
                            headers: {
                                'content-type': 'application/json'
                            }
                        }).then(function(response) {
                            that.formIsSending = false

                            if (response.status == 200) {
                                if (window.location.href.indexOf('/admin/') != -1) {
                                    window.location.href = "/admin"
                                } else {
                                    window.location.href = "/finish"
                                }
                            }
                            else if (response.status == 409) {
                                response.json().then(function(offending) {
                                    var taken = offending.join(", ")
                                    window.alert(
                                        "Žal so naslednje aktivnosti že polno zasedene: " + taken + "\n\n" +
                                        "Prosimo vas, da jih odstranite iz spiska izbranih dejavnosti in ponovno poizkusite potrditi vnos!"
                                    )
                                })
                            }
                            else if (response.status == 400) {
                                response.text().then(function(description) {
                                    window.alert(description);
                                });
                            }
                        })
                    },
                    beforeActivityDeadline: function() {
                        return this.twoPhaseProcessDeadline > this.currentDate;
                    },
                    atMaximumActivities: function() {
                        return this.beforeActivityDeadline() && this.twoPhaseProcessLimit <= this.selectedGroupsWithLimit
                    }
                }
            });

            // Make time reactive
            setInterval(function() { app.currentDate = new Date() }, 1000);


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
                        window.location.href = "/login-form"
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
