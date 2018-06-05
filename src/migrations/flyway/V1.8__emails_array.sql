ALTER TABLE pupil ADD emails TEXT[] NOT NULL DEFAULT ARRAY[]::text[];
COMMENT ON COLUMN pupil.emails IS 'All the emails to which messages for this pupil should be sent';

UPDATE pupil SET emails = ARRAY[email] WHERE email IS NOT NULL;
ALTER TABLE pupil DROP COLUMN email;

