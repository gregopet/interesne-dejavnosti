declare namespace AdminRest {

    /** The type of interaction the pupil may have had with the application */
    type EventType = 'login' |'abort' | 'submit' | 'failed_submit';
    
    /** A descriptor of the pupil's interaction with the application */
    interface PupilInterfaceActivity {
        id: number;
        pupil_id: number;
        time: string;
        admin_user: boolean;
        type: EventType;
        details: string | null;
    }

    /** The interface for editing a pupil's data */
    interface EditablePupil {
        id: number | null;
        access_code?: string;
        first_name: string;
        last_name: string;
        pupil_group: string;
        emails: string[];
    }
}