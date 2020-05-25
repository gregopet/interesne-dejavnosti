ALTER TABLE pupil ADD can_leave_alone BOOLEAN NOT NULL DEFAULT false;
COMMENT ON COLUMN pupil.can_leave_alone IS 'If true, younger pupils can still leave school without an escort';
