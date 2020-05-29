import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator';
import Activities from './ActivitiesComponent';
import AuthorizedPerson from './AuthorizedPersonComponent';
import LeaveTimes from './LeaveTimesComponent';
import MainPageComponent from './MainPageComponent';
import { UIActivity, DayOfWeek } from '../app';
import ActivitiesComponent from './ActivitiesComponent';
import * as $ from 'jquery';

/** Denotes a conflict between an activity and a leave time (pupil wants to leave before the activity starts */
interface Conflict {
    
    /** The activity that conflicts */
    activity: Rest.Activity;
    
    /** The day on which this conflict appears */
    day: DayOfWeek;
    
    /** The time at which the pupil wants to leave on the given day (null if pupil won't be in extended stay) */
    timeHome: number | null;
    
    /** The time at which the activity ends on the given day */
    timeActivity: number;
    
    /** The time at which the activity ends on the given day, _snapped to an actual valid leave time!_ */
    timeActivitySnapped: number | null;
}

/** Detects conflicts between selected activities */
@Component({
    template: `
    <div class="modal" tabindex="-1" role="dialog" ref="conflictModal" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document" v-if="conflicts">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Nedoslednost v času odhoda iz šole</h5>
                </div>
                <div class="modal-body">
                    <p>
                        Aktivnost {{ conflicts.activity.name }} se v {{ conflicts.day | day }} zaključi ob {{ conflicts.timeActivity | minuteTime }},
                        vi pa ste navedli<!--
                        --><span v-if="conflicts.timeHome"> čas odhoda domov že ob {{ conflicts.timeHome | minuteTime }}.</span><span v-else>, da vaš otrok ne bo v podaljšanjem bivanju.</span>
                    </p>
                    <p>
                        Prosimo, izberite, kako želite razrešiti to nedoslednost.
                    </p>
                    <div class="row">
                        <div class="col-sm-6">
                            <button type="button" class="btn btn-warning btn-block" @click.prevent="resolveConflictUpdateLeaveTime(conflicts.day, conflicts.timeActivitySnapped)">V {{ conflicts.day | day }} odhod ob {{ conflicts.timeActivitySnapped | minuteTime }}</button>
                        </div>
                        <div class="col-sm-6">
                            <button type="button" class="btn btn-danger btn-block" @click.prevent="resolveConflictRemoveActivity(conflicts.activity)">Odstrani {{ conflicts.activity.name }}</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `
})
export default class ActivityConflictComponent extends Vue {

    @Prop({ required: true })
    activitiesComponent: ActivitiesComponent;

    /** Are we currently displaying a conflict? */
    conflicts: Conflict | null = null;

    get mainPage(): MainPageComponent {
        return this.$parent as MainPageComponent;
    }

    /** Returns the DOM for the dialog */
    get dialogElement(): Element {
        return this.$refs.conflictModal as Element
    }

    @Watch('mainPage.state.monday') watchMon() { this.confirmNoLeaveTimesActivityConflicts() }
    @Watch('mainPage.state.tuesday') watchTue() { this.confirmNoLeaveTimesActivityConflicts() }
    @Watch('mainPage.state.wednesday') watchWed() { this.confirmNoLeaveTimesActivityConflicts() }
    @Watch('mainPage.state.thursday') watchThu() { this.confirmNoLeaveTimesActivityConflicts() }
    @Watch('mainPage.state.friday') watchFri() { this.confirmNoLeaveTimesActivityConflicts() }

    confirmNoLeaveTimesActivityConflicts() {
        const vm = this.mainPage.vm;
        if (!vm.pupilHasExtendedStay) return true;
        
        const latestLeaveTime = Math.max.apply(Math, vm.leaveTimes);
        const state = this.mainPage.state!!;

         // go through all selected activities
         for (var a = 0; a < state.activities.length; a++) {
            var activity = state.activities[a]

            // go through leave times
            if (activity.chosen && activity.times) {
                for (var s = 0; s < activity.times.length; s++) {
                    var slot = activity.times[s];
                    var leaveTime = state.extendedStay ? state[slot.day as DayOfWeek].leaveTime : null;

                    // Tolerate activity ending 5 minutes later than the leave time (it happens!)
                    if (leaveTime == null || leaveTime < slot.to - 5) {
                        
                        // other fix required for discrepancy (selector for leaving times doesn't know about 17:05!)
                        let timeActivitySnapped = vm.leaveTimes.find((t: number) => t >= slot.to) || null;
                        if (timeActivitySnapped == null && slot.to > latestLeaveTime) timeActivitySnapped = latestLeaveTime

                        this.conflicts = { activity: activity, day: slot.day as DayOfWeek, timeHome: leaveTime, timeActivity: slot.to, timeActivitySnapped }
                        $(this.dialogElement).modal({keyboard: false, backdrop: 'static'})
                        return // process conflicts one by one!
                    }
                }
            }
         }
         $(this.dialogElement).modal('hide') // no return? hide dialog, no conflicts
    }

    resolveConflictRemoveActivity(activity: UIActivity) {
        this.activitiesComponent.deselectActivity(activity);

        // check for further conflicts
        this.confirmNoLeaveTimesActivityConflicts()
    }

    resolveConflictUpdateLeaveTime(day: DayOfWeek, leaveTime: number) {
        const state = this.mainPage.state!!;
        const vm = this.mainPage.vm;
        state!![day].leaveTime = leaveTime

        // Pupil must have extended stay selected, otherwise leave time is pointless
        if (vm.pupilHasExtendedStay) {
            state!!.extendedStay = true
        }

        // check for further conflicts
        this.confirmNoLeaveTimesActivityConflicts()
    }


}