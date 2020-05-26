ALTER TABLE pupil ADD order_textbooks BOOLEAN NOT NULL DEFAULT false;
COMMENT ON COLUMN pupil.order_textbooks IS 'Did the pupil order textbooks from the school''s textbook fund?';