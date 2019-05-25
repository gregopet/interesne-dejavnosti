ALTER TABLE pupil ADD first_name TEXT;
COMMENT ON COLUMN pupil.first_name IS 'First name of the pupil';

ALTER TABLE pupil ADD last_name TEXT;
COMMENT ON COLUMN pupil.last_name IS 'Last name of the pupil';

UPDATE pupil SET
    first_name = substr(name, 0, strpos(name, ' ')),
    last_name = substr(name, strpos(name, ' ') + 1)
;
ALTER TABLE pupil ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE pupil ALTER COLUMN last_name SET NOT NULL;

CREATE OR REPLACE VIEW departures_hourly_report AS
select grp.pupil_group, grp.day, grp.leave_minutes,
        ARRAY(
                select pupil.last_name || ' ' || pupil.first_name
                from pupil
                join pupil_departures d on d.pupil_id = pupil.id
                where
                    pupil.pupil_group = grp.pupil_group and
                    d.day = grp.day and
                    grp.leave_minutes is not null and grp.leave_minutes < d.leave_minutes
                order by pupil.last_name, pupil.first_name
        ) as still_here,
        ARRAY(
                select pupil.last_name || ' ' || pupil.first_name
                from pupil
                join pupil_departures d on d.pupil_id = pupil.id
                where
                    pupil.pupil_group = grp.pupil_group and
                    d.day = grp.day and
                    grp.leave_minutes is not null and grp.leave_minutes = d.leave_minutes
                order by pupil.last_name, pupil.first_name
        ) as leave,
        ARRAY(
                select row(activity.name, array_agg(pupil.last_name || ' ' || pupil.first_name order by pupil.last_name, pupil.first_name))::named_strings
                from activity_slots
                join pupil_activity on pupil_activity.activity_id = activity_slots.activity_id
                join pupil on pupil.id = pupil_activity.pupil_id
                join activity on activity.id = pupil_activity.activity_id
                where
                    pupil.pupil_group = grp.pupil_group and
                    activity_slots.day = grp.day and
                    activity_slots.start_minutes = grp.leave_minutes
                group by activity.name
                order by activity.name
        ) as at_activities
from (
        select pupil.pupil_group, dep.day, dep.leave_minutes
        from pupil_departures dep
        join pupil on pupil.id = dep.pupil_id
        where dep.leave_minutes is not null
        group by pupil.pupil_group, dep.day, dep.leave_minutes
        order by 1, 2, 3 nulls first
) as grp;


ALTER TABLE pupil DROP COLUMN name;
