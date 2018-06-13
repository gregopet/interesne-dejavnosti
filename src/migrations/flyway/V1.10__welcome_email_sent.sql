ALTER TABLE pupil ADD welcome_email_sent BOOLEAN NOT NULL DEFAULT FALSE;
UPDATE pupil SET welcome_email_sent = true; -- mails were already sent this time around
COMMENT ON COLUMN pupil.welcome_email_sent IS 'Set to true once an email was successfuly sent to this pupil';