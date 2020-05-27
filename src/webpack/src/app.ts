import 'bootstrap/dist/css/bootstrap.min.css';
import Vue from 'vue';
import { formatMinutes, formatDay } from './time';
import * as _ from 'lodash';
import * as $ from 'jquery';
import 'bootstrap/js/dist/modal.js';
import AuthorizedPersonComponent from './userInterface/AuthorizedPersonComponent';
import LeaveTimes from './userInterface/LeaveTimesComponent';
import Activities from './userInterface/ActivitiesComponent';
import MainPageComponent from './userInterface/MainPageComponent';

Vue.filter('minuteTime', formatMinutes)
Vue.filter('day', formatDay)
Vue.component('authorizedperson', AuthorizedPersonComponent);
Vue.component('leavetimes', LeaveTimes);
Vue.component('activities', Activities);


export interface UIActivity extends Rest.Activity {
    freePlaces: number;
    currentlyMine: boolean | null;
}

export type DayOfWeek = 'monday' | 'tuesday' | 'wednesday' | 'thursday' | 'friday';

/** The view model for this page, allowing us to lay it out */
declare const viewModel: Rest.MainPageForm;

const mainComponent = new MainPageComponent({ propsData: { vm: viewModel } });
mainComponent.$mount('#component');