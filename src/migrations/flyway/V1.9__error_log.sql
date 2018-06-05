CREATE TABLE error_log (
    id BIGSERIAL PRIMARY KEY,
    pupil_id BIGINT NOT NULL REFERENCES pupil(id),
    created_on TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    message TEXT
);

COMMENT ON TABLE error_log IS 'Contains errors that may have happened during the process';
COMMENT ON COLUMN error_log.pupil_id IS 'Id of the pupil to which the error happened';
COMMENT ON COLUMN error_log.created_on IS 'The timestamp on which the error happened';
COMMENT ON COLUMN error_log.message IS 'The error message';