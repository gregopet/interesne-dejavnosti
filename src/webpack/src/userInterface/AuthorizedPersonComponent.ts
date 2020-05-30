import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';

/**
 * The component to edit authorized persons.
 * 
 * Will emit a 'remove' event when user clicks on the 'remove' button.
 * @property value The person we are editing here
 */
@Component({
    template: `
    <div>
        <div class="col">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Ime in priimek" v-model="value.name">
            </div>
        </div>
        <div class="col-sm-auto">
            <select class="form-control" v-model="value.type">
                <option value="parent">Oče / mati</option>
                <option value="sibling">Sestra / brat</option>
                <option value="grandparent">Babica / dedek</option>
                <option value="aunt_uncle">Teta / stric</option>
                <option value="other">Druga pooblaščena oseba</option>
            </select>
        </div>
        <div class="col">
            <button v-if="canRemovePerson" class="btn btn-outline-danger" type="button" @click="remove">Odstrani</button>
        </div>
    </div>
    `
})
export default class AuthorizedPersonComponent extends Vue {
    
    /** The person bound to the control */
    @Prop()
    value: Rest.AuthorizedPerson;

    /** The total number of persons in this person set */
    @Prop()
    totalPersons: number;

    /** Can we remove this person? */
    get canRemovePerson(): boolean {
        return !!this.value.name || this.totalPersons > 1;
    }

    remove() {
        this.$emit('remove', this.value);
    }
    
}