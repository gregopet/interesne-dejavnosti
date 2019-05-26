CREATE TYPE authorized_person_type AS ENUM ('sibling', 'grandparent', 'aunt_uncle', 'other');
COMMENT ON TYPE authorized_person_type IS 'Type of person who can pick up the pupil from school';

CREATE TABLE authorized_companion (
    id SERIAL PRIMARY KEY,
    pupil_id BIGINT NOT NULL REFERENCES pupil(id),
    name TEXT NOT NULL,
    type authorized_person_type NOT NULL
);

COMMENT ON COLUMN authorized_companion.pupil_id IS 'Reference to the pupil whom these persons will pick up';
COMMENT ON COLUMN authorized_companion.name IS 'Name of the person who will pick up the pupil';
COMMENT ON COLUMN authorized_companion.type IS 'The family tie to the pupil';