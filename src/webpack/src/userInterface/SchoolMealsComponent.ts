import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';

/** The form for one day */
@Component({
    template: `
        <div class="form-group row mb-1">
            <label class="col-sm-2 col-form-label col-form-label-sm">{{ day }}</label>
            <div class="col-sm-10">
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" :id="checkboxIdBreakfast" v-model="value.morningSnack">
                    <label class="form-check-label" :for="checkboxIdBreakfast">Dopoldanska malica</label>
                </div>
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" :id="checkboxIdLunch" v-model="value.lunch">
                    <label class="form-check-label" :for="checkboxIdLunch">Kosilo</label>
                </div>
            </div>
        </div>
    `
})
class SingleDayComponent extends Vue {
    @Prop({ required: true })
    day: String

    /** The day config */
    @Prop({ required: true })
    value: Rest.PupilDaySettings;

    get checkboxIdBreakfast() {
        return `${this.day}_breakfast`
    }

    get checkboxIdLunch() {
        return `${this.day}_lunch`
    }
}

/** The complete form for the entire week */
@Component({
    components: {
        SingleDayComponent
    },
    template: `
    <div>
        <div class="card mt-3">
            <div class="card-body">
                <h4 class="card-title">Šolska prehrana</h4>

                <p class="card-text">
                    Za vsak dan v tednu vas prosimo, da označite, katere obroke naročate za svojega otroka.
                    <span v-if="vm.pupilHasExtendedStay">Popoldansko malico boste lahko naročili v spodnjem razdelku, ko boste urejali podaljšano bivanje.</span>
                </p>

                <SingleDayComponent day="Ponedeljek" v-model="value.monday"></SingleDayComponent>
                <SingleDayComponent day="Torek" v-model="value.tuesday"></SingleDayComponent>
                <SingleDayComponent day="Sreda" v-model="value.wednesday"></SingleDayComponent>
                <SingleDayComponent day="Četrtek" v-model="value.thursday"></SingleDayComponent>
                <SingleDayComponent day="Petek" v-model="value.friday"></SingleDayComponent>
            </div>
        </div>
    </div>
    `
})
export default class SchoolMealsComponent extends Vue {
    @Prop({ required: true })
    vm: Rest.MainPageForm;

    /** The pupil state we are binding against */
    @Prop({ required: true })
    value: Rest.PupilState;
}