CREATE TYPE day_of_week AS ENUM ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday');
COMMENT ON TYPE day_of_week IS 'The various days of week';

CREATE TYPE time_slot AS (
    day day_of_week,
    start_minutes SMALLINT,
    end_minutes SMALLINT
);
COMMENT ON TYPE time_slot IS 'Time slots at which activities are available';
COMMENT ON COLUMN time_slot.day IS 'Day on which the time slot takes place';
COMMENT ON COLUMN time_slot.start_minutes IS 'The minute of day on which this time slot starts';
COMMENT ON COLUMN time_slot.end_minutes IS 'The minute of day until which this time slot starts';

CREATE TABLE activity(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    leader TEXT,
    available_to_years SMALLINT[] NOT NULL,
    slots time_slot[] NOT NULL
);

COMMENT ON TABLE activity IS 'Contains activities children can participate in';
COMMENT ON COLUMN activity.name IS 'Name of this activity';
COMMENT ON COLUMN activity.description IS 'The longer description of this activity';
COMMENT ON COLUMN activity.leader IS 'Name of the person leading this activity';
COMMENT ON COLUMN activity.available_to_years IS 'All the student class years this activity is available to';
COMMENT ON COLUMN activity.slots IS 'The times during which this activity takes place';