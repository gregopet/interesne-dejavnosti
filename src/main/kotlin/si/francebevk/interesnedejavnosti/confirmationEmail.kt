package si.francebevk.interesnedejavnosti

import si.francebevk.dto.Activity
import si.francebevk.dto.PupilSettings
import si.francebevk.viewmodel.MainPageForm

/** Creates the email message to send after a successful application */
fun composeEmail(vm: MainPageForm, leaveTimes: PupilSettings, activities: List<Activity>): String {
    val messageParts = mutableListOf<String>()

    messageParts.add(intro(vm))
    if (vm.askForTextbooks) {
        messageParts.add(textbookBlock(leaveTimes))
    }
    messageParts.add(meals(leaveTimes))
    if (vm.askForMorningWatch) {
        messageParts.add(morningWatch(leaveTimes))
    }
    if (vm.pupilHasExtendedStay) {
        messageParts.add(leaveTimes(vm, leaveTimes))
    }
    messageParts.add(activities(activities))
    messageParts.add(outro())



    return messageParts.joinToString(separator = "\n\n")
}

private fun intro(vm: MainPageForm): String {
    val subjects = mutableListOf("prehrano")
    if (vm.askForTextbooks) subjects.add("učbeniški sklad")
    if (vm.askForMorningWatch) subjects.add("jutranje varstvo")
    if (vm.pupilHasExtendedStay) subjects.add("podaljšano bivanje")

    val subjectsTxt = subjects.joinToString(", ")
    return """
        Spoštovani,
        
        vaša prijava na $subjectsTxt in interesne dejavnosti za učenca/učenko ${vm.pupilName} je bila sprejeta. Za vašo evidenco vam pošiljamo kopijo vnešenega.
    """.trimIndent()
}

private fun outro(): String = """
    Zahvaljujemo se vam za sodelovanje in vam želimo lepe počitnice!

    Lep pozdrav
    Barbara Kampjut, ravnateljica
""".trimIndent()

private fun textbookBlock(submition: PupilSettings): String {
    return if (submition.orderTextbooks) {
        "Naročili ste učbenike iz učbeniškega sklada."
    } else {
        "Niste naročili učbenikov iz učbeniškega sklada."
    }
}

private fun meals(submition: PupilSettings): String {
    return """
        Šolska prehrana, kot ste jo naročili po dnevih:
        Ponedeljek: ${submition.monday.prettyPrintSnacks()}
        Torek: ${submition.tuesday.prettyPrintSnacks()}
        Sreda: ${submition.wednesday.prettyPrintSnacks()}
        Četrtek: ${submition.thursday.prettyPrintSnacks()}
        Petek: ${submition.friday.prettyPrintSnacks()}
    """.trimIndent()
}

private fun morningWatch(submition: PupilSettings): String {
    return if (submition.morningWatchArrival != null) {
        "Otroka boste v jutranje varstvo pripeljali ob ${submition.morningWatchArrivalNice}"
    } else {
        "Vaš otrok ni vključen v jutranje varstvo."
    }
}

private fun leaveTimes(vm: MainPageForm, submition: PupilSettings): String {

    if (!submition.extendedStay) {
        return "Odločili ste se, da vaš otrok ne bo vključen v podaljšano bivanje. ${personsToAccompanyPupil(vm, submition)}"
    } else {
        val leaveBlock = """
        Ponedeljek: ${submition.monNice}
        Torek: ${submition.tueNice}
        Sreda: ${submition.wedNice}
        Četrtek: ${submition.thuNice}
        Petek: ${submition.friNice}
        """.trimIndent()

        return (
            "Odločili ste se, da vaš otrok bo vključen v podaljšano bivanje. Izbrali ste naslednje čase odhodov domov:\n" +
            leaveBlock + "\n\n" +
            personsToAccompanyPupil(vm, submition)
        )
    }
}

private fun personsToAccompanyPupil(vm: MainPageForm, submition: PupilSettings): String {
    val selfLeaveBlock = if (!vm.askForMorningWatch || !submition.canLeaveAlone) {
        "Vaš otrok šole ne sme zapustiti brez spremstva"
    } else {
        "Vaš otrok sme šolo ob času odhoda zapustiti tudi brez spremstva."
    }

    val persons = submition.authorizedPersons
    return if (persons.isNullOrEmpty()) {
        return "Za spremstvo otroka iz šole niste pooblastili nobene osebe. $selfLeaveBlock";
    } else {
        val personBlock = persons.map { person ->
            "|${person.name} (${person.type?.sloDescription ?: ""})"
        }.joinToString("\n")

        return """
        |Pooblastili ste naslednje osebe, da lahko vašega otroka prevzamejo iz šole:
        ${personBlock}
        |
        |${selfLeaveBlock}
        """.trimMargin("|")
    }
}

private fun activities(activities: List<Activity>): String {
    if (activities.isEmpty()) {
        return "Svojega otroka niste vpisali na nobeno interesno dejavnost."
    } else {
        return activities.map { act ->
            if (act.times.isEmpty()) act.name else {
                act.times.joinToString(", ", prefix = "${act.name} (", postfix = ")")
            }
        }.joinToString("\n", prefix = "Izbrali ste naslednje interesne dejavnosti:\n")
    }
}