import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator';
import { formatMinutes } from '../time';

@Component({
    filters: { formatMinutes },
    template: `
    <div>
        <div class="form-group row">
            <label :for="formControlId" class="col-sm-2 col-form-label col-form-label-sm">{{ day }}</label>
            <div class="col-sm-10">
                <select class="form-control form-control-sm" :id="formControlId" v-model="value.leaveTime">
                    <option v-bind:value="null">Po pouku</option>
                    <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                </select>
                
                <div v-if="canHaveSnack">
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" :id="formSnackControlId" v-model="value.afternoonSnack">
                        <label class="form-check-label" :for="formSnackControlId">Popoldanska malica</label>
                    </div>
                </div>
                <span class="text-secondary font-italic" v-else>Popoldanska malica je mogoča le, če učenec/učenka odide po {{ snackTime | formatMinutes }}</span>
            </div>
        </div>
    </div>
    `
})
class LeaveDayComponent extends Vue {
    @Prop({ required: true })
    day: String;

    get leaveTimeRange(): number[] {
        return (this.$parent as LeaveTimes).leaveTimeRange as number[];
    }

    get snackTime(): number {
        return (this.$parent as LeaveTimes).snackTime;
    }

    /** Does the pupil leave late enough so they can have the afternoon snack? */
    get canHaveSnack(): boolean {
        return this.value.leaveTime != null && this.value.leaveTime > this.snackTime;
    }

    @Prop({ required: true })
    value: Rest.PupilDaySettings;

    get formControlId(): string {
        return `${this.day}_leave_time`;
    }

    get formSnackControlId(): string {
        return `${this.day}_snack`;
    }
}  

@Component({
    components: {
        LeaveDayComponent
    },
    template: `
    <div>
        <div class="form-group">
            <label for="extended_stay_select">Prosimo, izberite, ali bo vaš otrok vključen v podaljšano bivanje:</label>
            <select class="form-control" v-model="value.extendedStay" id="extended_stay_select" v-cloak>
                <option v-bind:value="false">Ne, moj otrok ne bo udeležen v podaljšanem bivanju</option>
                <option v-bind:value="true">Da, moj otrok bo udeležen v podaljšanem bivanju</option>
            </select>
        </div>

        <p class="card-text" style="margin-top:3em;" v-if="value.extendedStay">
            Prosimo vas, da za vsak dan v tednu izberete čas odhoda domov ali označite, da otrok na ta dan ne bo udeležen
            v podaljšano bivanje.
        </p>

        <form class="leave-form" v-if="value.extendedStay" v-cloak>
            <LeaveDayComponent day="Ponedeljek" v-model="value.monday"></LeaveDayComponent>
            <LeaveDayComponent day="Torek" v-model="value.tuesday"></LeaveDayComponent>
            <LeaveDayComponent day="Sreda" v-model="value.wednesday"></LeaveDayComponent>
            <LeaveDayComponent day="Četrtek" v-model="value.thursday"></LeaveDayComponent>
            <LeaveDayComponent day="Petek" v-model="value.friday"></LeaveDayComponent>
        </form>

        <p>Če se med letom kdaj zgodi, da mora otrok iz šole oditi ob drugačnem času,
            morate za tisti dan izpolniti obrazec <a href="http://francebevk.splet.arnes.si/files/2018/09/Obvestilo_za_učitelja_OPB.pdf">
            Obvestilo za učitelja podaljšanega bivanja</a> (povezavo
            lahko najdete tudi na spletni strani šole).
        </p>
    </div>
    `
})
export default class LeaveTimes extends Vue {
    
    /** The student's state, including leave times */
    @Prop()
    value: Rest.PupilState;

    /** Minutes of day when students can leave */
    @Prop()
    leaveTimeRange: number[];
    
    /** Time of the afternoon snack - pupils can only have it if they leave later */
    @Prop({ required: true })
    snackTime: number;
}