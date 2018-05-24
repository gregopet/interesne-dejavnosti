def text = new File("spisek-dejavnosti.csv").text
def fields = text.split("([|]|KONEC)", -1) as List

def getDay = { s ->
	if      (!s) return null
	else if (s.startsWith("Ponedeljek")) return "monday"
	else if (s.startsWith("Torek")) return "tuesday"
	else if (s.startsWith("Sreda")) return "wednesday"
	else if (s.startsWith("Četrtek")) return "thursday"
	else if (s.startsWith("Petek")) return "friday"
	else throw new IllegalArgumentException("Unknown day for string $s")
}

def parseTime = { s ->
	try {
		def parts = s.split("[.]")
		def hour = Integer.parseInt(parts[0])
		def minute = Integer.parseInt(parts[1])
		return hour * 60 + minute
	} catch(Exception ex) {
		throw new RuntimeException("Error parsing time '$s'")
	}
}

def sanitize = { s ->
	s?.replace("'", "''")
}

def getTimes = { s ->
	if (!s?.trim()) return null
	def day = getDay(s)
	def from = s.split(" ")[1]
	def to = s.split(" ")[3]
	return [
		day: day,
		from: parseTime(from),
		to: parseTime(to)
	]
}

println "INSERT INTO activity(name, description, leader, available_to_years, slots) VALUES"
fields.collate(8).eachWithIndex { row, i->
	def name = row[0].trim()
	def actor = row[1]?.trim() ?: ""
	def classes = row[2]?.split(",")?.collect { it.trim() }.findAll() ?: []
	def price = row[4]?.trim() ?: ""
	def maxPupils = row[5]?.trim() ? Integer.parseInt(row[5].trim()) : 0
	def description = row[6]?.trim()
	def days = row[3]?.split("\n")?.collect { getTimes(it?.trim()) }.findAll().collect { "ROW('${it.day}', ${it.from}, ${it.to})" }
	
	if (i) print ','
	println "('${sanitize(name)}', '${sanitize(description)}', '${sanitize(actor)}', ARRAY$classes::smallint[], ARRAY[${days.join(',')}]::time_slot[])"
}
println ";"
