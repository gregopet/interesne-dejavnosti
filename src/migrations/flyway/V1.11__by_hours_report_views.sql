CREATE VIEW pupil_departures AS
SELECT id AS pupil_id, 'monday'::day_of_week AS day, leave_mon AS leave_minutes FROM pupil
UNION
SELECT id AS pupil_id, 'tuesday'::day_of_week AS day, leave_tue AS leave_minutes FROM pupil
UNION
SELECT id AS pupil_id, 'wednesday'::day_of_week AS day, leave_wed AS leave_minutes FROM pupil
UNION
SELECT id AS pupil_id, 'thursday'::day_of_week AS day, leave_thu AS leave_minutes FROM pupil
UNION
SELECT id AS pupil_id, 'friday'::day_of_week AS day, leave_fri AS leave_minutes FROM pupil;

COMMENT ON VIEW pupil_departures IS 'Pupil''s departures as a proper table to be used in reports';


CREATE VIEW activity_slots AS
SELECT activity_id, (slot).day, (slot).start_minutes, (slot).end_minutes FROM (SELECT id as activity_id, unnest(slots) AS slot FROM activity) AS t;

COMMENT ON VIEW activity_slots IS 'All the various slots belonging to activities';


CREATE TYPE named_strings AS (name text, strings text[]);
COMMENT ON TYPE named_strings IS 'A named array of strings';

CREATE VIEW departures_hourly_report AS
select grp.pupil_group, grp.day, grp.leave_minutes,
        ARRAY(
                select pupil.name 
                from pupil 
                join pupil_departures d on d.pupil_id = pupil.id 
                where
                    pupil.pupil_group = grp.pupil_group and
                    d.day = grp.day and
                    grp.leave_minutes is not null and grp.leave_minutes < d.leave_minutes
                order by pupil.name
        ) as still_here,
        ARRAY(
                select pupil.name 
                from pupil 
                join pupil_departures d on d.pupil_id = pupil.id 
                where
                    pupil.pupil_group = grp.pupil_group and
                    d.day = grp.day and
                    grp.leave_minutes is not null and grp.leave_minutes = d.leave_minutes
                order by pupil.name
        ) as leave,
        ARRAY(
                select row(activity.name, array_agg(pupil.name order by pupil.name))::named_strings
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
COMMENT ON VIEW departures_hourly_report IS 'A review of hourly activity per-class';
