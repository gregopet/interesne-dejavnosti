CREATE OR REPLACE VIEW departures_hourly_report AS
select grp.pupil_group, grp.day, grp.leave_minutes,
        ARRAY(
                select pupil.last_name || ' ' || pupil.first_name
                from pupil
                join pupil_departures d on d.pupil_id = pupil.id
                where
                    pupil.pupil_group = grp.pupil_group and
                    d.day = grp.day and
                    grp.leave_minutes is not null and grp.leave_minutes < d.leave_minutes and
                    not exists (
                        select *
                        from pupil_activity krozek
                        natural join activity_slots cas_krozka
                        where krozek.pupil_id = pupil.id and cas_krozka.day = d.day and cas_krozka.start_minutes = grp.leave_minutes
                    )
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
        ) as at_activities,
        ARRAY(
            select pupil.last_name || ' ' || pupil.first_name
            from pupil
            join pupil_departures d on d.pupil_id = pupil.id
            where
                pupil.pupil_group = grp.pupil_group and
                d.day = grp.day and
                d.leave_minutes is null
                order by pupil.last_name, pupil.first_name
        ) as not_included_today
from (
        select pupil.pupil_group, dep.day, dep.leave_minutes
        from pupil_departures dep
        join pupil on pupil.id = dep.pupil_id
        where dep.leave_minutes is not null
        group by pupil.pupil_group, dep.day, dep.leave_minutes

        union

        select distinct pupil.pupil_group, activity_slots.day, activity_slots.start_minutes
        from pupil_activity
        join pupil on pupil.id = pupil_activity.pupil_id
        natural join activity_slots

        order by 1, 2, 3 nulls first
) as grp;