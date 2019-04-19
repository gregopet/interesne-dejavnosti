CREATE TYPE activity_log_type AS ENUM ('login', 'abort', 'submit', 'failed-submit');

CREATE TABLE activity_log(
    id BIGSERIAL PRIMARY KEY,
    pupil_id BIGINT NOT NULL REFERENCES pupil(id),
    time TIMESTAMPTZ NOT NULL,
    admin_user BOOLEAN NOT NULL,
    type activity_log_type NOT NULL,
    details TEXT
);

CREATE INDEX activity_log_idx_pupil_id ON activity_log (pupil_id);

COMMENT ON TABLE activity_log IS 'Records the activity related to a pupil';
COMMENT ON COLUMN activity_log.pupil_id IS 'ID of the pupil to whom this log entry applies';
COMMENT ON COLUMN activity_log.time IS 'The time at which the event happened';
COMMENT ON COLUMN activity_log.admin_user IS 'True when the action was performed by an admin user, not the parent';
COMMENT ON COLUMN activity_log.type IS 'What actually happened?';
COMMENT ON COLUMN activity_log.details IS 'A description of the event that we can display when reconstructing events';
