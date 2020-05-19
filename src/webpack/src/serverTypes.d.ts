/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.23.603 on 2020-05-19 11:42:34.

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

    interface PupilSettings {
        extendedStay: boolean;
        selectedActivities: number[];
        monday: number | null;
        tuesday: number | null;
        wednesday: number | null;
        thursday: number | null;
        friday: number | null;
        notifyViaEmail: boolean;
        authorizedPersons: AuthorizedPerson[] | null;
        tueNice: string;
        thuNice: string;
        friNice: string;
        monNice: string;
        wedNice: string;
    }

    interface PupilState {
        activities: Activity[];
        extendedStay: boolean;
        monday: number | null;
        tuesday: number | null;
        wednesday: number | null;
        thursday: number | null;
        friday: number | null;
        twoPhaseLimit: number;
        twoPhaseEndMs: number;
        authorizedPersons: AuthorizedPerson[] | null;
    }

    interface TimeSlot {
        day: string;
        from: number;
        to: number;
        daySlovenian: string;
        daySlovenianShort: string;
    }

    type AuthorizedPersonType = "sibling" | "grandparent" | "aunt_uncle" | "other";

}
