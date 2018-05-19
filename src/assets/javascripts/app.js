var groups = [
	{
		name: 'Judo',
		title: 'Judo',
		description: 'Judo (japonsko 柔道, hepburn jūdō) je moderna japonska borilna veščina, ki se je razvila konec 19. stoletja (formalno ustanovljena okoli leta 1882) iz več japonskih tradicionalnih (t. i. veščine korju) oblik ju jutsuja, njen začetnik pa je Džigoro Kano.\nJudo je goloroka borilna veščina, ki vključuje borbo stoje in na tleh, mete, padce, vzvode, udarcev z rokami in nogami pa Judo neposredno ne vsebuje. Tako je na prvi pogled zelo podoben rokoborbi in je v osnovi ena izmed najbolj razširjenih oblik le-te.',
		tutor: 'Jožef Horvat',
		times: {
			'Ponedeljek': '15:00 - 16:00',
			'Sreda': '17:00 - 18:00'
		}
	}, {
		name: 'Šah',
		title: 'Šah',
		description: 'Šah je igra na igralni deski za dva igralca. Šteje za miselno namizno potezno igro z dolgo tradicijo in visoko ravnjo organiziranosti po celem svetu.\nZa igranje je potrebna šahovnica s 64 črno-belimi polji (8 · 8) in 6 različno močnih in različno gibajočih se figur.',
		tutor: 'Marija Golob',
		times: {
			'Ponedeljek': '14:00 - 15:00',
			'Sreda': '17:00 - 18:00'
		}
	}, {
		name: 'Plesne urice',
		title: 'Plesne urice',
		description: 'Ples je vrsta izražanja, umetnosti in zabave. Ples je govorica telesa, ki se izraža skozi ritem glasbe in lahko predstavlja stil človekovega življenja, kajti z vsakim gibom se v človekovi duši ustvarja zadovoljstvo. Ples je del kulturne izobrazbe vsakega posameznika in je kultura posameznega naroda.\nPles je tudi umetnost in hkrati šport, v katerem se prepleta usklajenost dveh ali več teles. Najstarejši slikovni zapisi o plesu, segajo celo do 15 000 let nazaj. Nekoč je bil ples vezan na religijo in njene običaje, z razvojem kultur je postal še oblika zabave sprva majhnih skupin, kasneje pa način preživljanja prostega časa velikih množic.',
		tutor: 'Ana Zupančič',
		times: {
			'Torek': '15:30 - 16:30',
			'Četrtek' : '16:00 - 17:00'
		}
		
	}
]

Vue.component('paragraphs', {
	render: function(createElement) {
		return createElement('div', _.map(this.text.split('\n'), function(txt) {
			return createElement('p', txt);
		}));
	},
	props: {
		text: {
			type: String,
			required: true
		}
	}
});

var app = new Vue({
	el: '#app',
	data: {
		groups: groups,
		currentGroup: groups[0],
		pupilGroups: []
	},
	methods: {
		isSelected: function(group) { 
			return _.find(this.pupilGroups, group);
		},
		select: function(group) {
			this.pupilGroups.push(group);
		},
		deselect: function(group) {
			this.pupilGroups = _.without(this.pupilGroups, group);
		}
	}
});
