ALTER TABLE pupil ADD extended_stay BOOLEAN;
UPDATE pupil SET extended_stay = true;
ALTER TABLE pupil ALTER COLUMN extended_stay SET NOT NULL;
COMMENT ON COLUMN pupil.extended_stay IS 'Indicates choice for pupil''s inclusion in extended stay';