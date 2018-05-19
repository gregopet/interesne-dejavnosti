CREATE TABLE pupil_group (
	name VARCHAR(2) NOT NULL PRIMARY KEY,
	year SMALLINT
);

COMMENT ON TABLE pupil_group IS 'A class of pupils';


CREATE TABLE pupil (
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	pupil_group VARCHAR(2) NOT NULL REFERENCES pupil_group(name),
	access_code TEXT
);

COMMENT ON TABLE pupil IS 'Contains a single pupil';
COMMENT ON COLUMN pupil.id IS 'The pupil''s unique ID';
COMMENT ON COLUMN pupil.name IS 'The pupil''s name';
COMMENT ON COLUMN pupil.pupil_group IS 'The pupil''s class';
COMMENT ON COLUMN pupil.access_code IS 'The pupil''s access code required to access their profile';
