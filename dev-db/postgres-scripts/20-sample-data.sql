begin;

INSERT INTO pupil_group(name, year) values ('1A', 1), ('2A', 2), ('3A', 3), ('4A', 4), ('5A', 5);

INSERT INTO pupil(name, pupil_group, access_code, extended_stay) values
	('Leon Uršič', '1A', '1', true),
	('Jana Jazbec', '2A', '2', true),
	('Branka Potočnik', '3A', '3', true),
	('Vid Bogataj', '4A', '4', true),
	('Albina Sušnik', '5A', '5', true);

INSERT INTO activity(name, leader, available_to_years, max_pupils, slots, description) VALUES
('Judo', 'Jožef Kokolj', ARRAY[1, 2, 3], 20, ARRAY[
	ROW('monday', 900, 1200),
	ROW('wednesday', 840, 1020)
]::time_slot[], 'Judo (japonsko 柔道, hepburn jūdō) je moderna japonska borilna veščina, ki se je razvila konec 19. stoletja (formalno ustanovljena okoli leta 1882) iz več japonskih tradicionalnih (t. i. veščine korju) oblik ju jutsuja, njen začetnik pa je Džigoro Kano.\nJudo je goloroka borilna veščina, ki vključuje borbo stoje in na tleh, mete, padce, vzvode, udarcev z rokami in nogami pa Judo neposredno ne vsebuje. Tako je na prvi pogled zelo podoben rokoborbi in je v osnovi ena izmed najbolj razširjenih oblik le-te.'
),
('Šah',  'Manica Hudobivnik', ARRAY[2, 3, 4], 24 ,ARRAY[
	ROW('monday', 960, 1020),
	ROW('thursday', 840, 900)
]::time_slot[], 'Šah je igra na igralni deski za dva igralca. Šteje za miselno namizno potezno igro z dolgo tradicijo in visoko ravnjo organiziranosti po celem svetu.\nZa igranje je potrebna šahovnica s 64 črno-belimi polji (8 · 8) in 6 različno močnih in različno gibajočih se figur.'
),
('Tek', 'Hitra Obratovič', ARRAY[1, 2, 3, 4, 5, 6, 7, 8, 9], 15, ARRAY[
	ROW('tuesday', 960, 1020),
	ROW('thursday', 840, 900)
]::time_slot[], 'Tek na 3000 m z zaprekami (tudi tek na 3000 m z ovirami) je srednjeprogaška atletska disciplina.\nImenuje se tudi kot angleški izvirnik steeplechase [stíplčeis] (ali pogovorno [stíplčez]), ki je generično ime za sorodno in istoimensko disciplino konjske dirke po progi z ovirami, pripravljenimi na prostem v naravi.Znan je tudi kot okrajšani izvirnik steeple.\nTek temelji na prevladovanju štirih suhih in ene vodne zapreke (prepreke) na krog (400 m), izvaja pa se na atletskih tekmovanjih na odprtih športnih prirediščih. Od leta 1920 je v moški konkurenci olimpijska disciplina, od leta 2008 pa tudi v ženski.'
),
('Plesne urice', 'Ledenko Drsič', ARRAY[3, 4, 5], 5, ARRAY[
	ROW('monday', 840, 900)
]::time_slot[], 'Ples je vrsta izražanja, umetnosti in zabave. Ples je govorica telesa, ki se izraža skozi ritem glasbe in lahko predstavlja stil človekovega življenja, kajti z vsakim gibom se v človekovi duši ustvarja zadovoljstvo. Ples je del kulturne izobrazbe vsakega posameznika in je kultura posameznega naroda.\nPles je tudi umetnost in hkrati šport, v katerem se prepleta usklajenost dveh ali več teles. Najstarejši slikovni zapisi o plesu, segajo celo do 15 000 let nazaj. Nekoč je bil ples vezan na religijo in njene običaje, z razvojem kultur je postal še oblika zabave sprva majhnih skupin, kasneje pa način preživljanja prostega časa velikih množic.'
);


INSERT INTO pupil_group(name, year) values ('6A', 6), ('7A', 7), ('8A', 8), ('9A', 9);

INSERT INTO pupil(name, pupil_group, access_code, extended_stay) values
	('Franc Hribar', '6A', '6', false),
	('Marija Fras', '7A', '7', false),
	('Ana Vidmar', '8A', '8', false),
	('Anton Turk', '9A', '9', false);


commit;