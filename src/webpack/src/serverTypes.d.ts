/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.23.603 on 2020-05-30 07:17:37.

declare namespace Rest {

    interface Activity {
        id: number;
        name: string;
        description: string;
        leader: string;
        times: TimeSlot[];
        chosen: boolean;
        cost: string | null;
    }

    interface ActivityKt {
    }

    interface ActivityVacancy {
        id: number;
        free: number;
        currentlyMine: boolean;
    }

    interface AuthorizedPerson {
        name: string | null;
        type: AuthorizedPersonType | null;
    }

    interface PupilDaySettings {
        leaveTime: number | null;
        morningSnack: boolean;
        lunch: boolean;
        afternoonSnack: boolean;
    }

    interface PupilSettings {
        extendedStay: boolean;
        selectedActivities: number[];
        monday: PupilDaySettings;
        tuesday: PupilDaySettings;
        wednesday: PupilDaySettings;
        thursday: PupilDaySettings;
        friday: PupilDaySettings;
        notifyViaEmail: boolean;
        authorizedPersons: AuthorizedPerson[] | null;
        canLeaveAlone: boolean;
        morningWatchArrival: number | null;
        orderTextbooks: boolean;
    }

    interface PupilState {
        activities: Activity[];
        extendedStay: boolean;
        monday: PupilDaySettings;
        tuesday: PupilDaySettings;
        wednesday: PupilDaySettings;
        thursday: PupilDaySettings;
        friday: PupilDaySettings;
        twoPhaseLimit: number;
        twoPhaseEndMs: number;
        authorizedPersons: AuthorizedPerson[] | null;
        canLeaveAlone: boolean;
        morningWatchArrival: number | null;
        orderTextbooks: boolean;
    }

    interface TimeSlot {
        day: string;
        from: number;
        to: number;
        daySlovenianShort: string;
        daySlovenian: string;
    }

    interface MainPageForm {
        pupilName: string;
        pupilClass: string;
        pupilHasExtendedStay: boolean;
        closeDate: string;
        closeHour: string;
        morningWatchTimes: number[];
        leaveTimes: number[];
        firstPhaseLimit: number | null;
        firstPhaseEndDate: string;
        firstPhaseEndTime: string;
        askForSelfLeave: boolean;
        askForMorningWatch: boolean;
        askForTextbooks: boolean;
        afternoonSnackTime: number;
        inFirstPhase: boolean;
        adminRequest: boolean;
        morningWatchTimesJsonArray: string;
    }

    type AuthorizedPersonType = "sibling" | "grandparent" | "aunt_uncle" | "other" | "parent";

}
