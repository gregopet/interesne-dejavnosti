# Due to Java/Flyway not being able to connect to the socket, we must make Postgres listen on 127.0.0.1
pg_ctl -D "$PGDATA" -m fast -w stop
pg_ctl -D "$PGDATA" -o "-c listen_addresses='127.0.0.1'" -w start

/flyway/flyway -url=jdbc:postgresql://127.0.0.1:5432/interesne-dejavnosti -schemas=public -user=postgres -connectRetries=60 migrate

# Now let's leave Postgres as we found it!
pg_ctl -D "$PGDATA" -m fast -w stop
pg_ctl -D "$PGDATA" -o "-c listen_addresses=''" -w start
