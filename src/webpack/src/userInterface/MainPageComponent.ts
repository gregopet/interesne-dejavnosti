import Vue from 'vue';
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator';
import Activities from './ActivitiesComponent';
import AuthorizedPerson from './AuthorizedPersonComponent';
import LeaveTimes from './LeaveTimesComponent';
import ActivityConflict from './ActivityConflictComponent';
import SchoolMeals from './SchoolMealsComponent';
import { DayOfWeek, UIActivity } from '../app';

@Component({
    components: {
        Activities,
        AuthorizedPerson,
        LeaveTimes,
        ActivityConflict,
        SchoolMeals
    },

    template: `
    <div class='container mb-3'>
        <ActivityConflict ref="conflictComponent" :activities-component="$refs.activitySelector"></ActivityConflict>

        <div class="alert alert-danger fixed-top" v-if="vm.adminRequest">
            <strong>Administratorski dostop:</strong>

            <label class="ml-2" for="send-email">pošlji email staršem?</label>
            <input type="checkbox" id="send-email" v-model="adminNotifyViaEmail">
        </div>

        <h1 v-if="vm.askForTextbooks">Prijava na razširjen program, prehrano in naročilo za učbeniški sklad za šolsko leto 2020/2021</h1>
        <h1 v-else>Prijava na razširjen program in prehrano za šolsko leto 2020/2021</h1>

        <p class="lead">
            Učenec/učenka: <strong> {{ vm.pupilName }}, {{ vm.pupilClass }}</strong>
        </p>

        <p class="lead" v-if="vm.pupilHasExtendedStay">
            Vnesite podatke o času odhoda iz šole in o interesnih dejavnostih vašega otroka ter izbiro potrdite s pritiskom na zeleni gumb
            na dnu te strani.
        </p>
        <p class="lead" v-else>
            Vnesite podatke o interesnih dejavnostih vašega otroka ter izbiro potrdite s pritiskom na zeleni gumb
            na dnu te strani.
        </p>

        <div class="alert alert-primary" role="alert" id="firstPhaseAlert" v-if="vm.inFirstPhase">
            <h4>Dragi starši!</h4>
            <p>
                Število učencev na posameznih krožkih je žal omejeno.
            </p>
            <p>
                Da bi čim več otrok imelo možnost izbrati svojo najljubšo dejavnost, lahko krožke brez omejitve števila
                prijav sicer izbirate poljubno, izmed krožkov z omejenim vpisom pa si lahko vsak otrok izbere največ {{ vm.firstPhaseLimit }}!
            </p>
            <p>
                Podrobnosti o vašem skupnem številu prijav lahko vidite na desni, podatek o omejitvi vpisa za posamezno dejavnost pa na levi strani območja za izbiro interesne dejavnosti.
            </p>
            <p>
                Hvala za razumevanje!
            </p>
        </div>

        <div class="alert alert-warning" role="alert">
            Vnešene podatke lahko naknadno še spremenite, prosimo pa vas, da s popravki zaključite do <strong>{{ vm.closeDate }} ob {{ vm.closeHour }}</strong>!
            Po tem datumu bodo morebitne spremembe mogoče samo še z izrecnim soglasjem šole!
        </div>

        <div v-if="state">

            <div class="card mt-3" id="morning-watch" v-cloak v-if="state && vm.askForMorningWatch">
                <div class="card-body">
                    <h4 class="card-title">Jutranje varstvo</h4>

                    <h5>Označite uro prihoda svojega otroka v jutranje varstvo</h5>
                    <div class="form-group">
                        <select class="form-control" v-model="state.morningWatchArrival">
                            <option :value="null">Ne, moj otrok ne bo v jutranjem varstvu</option>
                            <option :value="time" v-for="time in vm.morningWatchTimes">Da, moj otrok bo v jutranje varstvo prišel ob {{ time | minuteTime }}</option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="card mt-3" v-if="vm.askForTextbooks">
                <div class="card-body">
                    <h4 class="card-title">Učbeniški sklad</h4>
                    <h5>Želite naročiti komplet učbenikov iz učbeniškega sklada?</h5>

                        <select class="form-control" v-model="state.orderTextbooks">
                        <option :value="false">Ne, ne želim naročiti kompleta učbenikov, ki ga je določila šola</option>
                        <option :value="true">Da, za svojega otroka naročam komplet učbenikov, ki ga je določila šola</option>
                    </select>
                </div>
            </div>

            <SchoolMeals :vm="vm" v-model="state"></SchoolMeals>

            <div class="card mt-3" id="extended-stay-card" v-if="vm.pupilHasExtendedStay">
                <div class="card-body">
                    <h4 class="card-title">Odhodi iz šole</h4>
                    <LeaveTimes v-model="state" :leave-time-range="vm.leaveTimes" :snack-time="vm.afternoonSnackTime"></LeaveTimes>

                    <div v-if="vm.askForSelfLeave">
                        <h5>Dovolite vašemu otroku, da zapusti šolo sam, brez spremstva?</h5>
                        <div class="form-group">
                            <select class="form-control" v-model="state.canLeaveAlone">
                                <option :value="false">Ne, moj otrok šole ne sme zapustiti brez spremstva</option>
                                <option :value="true">Da, moj otrok sme šolo zapustiti sam, brez spremstva</option>
                            </select>
                        </div>
                    </div>

                    <h5>Katere osebe smejo prevzeti otroka ob koncu pouka?</h5>
                    <p>Prosimo, navedite vse osebe, ki bi lahko tekom šolskega leta prevzela otroka!</p>
                    <div class="form" v-cloak>
                        <AuthorizedPerson class="form-row mb-2" v-model="person" v-on:remove="removePerson(index)" v-for="(person, index) in state.authorizedPersons" :total-persons="state.authorizedPersons.length"></AuthorizedPerson>

                        <button class="btn btn-link" @click.prevent="addPerson()">Dodaj osebo</button>
                    </div>
                </div>
            </div>


            <div class="card mt-3">
                <div class="card-body">
                    <h4 class="card-title">Interesne dejavnosti</h4>
                    <div class="row">
                        <div class="col-md-7">
                            <label for="izbirnik-opisov"><small>Poiščite željeno dejavnost v spodnjem seznamu ter jo izberite s pritiskom na modri gumb:</small></label>
                        </div>
                    </div>

                    <Activities ref="activitySelector" v-model="state" :leave-time-range="vm.leaveTimes"></Activities>
                </div>
            </div>



            <div class="row mt-4" v-cloak>
                <div class="col-6">
                    <button v-if="vm.adminRequest" class="btn btn-block" @click.prevent="save()" :disabled="formIsSending" v-bind:class="{ 'btn-warning': adminNotifyViaEmail, 'btn-danger': !adminNotifyViaEmail }">Shrani in {{sendAdminEmailAsText}}</button>
                    <button v-else class="btn btn-success btn-block" @click.prevent="save()" :disabled="formIsSending">Zaključujem in potrjujem izbor {{ numberOfSelectedGroups }} dejavnosti</button>
                </div>
                <div class="col-6">
                    <a v-if="vm.adminRequest" class="btn btn-link btn-block" href="/admin">Nazaj na spisek učencev</a>
                    <button v-else class="btn btn-danger btn-block" @click.prevent="logout()" :disabled="formIsSending">Prekinitev</button>
                </div>
            </div>


        </div>
    </div>
    `
})
export default class MainPageComponent extends Vue {
    
