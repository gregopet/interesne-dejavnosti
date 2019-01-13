# Prijava na popoldanske aktivnosti in podaljšano bivanje

Pred vami je spletna aplikacija, ki smo jo junija 2018 na osnovni šoli Franceta 
Bevka uporabili za prijavo učencev na popoldanske aktivnosti ter registracijo
časov odhodov domov v naslednjem šolskem letu.

Aplikacijo si boste najlažje predstavljali, če si pogledate 
[demonstracijsko verzijo](https://s3-us-west-1.amazonaws.com/publicstash/sola/login.html).
V njo se lahko prijavite brez gesla, podatki so izmišljeni in ne bo vam
pošiljala elektronske pošte, sicer pa izgleda enako, kot prava.

V naslednjih poglavjih si lahko preberete daljši opis aplikacije in navodila za
uporabo.


## Potek prijav - starši

Starši na svoj elektronski naslov za vsakega otroka prejmejo eno poštno
sporočilo. V tem sporočilu se nahaja ime otroka ter unikatna varnostna koda,
ki smo jo jim dodelili (ta varnostna koda nadomešča uporabniško ime in geslo,
zato mora biti primerno dolga - starše v mailu pozovemo, naj uporabijo kar
funkcijo kopiraj&prilepi). V sporočilu prav tako navedemo datum in uro, na
katero bo aplikacija pričela zbirati prijave, ter seveda internetni naslov,
na katerem lahko do aplikacije dostopajo.

Ob uri začetka prijav se staršem na internetnem naslovu začne prikazovati vnosno
polje, v katerega lahko vnesejo varnostno kodo. Po vnosu veljavne kode se jim
prikaže obrazec, na katerem je jasno zapisano ime njihovega otroka (da ne pride
do pomot) ter pri mlajših otrocih izbirnik časov odhoda domov za vsak dan v
tednu. Za vse otroke, tako mlajše kot starejše, pa je prisoten tudi obrazec za
izbiro interesnih dejavnosti. V seznamu lahko izberejo samo dejavnosti, primerne
za starostno skupino otroka. Pri vsaki dejavnosti se nahaja tudi število
preostalih prostih mest.

Aplikacija izvaja nekaj kontrol: starši otroka ne morejo prijaviti na 
dejavnosti, ki se med seboj časovno prekrivajo. Prav tako se kontrolira, da
morajo biti mlajši otroci prijavljeni v podaljšano bivanje vsaj do časa, ko
se na nek dan konča njihova zadnja interesna dejavnost. Starše, ki imajo
dober razlog za to, da njihov (mlajši) otrok ne bo v podaljšanem bivanju do
interesnih dejavnosti smo prosili, da to šoli obrazložijo osebno.

Po vnosu podatkov za svojega otroka morajo starši pritisniti na gumb za
shranjevanje obrazca. Če so bili za kakšno interesno dejavnost prepozni in
je dejavnost že polna, jih bo aplikacija pozvala, da izberejo kakšno drugo
dejavnost. V nasprotnem primeru se bodo interesne dejavnosti shranile, staršem
pa bo za njihovo evidenco poslano elektronsko sporočilo s povzetkom vsega, kar
so vnesli v obrazec (kopija sporočila se pošlje na šolski elektronski naslov).

Po želji lahko starši ponovno odprejo aplikacijo in vnešene podatke spremenijo -
po vsaki spremembi bodo dobili nov evidenčni mail.

Ko preteče rok za prijavo dejavnosti, je staršem prijava v aplikacijo ponovno 
onemogočena.


## Potek prijav - šola

Šola mora osebi, ki bo aplikacijo urejevala, zagotoviti spisek vseh učencev,
njihovih razredov, ter elektronske naslove njihovih staršev (za starše, ki
nimajo elektronskih naslovov, je možno njihove dostopne kode natisniti na
papir). Prav tako lahko dodajo tudi dokument s predstavitvijo dejavnosti, ki se 
bo priložil k prvem elektronskem sporočilu. Nazadnje pa morajo določiti še
datume, na katere se bodo prijave odprle in zaprle, da jih odgovorna oseba
vnese v aplikacijo.

Po odprtju aplikacije lahko šola spremlja stanje prijav na preprostem 
administratorskem vmesniku, ki se nahaja na poti `/admin` (dostop do tega
vmesnika je zaščiten s posebnim geslom). Preko tega vmesnika se tudi sproži
pošiljanje pozdravnega maila, ko so vsi podatki pripravljeni. Nazadnje pa je
preko vmesnika možen dostop do obrazca posameznih učencev tudi po datumu, ko
se za starše prijave že zaprejo (predvsem za reševanje posebnih zahtev in v
pomoč staršem, ki prijave niso uspeli izvesti sami).

Preko administratorskega vmesnika je mogoče dobiti tudi posebno datoteko z 
zapisom izbir za vse učence, ki lahko služi kot vhodni podatek programu za
načrtovanje skupin popoldanskih dejavnosti, ki ga ravno tako pripravljamo za 
potrebe šole Franceta Bevka (morda bo prišel prav tudi komu drugemu, ko bo
pripravljen). Ker so vsi podatki (razen elektronskih naslovov staršev) shranjeni
v tej datoteki, se lahko po izvozu spletno aplikacijo tudi ugasne.


## Programska implementacija

Aplikacija je "polavtomatska" - ne vsebuje popolnih vmesnikov za vnašanje
učencev in interesnih dejavnosti, saj smo pri nas to vnesli kar ročno v
bazo podatkov s pomočjo skriptnega jezika. Morda bomo tudi takšen vmesnik 
za naslednje leto še vgradili, vendar pa imajo šolski delavci že 
tako ogromno dela z vnašanjem v razne programe in smo jih z omenjenim pristopom
razbremenili ene dodatne tlake. 

Tudi ostalih stvari je ogromno kar vgrajenih v aplikacijo: ure popoldanskega
varstva, šolski email naslov in podobne reči so zaradi enostavnosti nastavljive
kar preko programske kode. Namen je bil, da se aplikacijo spiše čim bolj
jasno in direktno, na način, da bi morale tudi prilagoditve biti relativno
enostavne brez posebnega predznanja o neki specifični tehnologiji.


## Namestitev

Za namestitev aplikacije potrebujete strežnik, na katerem je nameščena Java
(verzija mora biti 8 ali višja) ter bazo podatkov PostgreSQL (pri nas je bila
uporabljena verzija 10.3, vendar bi bila za aplikacijo dovolj tudi kakšna nižja
verzija). Strežnik ne rabi biti posebno močan, v našem primeru sta tako
aplikacija kot baza tekli na skupnem virtualnem strežniku z 2GB pomnilnika in 1 
CPU-jem.

Vsekakor priporočamo, da zaradi varnosti aplikacijo ponudite preko protokola 
HTTPS. Aplikacija sama podpira protokol HTTPS, vendar je morda še lažje
namestiti reverse proxy, ki bo stal pred aplikacijo ter namesto nje 'govoril'
HTTPS (npr. Apache ali Nginx). Certifikat smo si priskrbeli brezplačno s pomočjo
brezplačne storitve [Let's Encrypt](https://letsencrypt.org/), namestitev tako 
za Apache kot za Nginx pa je resnično enostavna (vsaj pod operacijskim sistemom
Linux, z Windows žal nimamo izkušenj).

Glede na število učencev boste morda potrebovali tudi ponudnika masovnega
pošiljanja elektronske pošte - pri nas smo uporabili [Amazon Simple Email
Service](https://aws.amazon.com/ses/), ki je zelo ugoden in enostaven za 
uporabo. Vse, kar potrebujete, je ustvariti račun in na njem aktivirati vaš
`From:` naslov pošiljatelja - Amazon vam bo na ta naslov poslal povezavo, ki jo
morate odpreti kot dokazilo, da ste resnično lastniki tega email naslova. Ne 
pozabite pa pravočasno iti iz testnega v produkcijsko okolje, saj si bo Amazon
za ta korak vzel do 48 ur časa. Na koncu so se celotni stroški za pošiljanje 
elektronske pošte gibali okrog 1 evra.

Če boste namesto komercialnega ponudnika uporabili lastno rešitev za pošiljanje
elektronske pošte bodite pozorni, da elektronska pošta ne pristane v predalih
za vsiljeno pošto - zaradi ogromne količine spama je pošiljanje velikega števila
elektronskih sporočil včasih delikatna umetnost.


## Konfiguracija in zagon

Aplikacija se zapakira v standardno JAR datoteko, ki se jo zažene s pomočjo
Jave. Ta datoteka vsebuje tudi že vse potrebne knjižnjice za svoje delovanje.
Na operacijskem sistemu Linux smo jo pri nas potem zagnali s programom `screen`,
v okolju Windows pa bi bilo aplikacijo morda pametno zagnati kot storitev.

Ob zagonu lahko podate nekaj Java sistemskih parametrov, ki vplivajo na 
delovanje aplikacije:

### `config.file`

Parameter `config.file` vsebuje pot do konfiguracijske datoteke za aplikacijo.
V tej datoteki so podani podatki za priklop na bazo podatkov, SMTP strežnik in
lokacijo kataloga popoldanskih aktivnosti, ki se pošlje ob pozdravnem email
sporočilu. Primer se nahaja v datoteki `src/ratpack/config.yaml`, ki jo bo
aplikacija tudi privzeto uporabila, če ji ne boste navedli svoje.


### `migrate.database`

S parametrom `migrate.database` naročite aplikaciji, da ob zagonu skuša
posodobiti bazo v zadnje stanje (uporabnik, ki je nastavljen za dostop do baze,
mora v tem primeru imeti pravico spreminjati shemo baze). Aplikacijo boste
morali s tem parametrom zagnati vsaj enkrat, da vam vzpostavi shemo baze, nič
pa ni narobe, če aplikacijo s to nastavitvijo zaženete vsakič.

### `logback.configurationFile`

Beleženje dogodkov v aplikaciji se izvaja preko knjižnjice 
[Logback](https://logback.qos.ch/). Če želite konfigurirati, kam se hranijo
strežniški zapisi in v kakšnem zapisu, lahko s pomočjo parametra 
`logback.configurationFile` aplikaciji podate svoje nastavitve. Primere
konfiguracije si lahko dobite na domači strani projekta Logback, en primer pa
se nahaja tudi znotraj aplikacije v datoteki `src/ratpack/logback.xml`. Vsi
razredi, ki vsebujejo logiko prijave na interesne dejavnosti, se začnejo z
`si.francebevk`.


### Primer zagona

V našem primeru smo aplikacijo zaganjali z naslednjim ukazom:

```
java -Dconfig.file=/home/webapp/config.yaml -Dmigrate.database=true -Dlogback.configurationFile=/home/webapp/logback.xml -jar francebevk-running.jar
```

## Testiranje, prilagajanje in lasten razvoj

Priporočamo, da aplikacijo pred uporabo najprej stestirate in preverite, če je
prava za vas. Ravno tako morate pred uporabo popraviti vsaj nekaj tekstov ter
gesel (dober začetek je že, če povsod po kodi poiščete tekst `Franceta Bevka` 
ter ga zamenjate z imenom svoje šole).

### Testni zagon

Preden zaženete aplikacijo potrebujete Postgres bazo, v katero bo aplikacija
shranjevala podatke. Najlažji način je z uporabo orodja Docker Compose, za
katerega je konfiguracija dostopna v direktoriju `dev-db` (v tem primeru tudi
ne potrebujete nobenih sprememb v konfiguraciji aplikacije). Če pa želite
uporabiti kakšno drugo instanco Postgres baze, morate popraviti konfiguracijo
aplikacije, ki se nahaja v datoteki `src/ratpack/config.yaml`. 

Ko imate bazo pripravljeno zaženite `./gradlew run` (oziroma `gradlew.bat run`
na Windowsih) in aplikacija bi se morala zgraditi ter zagnati.

*Pozor*: če testno aplikacijo odpirate na lokalnem
računalniku, jo morate odpreti preko naslova `http://127.0.0.1:5050` in _ne_
preko naslova `http://localhost:5050`, sicer se vam na nekaterih sistemih lahko
zgodi, da prijava ne bo delovala! Ta težava na produkcijski verziji aplikacije
ni prisotna.

Ob vsaki spremembi morate aplikacijo ustaviti in ponovno
zagnati, lahko pa pri zagonu s pomočjo `gradlew` dodate parameter `-t`, da bo
orodje samo zaznalo spremembe in ponovno zagnalo aplikacijo.


### Namestitev na produkcijski strežnik

Da dobite JAR datoteko, pripravljeno za kopiranje na strežnik, zaženite ukaz 
`./gradlew shadowJar` (oziroma `gradlew.bat shadowJar` na Windowsih) - ustvarila
se vam bo JAR datoteka v direktoriju `build/libs`.

Za avtomatsko uploadanje datoteke na Linux strežnik lahko uporabite ukaz
`./gradlew deployProd`, vendar morate pred tem popraviti podatke o svojem
strežniku v datoteki `build.gradle` (poiščite razdelek `remotes`). Ta korak
ni nujno potreben, datoteko lahko na strežnik prenesete tudi ročno.


### Uvoz podatkov s pomočjo SQL stavkov

Kot je bilo omenjeno že zgoraj, smo uvoz podatkov o učencih in dejavnostih
izvedli s pomočjo skriptnega jezika, ki nam je iz vhodnih podatkov ustvaril
SQL stavke. Za pomoč prilagamo primere SQL stavkov, ki nam jih je zgeneriral
naš program (samega programa ne prilagamo, saj je zelo pogojen s virom, iz
katerega so bili prvotno zajeti podatki).

Za vnos razredov (ime tabele `pupil_grup` je bil obvezen zaradi tega, ker je 
`class` v večini programskih jezikov rezervirana beseda):

```sql
INSERT INTO pupil_group(name, year) VALUES ('3A', 3);
```

Za vnos učencev (dostopne kode smo zgenerirali iz naključnih števil, ki smo jih
spustili čez prilagojeno verzijo sistema 
[Proquint](https://github.com/dsw/proquint), saj ta ustvari kode, ki jih je
možno relativno enostavno narekovati tudi preko telefona):

```sql
INSERT INTO pupil(name, pupil_group, access_code, extended_stay, emails)
VALUES ('Branka Potočnik', '1A', 'kilad-dorod-haluf-tidoz', false, ARRAY['name1@example.org', 'name2@example.org`]::text[]);
```

Za vnos aktivnosti Nemščina, ki poteka vsak torek od 15:30 do 16:20, je na voljo
učencem največ 28 učencem 2. in 3. razredov in stane 5 evrov na mesec, bi vnos 
izgledal takole:

```sql
INSERT INTO activity(name, description, leader, available_to_years, slots, cost, max_pupils)
VALUES ('Nemščina', 'Nemščina je super', 'Leonarda Novak', ARRAY[2, 3]::smallint, ARRAY[ROW('tuesday',930, 980)]::timeslot[], '5 evrov na mesec', 28);
```

Za krožke, ki nimajo omejitve vpisa, smo uporabili kar neko zelo visoko številko
maksimalno prijavljenih, npr. 1000. Časi v dnevu so v bazi zakodirani s pomočjo
minute v dnevu (torej za 15:30 uporabite formulo `15 * 60 + 30 = 930`).


### Nadaljni razvoj

Če bi se kdo odločil za korenitejše spremembe, za lažje delo prilagamo spisek
glavnih orodij in tehnologij, uporabljenih pri izdelavi aplikacije. Za razvoj
predlagamo orodje [Intellij IDEA](https://www.jetbrains.com/idea/) - _Community_
verzija je brezplačna in popolnoma zadostuje za delo na projektu (da uvozite
projekt, uporabite funkcijo _Project from existing sources_ ter izberite datoteko
`build.gradle`).

Strežniški del aplikacije je večinoma napisan v programskem jeziku 
[Kotlin](https://kotlinlang.org/), deluje pa s pomočjo spletnega ogrodja
[Ratpack](https://ratpack.io/).

Uporabniški vmesnik je spisan v JavaScript-u s pomočjo orodja 
[Vue.js](https://vuejs.org/), za vizualni izgled pa skrbi knjižnjica 
[Bootstrap 4](https://getbootstrap.com/).

Za dostop do podatkov je uporabljena knjižnjica [jOOQ](https://www.jooq.org/),
shema baze pa se posodablja s pomočjo orodja [Flyway](https://flywaydb.org/) (
migracije se nahajajo v direktoriju `src/migrations`).
Vse tabele, indeksi, uporabniški tipi podatkov in pogledi so komentirani s 
pomočjo Postgres ukaza `COMMENT ON ...`, tako da si lahko podatkovni model 
razlagate s pomočjo teh podatkov (če za dostop do baze uporabljate orodje `psql`
lahko v njemu recimo napišete `\dt+ pupil`, da dobite opis vseh stolpcev v 
tabeli o učencih).

Tekstovni deli, tako HTML kot elektronska sporočila, se ustvarjajo s pomočjo
predlog [Rocker Templates](https://github.com/fizzed/rocker). Predloge se 
nahajajo v direktoriju `src/views`.

JavaScript in CSS datoteke se predprocesirajo s pomočjo orodja 
[Asset Pipeline](https://github.com/bertramdev/asset-pipeline), viri pa se
nahajajo v direktoriju `src/assets/`.

Za grajenje se uporablja orodje [Gradle](https://gradle.org/).

Seveda se priporočamo, da morebitne izboljšave pošljete nazaj tudi nam :)
