ALTER TABLE pupil ADD leave_mon SMALLINT;
ALTER TABLE pupil ADD leave_tue SMALLINT;
ALTER TABLE pupil ADD leave_wed SMALLINT;
ALTER TABLE pupil ADD leave_thu SMALLINT;
ALTER TABLE pupil ADD leave_fri SMALLINT;

COMMENT ON COLUMN pupil.leave_mon IS 'Indicates the minutes of day at which pupil will leave school on monday (null means student is not participating on that day)';
COMMENT ON COLUMN pupil.leave_tue IS 'Indicates the minutes of day at which pupil will leave school on tuesday (null means student is not participating on that day)';
COMMENT ON COLUMN pupil.leave_wed IS 'Indicates the minutes of day at which pupil will leave school on wednesday (null means student is not participating on that day)';
COMMENT ON COLUMN pupil.leave_thu IS 'Indicates the minutes of day at which pupil will leave school on thursday (null means student is not participating on that day)';
COMMENT ON COLUMN pupil.leave_fri  IS 'Indicates the minutes of day at which pupil will leave school on friday (null means student is not participating on that day)';