    /** The page viewmodel */
    @Prop({required: true})
    vm: Rest.MainPageForm;

    /** The current state for this pupil (what activities are selected etc..) */
    state: Rest.PupilState | null = null;

    /** Should the buttons be blocked because the form is still processing? */
    formIsSending = false;

    /** Admins can override email notification to parents */
    adminNotifyViaEmail = true;

    /** Handle to a timer that needs to be destroyed when page unloads */
    vacancyTimer: number | null = null;

    mounted() {
        this.fetchState();

        this.checkVacancy()
        this.vacancyTimer = window.setInterval(this.checkVacancy, 5000)
    }

    beforeDestroy() {
        if (this.vacancyTimer) {
            window.clearInterval(this.vacancyTimer);
        }
    }

    addPerson() {
        this.state!!.authorizedPersons!!.push({ name: null, type: null });
    }

    removePerson(idx: number) {
        this.state!!.authorizedPersons!!.splice(idx, 1);
        this.ensureAtLeastOneAuthorizedPerson();
    }

    ensureAtLeastOneAuthorizedPerson() {
        if (!this.state!!.authorizedPersons || this.state!!.authorizedPersons.length == 0) {
            this.state!!.authorizedPersons = [{ name: null, type: null }]; 
        }
    }

    logout() {
        if (confirm("Ste prepričani da želite zapreti stran, ne da bi shranili spremembe?")) {
            window.location.href = "/logout"
        }
    }

    confirmNoLeaveTimesActivityConflicts() {
        this.conflictComponent.confirmNoLeaveTimesActivityConflicts();
    }

    /** Number activities selected */
    get numberOfSelectedGroups(): number {
        return this.state!!.activities.filter( (act: Rest.Activity) => act.chosen).length
    }

    get sendAdminEmailAsText() {
        return this.adminNotifyViaEmail ? "obvesti starše preko e-pošte" : "NE obvesti staršev preko e-pošte"
    }

    /** Save the form and quit if save was OK */
    save() {
        const state = this.state!!;
        
        // selected activity IDs
        var selectedActivityIds = state.activities.filter( (act) => act.chosen).map( (act) => act.id );
        var payload: Rest.PupilSettings = {
            extendedStay: state.extendedStay,
            selectedActivities: selectedActivityIds,
            monday: state.monday,
            tuesday: state.tuesday,
            wednesday: state.wednesday,
            thursday: state.thursday,
            friday: state.friday,
            notifyViaEmail: this.adminNotifyViaEmail,
            authorizedPersons: state.authorizedPersons,
            canLeaveAlone: state.canLeaveAlone,
            morningWatchArrival: state.morningWatchArrival,
            orderTextbooks: !!state.orderTextbooks
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
    }

    // Vacancy checks
    async checkVacancy() {
        const response = await fetch("vacancy", {
            credentials: 'include',
            cache: 'no-cache',
            headers: new Headers({
                "X-Requested-With": "XMLHttpRequest"
            })
        })
        
        if (response.status == 200) {
            const payload: Rest.ActivityVacancy[] = await response.json();
            this.state!!.activities.forEach( (act: UIActivity) => {
                var status = payload.find( (p) => p.id == act.id);
                if (status) {
                    act.freePlaces = status.free
                    act.currentlyMine = status.currentlyMine
                }
            });
        } else if (response.status == 401) {
            alert("Žal je prišlo do neznane napake, vaša seja je bila prekinjena! Prosimo vas, da se ponovno prijavite!")
            window.location.href = "/login-form"
        } else {
            // what? wait a bit (maybe just a temporary glitch?) and then panic?
        }
    }

    private async fetchState() {
        const response = await fetch("state?rnd=" + Math.floor(Math.random() * Math.floor(1000000)), { credentials: 'include', cache: 'no-cache' } )
        this.state = await response.json() as Rest.PupilState
        this.ensureAtLeastOneAuthorizedPerson();   
    }

    /** Returns the component for resolving conflicts via its ref */
    private get conflictComponent(): ActivityConflict {
        return this.$refs.conflictComponent as ActivityConflict;
    }
}