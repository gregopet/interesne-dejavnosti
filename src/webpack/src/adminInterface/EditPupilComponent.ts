import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';
import * as $ from 'jquery';

@Component({
    template: `
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" ref="pupilEditDialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div v-if="editedPupil && !errorLoadingPupil">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">{{ editedPupil.firstName }} {{ editedPupil.lastName }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" v-if="!savingPupil">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="name">Ime</label>
                            <input class="form-control" id="name" v-model="editedPupil.first_name" ref="firstNameField">
                        </div>

                        <div class="form-group">
                            <label for="surname">Priimek</label>
                            <input class="form-control" id="surname" v-model="editedPupil.last_name">
                        </div>

                        <div class="form-group">
                            <label for="klass">Razred</label>
                            <select class="form-control" id="klass" v-model="editedPupil.pupil_group">
                                <option :value="klass" v-for="klass in klasses">{{klass}}</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="accessCode">Vstopna koda</label>
                            <input class="form-control" id="accessCode" v-model="editedPupil.access_code" readonly>
                        </div>

                        <div class="form-group">
                            <label for="email1">Elektronska pošta 1</label>
                            <input class="form-control" type="email" id="email1" v-model="editedPupil.emails[0]">
                        </div>

                        <div class="form-group">
                            <label for="email2">Elektronska pošta 2</label>
                            <input class="form-control" type="email" id="email2" v-model="editedPupil.emails[1]">
                        </div>


                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" @click.prevent="deletePupil()" v-if="editedPupil.id">Izbriši</button>
                        <button type="button" class="btn btn-warning" data-dismiss="modal" :disabled="savingPupil">Prekini</button>
                        <button type="button" class="btn btn-primary" @click.prevent="savePupil(editedPupil)" :disabled="savingPupil">Shrani</button>
                    </div>
                </div>
                <div v-if="errorLoadingPupil" class="modal-header">
                    {{ errorLoadingPupil }}
                </div>
                <div v-if="!editedPupil && !errorLoadingPupil" style="padding: 1em;">
                    NALAGAM...
                </div>
            </div>
        </div>
    </div>
    `
})
export default class EditPupilComponent extends Vue {

    @Prop({ required: true })
    klasses: string[];

    /** The pupil being edited - they may have to be loaded from the server, first */
    editedPupil: AdminRest.EditablePupil | null = null;

    /** True while we're still awaiting the pupil's data */
    loadingPupil = true;

    /** True while we're trying to save the pupil's data on the server */
    savingPupil = false;

    /** Had an error occured while we tried to get the pupil's information? What's the error message? */
    errorLoadingPupil: string | null = null;

    mounted() {
        // Clean up this component once the dialog closes
        $(this.$refs.pupilEditDialog)
            .on('hidden.bs.modal', (e) => {
                this.$el.parentNode?.removeChild(this.$el);
                this.$destroy();
            })
            .on('shown.bs.modal', (e) => {
                (this.$refs.firstNameField as HTMLElement).focus();
            });
    }

    private showDialog() {
        $(this.$refs.pupilEditDialog).modal()
    }

    /** Opens the pupil editing dialog */
    editPupil(pupilId: number) {
        fetch('/admin/pupil-editor/' + pupilId, { cache: 'no-cache', credentials: 'include' }).then((response) => {
            if (response.ok) {
                response.json().then( (pupil: AdminRest.EditablePupil) => {
                    while(pupil.emails.length < 2) {
                        pupil.emails.push("");
                    }
                    this.editedPupil = pupil;
                });
            }
        });
        this.showDialog();
    }

    /** Opens the pupil editing dialog for a new pupil */
    async createPupil() {
        this.showDialog();
        const accessCodeResponse = await fetch('/admin/proquint', { cache: 'no-cache', method: 'POST', credentials: 'include' });
        if (accessCodeResponse.ok) {
            const accessCode = await accessCodeResponse.text();
            this.editedPupil = {
                id: null,
                emails: [],
                first_name: "",
                last_name: "",
                pupil_group: this.klasses[0],
                access_code: accessCode
            }
            this.loadingPupil = false;
        } else {
            this.errorLoadingPupil = "Napaka :("
        }
    }

    /** Deletes the pupil for whom we have opened the editing dialog */
    private deletePupil() {
        if (window.confirm(`Ste prepričani, da želite izbrisati učenca ${this.editedPupil!!.first_name} ${this.editedPupil!!.last_name}?`)) {
            fetch('/admin/pupil-editor/' + this.editedPupil!!.id, {
                cache: 'no-cache',
                credentials: 'include',
                method: 'DELETE',
            }).then((response) => {
                if (response.ok) {
                    $(this.$refs.pupilEditDialog).modal('hide');
                    window.location.reload(false);
                } else {
                    this.errorLoadingPupil = "Napaka pri brisanju učenca :(";
                }
            })
        }
    }
    
    /** Saves the pupil on the server */
    savePupil(editedPupil: AdminRest.EditablePupil) {
        this.savingPupil = true;
        
        let method = 'POST';
        let url: string = '/admin/pupil-editor';
        const editingExistingPupil = !!editedPupil.id
        if (editedPupil.id) {
            method = 'PUT';
            url += '/' + editedPupil.id;
        }

        fetch(url, {
            method,
            cache: 'no-cache',
            credentials: 'include',
            headers: new Headers({'content-type': 'application/json'}),
            body: JSON.stringify(editedPupil),
        }).then((response) => {
            if (response.ok) {
                $(this.$refs.pupilEditDialog).modal('hide');
                window.location.reload(false);
            } else {
                this.errorLoadingPupil = "Napaka pri shranjevanju učenca :(";
            }
        })
    }
}