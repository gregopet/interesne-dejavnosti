import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';

@Component({
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
            <div class="form-group row">
                <label for="pon" class="col-sm-2 col-form-label col-form-label-sm">Ponedeljek</label>
                <div class="col-sm-10">
                    <select class="form-control form-control-sm" id="pon" v-model="value.monday">
                        <option v-bind:value="null">Po pouku</option>
                        <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="tor" class="col-sm-2 col-form-label col-form-label-sm">Torek</label>
                <div class="col-sm-10">
                    <select class="form-control form-control-sm" id="tor" v-model="value.tuesday">
                        <option v-bind:value="null">Po pouku</option>
                        <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="sre" class="col-sm-2 col-form-label col-form-label-sm">Sreda</label>
                <div class="col-sm-10">
                    <select class="form-control form-control-sm" id="sre" v-model="value.wednesday">
                        <option v-bind:value="null">Po pouku</option>
                        <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="cet" class="col-sm-2 col-form-label col-form-label-sm">Četrtek</label>
                <div class="col-sm-10">
                    <select class="form-control form-control-sm" id="cet" v-model="value.thursday">
                        <option v-bind:value="null">Po pouku</option>
                        <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="pet" class="col-sm-2 col-form-label col-form-label-sm">Petek</label>
                <div class="col-sm-10">
                    <select class="form-control form-control-sm" id="pet" v-model="value.friday">
                        <option v-bind:value="null">Po pouku</option>
                        <option v-bind:value="time" v-for="time in leaveTimeRange">{{ time | minuteTime }}</option>
                    </select>
                </div>
            </div>
        </form>
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
}