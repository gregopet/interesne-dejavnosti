var p12 = { day: 'P', from: 1, to: 2 }
var p23 = { day: 'P', from: 2, to: 3 }
var p34 = { day: 'P', from: 3, to: 4 }
var p14 = { day: 'P', from: 1, to: 4 }
var t14 = { day: 'T', from: 1, to: 4 }

describe("timeSlotsOverlap", function() {
	
	it("Detects identical intervals as overlapping", function() {
		expect(timeSlotsOverlap(p12, p12)).toBeTruthy();
	});
	
	it("Detects overlapping intervals as overlapping", function() {
		expect(timeSlotsOverlap(p12, p14)).toBeTruthy()
		expect(timeSlotsOverlap(p14, p12)).toBeTruthy()
	});
	
	it("Does not detect non-overlapping intervals as overlapping", function() {
		expect(timeSlotsOverlap(p12, p34)).toBeFalsy()
		expect(timeSlotsOverlap(p34, p12)).toBeFalsy()
	});
	
	it("Does not detect intervals from different days as overlapping", function() {
		expect(timeSlotsOverlap(t14, p14)).toBeFalsy()
	});
	
	it("Does not detect touching intervals as overlapping", function() {
		expect(timeSlotsOverlap(p12, p23)).toBeFalsy()
		expect(timeSlotsOverlap(p23, p12)).toBeFalsy()
	});
	
});

describe("timeSlotGroupsOverlap", function() {
	
	var P123 = [ p12, p23, p34 ]
	var P1 = [ p12 ]
	var P2 = [ p23 ]
	var P3 = [ p34 ]
	
	it("Returns true as soon as two of the intervals from the two groups overlap", function() {
		expect(timeSlotGroupsOverlap(P123, P1)).toBeTruthy()
		expect(timeSlotGroupsOverlap(P1, P123)).toBeTruthy()
	});
	
	it("Returns false if none of the intervals from the two groups overlap", function() {
		expect(timeSlotGroupsOverlap(P1, P2)).toBeFalsy()
		expect(timeSlotGroupsOverlap(P2, P1)).toBeFalsy()
		expect(timeSlotGroupsOverlap(P1, P3)).toBeFalsy()
		expect(timeSlotGroupsOverlap(P3, P1)).toBeFalsy()
	});
	
	it("Does not check if two groups passed in are non-overlapping", function() {
		expect(timeSlotGroupsOverlap([p12, p14], [t14])).toBeFalsy()
	});
	
});

describe("formatMinutes", function() {
	it("Formats times correctly", function() {
		expect(formatMinutes(915)).toEqual("15:15")
	})
	
	it("Zero-pads minutes lower than 10", function() {
		expect(formatMinutes(900)).toEqual("15:00")
	});
});
