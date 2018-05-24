ALTER TABLE activity ADD cost TEXT;
ALTER TABLE activity ADD max_pupils SMALLINT;
UPDATE activity SET max_pupils = 28;
ALTER TABLE activity ALTER COLUMN max_pupils SET NOT NULL;


COMMENT ON COLUMN activity.cost IS 'A textual description of the cost for this activity';
COMMENT ON COLUMN activity.max_pupils IS 'Maximum number of pupils for this activity';