import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';
import { timeSlotGroupsOverlap } from '../time';
import * as _ from 'lodash';

interface UIActivity extends Rest.Activity {
    freePlaces: number;
    currentlyMine: boolean | null;
}


/**
 * Allows the user to pick their activities.
 */
@Component({
    template: `
    <div class="row">
        <div class="col-md-7">
            <div class="form" v-cloak>
                <div class="form-group">
                    <select class="form-control" id="izbirnik-opisov" v-model="currentGroup">
                        <option v-for="group in sortedActivities" v-bind:value="group"> {{ group.name }}</option>
                    </select>
                </div>
                <div v-if="currentGroup">
                    <h4>

                        <span v-if="currentGroup.chosen">
                            <button class="btn btn-danger btn-block" @click.prevent="deselectActivity(currentGroup)">Odstrani dejavnost {{ currentGroup.name }}</button>
                        </span><span v-else>
                            <div v-if="hasConflictWithActivity(currentGroup)" class="text-danger">
                                Ne morete izbrati aktivnosti, ker se časovni termini pokrivajo z dejavnostjo {{hasConflictWithActivity(currentGroup)}}!
                            </div>
                            <div v-else>
                                <button class="btn btn-primary btn-block" @click.prevent="selectActivity(currentGroup)" v-if="!atMaximumActivities || !isGroupMembershipLimited(currentGroup)">Izberi dejavnost {{ currentGroup.name }}</button>
                                <a href="#firstPhaseAlert" class="btn btn-link" v-else>
                                    Izbrali ste maksimalno število aktivnosti z omejitvijo vpisa!
                                </a>
                            </div>
                        </span>

                    </h4>

                    <p>
                        <small>Nosilec/nosilka dejavnosti: {{ currentGroup.leader }}</small>
                        <br>
                        <small v-if="isGroupMembershipLimited(currentGroup)">
                            Št. preostalih prostih mest: {{ currentGroup.freePlaces }}
                            <span v-if="currentGroup.currentlyMine === true" class="text-success"><i>(na aktivnost je vaš otrok trenutno prijavljen)</i></span>
                            <span v-if="currentGroup.currentlyMine === false"><i>(na aktivnost vaš otrok trenutno ni prijavljen)</i></span>
                        </small>
                        <small v-else>
                            Število mest ni omejeno.
                        </small>
                        <br>
                        <small v-if="currentGroup.cost" class="text-danger">Cena: {{ currentGroup.cost }}</small>
                    </p>

                    <div v-if="currentGroup.times.length">
                        <h6>Termini:</h6>
                        <ul>
                            <li v-if="currentGroup.times" v-for="(times, day) in currentGroup.times">
                                {{ times.day | day }}, {{ times.from | minuteTime }} - {{ times.to | minuteTime }}
                            </li>
                        </ul>
                    </div>

                    <div v-html="currentGroup.description">
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4 offset-md-1" v-cloak>
            <div v-if="pupilGroups.length == 0" class="alert alert-danger" role="alert">
                <strong>Pozor!</strong> Trenutno niste izbrali še nobene dejavnosti!
                <hr>
                Poiščite željeno dejavnost in jo izberite s pritiskom na modri gumb!
            </div>
            <div v-else>
                <p v-if="!areActivitiesLimited">Izbrali ste naslednje dejavnosti:</p>
                <div v-else>
                    <p v-if="selectedActivitiesWithAttendanceLimit == 0">Trenutno izbrane aktivnosti:</p>
                    <p v-else>Izbrali ste si {{ selectedActivitiesWithAttendanceLimit }} od {{ maxActivitiesWithAttendanceLimit }} dejavnosti z omejitvijo vpisa:</p>
                </div>
                <ul class="list-unstyled" v-for="act in pupilGroups">
                    <li>
                        {{act.name}}
                        <a href="#" @click.prevent="deselectActivity(act)"><small>(odstrani)</small></a>
                        <div v-if="isGroupMembershipLimited(act)">
                            <em class="small">(aktivnost ima omejitev vpisa)</em>
                        </div>
                        <ul>
                            <li v-for="(times, day) in act.times">
                                {{ times.day | day }}, {{ times.from | minuteTime }} - {{ times.to | minuteTime }}
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    `
})
export default class ActivitiesComponent extends Vue {

    /** The student's state, including leave times */
    @Prop()
    value: Rest.PupilState;


    /** Minutes of day when students can leave */
    @Prop()
    leaveTimeRange: number[];

    /** 
     * The available activities, sorted alphabetically and enhanced with extra UI properties; objects
     * are still the same, though, so any change will be reflected on the state.
     */
    sortedActivities: UIActivity[] = [];

    /** What activity is currently displayed in the UI? */
    currentGroup: UIActivity = this.value.activities[0] as UIActivity;

    /** Date as a reactive property */
    currentDate: Date = new Date();
    private dateIntervalHandle?: number;

    mounted() {
        this.sortedActivities = _.sortBy(this.value.activities, function(a) { return a.name.toLowerCase() }) as UIActivity[];
        this.sortedActivities.forEach( (act) => {
            // display something until the vacancy comes through
            act.freePlaces = 3;
            act.currentlyMine = null;
        });
        this.currentGroup = this.sortedActivities[0];

        // Make time reactive
        this.dateIntervalHandle = window.setInterval( () => this.currentDate = new Date(), 1000);
    }

    destroyed() {
        if (this.dateIntervalHandle) {
            window.clearInterval(this.dateIntervalHandle);
        }
    }

    /** Trigger time conflict detection on parent */
    confirmNoLeaveTimesActivityConflicts() {
        (this.$parent as any).confirmNoLeaveTimesActivityConflicts()
    }

    /** Select this activity for pupil */
    selectActivity(act: UIActivity) { 
        act.chosen = true 
        this.confirmNoLeaveTimesActivityConflicts();
    }

    /** Remove this activity from pupil's suggestion */
    deselectActivity(act: UIActivity) { act.chosen = false }

    /** What activities did the pupil already select? */
    get pupilGroups(): UIActivity[] {
        return this.sortedActivities.filter( (activity) => activity.chosen );
    }

    /** Did the pupil already select the maximum limited-attendance activities they are allowed to? */
    get atMaximumActivities(): boolean {
        if (!this.areActivitiesLimited) return false;
        return this.selectedActivitiesWithAttendanceLimit >= this.value.twoPhaseLimit;
    }

    /** Return whether we impose a limit on the pupil's groups with limited membership */
    get areActivitiesLimited(): boolean {
        return this.currentDate.getTime() < this.value.twoPhaseEndMs;
    }

    /** Returns the number of limited attendance activities the pupil has chosen */
    get selectedActivitiesWithAttendanceLimit(): number {
        return this.pupilGroups.filter(this.isGroupMembershipLimited).length
    }

    /** Returns the max number of restricted activities (or null if there is no limit) */
    get maxActivitiesWithAttendanceLimit(): number | null {
        if (!this.areActivitiesLimited) return null;
        else return this.value.twoPhaseLimit;
    }

    /** Returns the name of any selected activity that may share times with this one */
    hasConflictWithActivity(activity: UIActivity): string | null {
        var conflicting = this.pupilGroups.find( selectedGroup => timeSlotGroupsOverlap(selectedGroup.times, activity.times));
        return conflicting ? conflicting.name : null;
    }

    /** Returns true if this group really does have a limited membership count (we're just sending a large number otherwise as a legacy hack) */
    isGroupMembershipLimited(activity: UIActivity): boolean {
        return activity.freePlaces < 100;
    }
}