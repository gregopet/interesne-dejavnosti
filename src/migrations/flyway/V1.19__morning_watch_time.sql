ALTER TABLE pupil ADD morning_care_arrival SMALLINT;
COMMENT ON COLUMN pupil.morning_care_arrival IS 'If not null then the pupil will arrive into morning care at this time';