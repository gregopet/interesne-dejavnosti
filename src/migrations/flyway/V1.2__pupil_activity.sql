CREATE TABLE pupil_activity (
    pupil_id BIGINT REFERENCES pupil(id),
    activity_id BIGINT REFERENCES activity(id),
    PRIMARY KEY(activity_id, pupil_id)
);

COMMENT ON TABLE pupil_activity IS 'A mapping table between pupils and their chosen activities';