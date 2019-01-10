# Razvojna baza

Če nimate postavljene nobene Postgres baze, si za potrebe razvoja lahko poženete
razvojno verzijo v Docker containerju. S pomočjo [Docker Compose](https://docs.docker.com/compose/)
orodja si lahko postavite Postgres instanco, dostopno za razvoj. Ko imate Docker
Compose nameščen, lahko zaženete ukaz `docker-compose up`, pa bo baza dostopna na
standardnem Postgres portu 5432 (port lahko spremenite tako, da popravite datoteko
`docker-compose.yml`).

Docker Compose bo bazo podatkov tudi avtomatsko zmigriral na zadnjo verzijo ter
jo napolnil s testnimi podatki. Med razvojem vam sicer novih migracij ni treba
zaganjati s pomočjo Docker Compose, saj aplikacija ob zagonu sama zažene 
manjkajoče migracije.

Če želite popolnoma počistiti bazo podatkov, lahko uporabite ukaz `docker compose up -V`.
