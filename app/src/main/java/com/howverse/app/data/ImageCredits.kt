package com.howverse.app.data

/**
 * Attribution for every section photograph shipped in the app. Each photo comes from
 * Wikimedia Commons under a free licence (CC0, public domain, CC BY or CC BY-SA), so the
 * author, the licence and the source page have to travel with it.
 */
data class Credit(
    val sectionId: String,
    val section: String,
    val title: String,
    val author: String,
    val license: String,
    val licenseUrl: String,
    val pageUrl: String
)

object ImageCredits {

    // id ~~ section ~~ title ~~ author ~~ licence ~~ licence url ~~ source page
    private val raw: List<String> = """
a~~A · Art & Design~~Rembrandt The Artist in his studio~~Rembrandt~~Public domain~~~~https://commons.wikimedia.org/wiki/File:Rembrandt_The_Artist_in_his_studio.jpg
b~~B · Business & Management~~Business breakfast meeting. (54641476579)~~Foreign, Commonwealth & Development Office~~CC BY 4.0~~https://creativecommons.org/licenses/by/4.0~~https://commons.wikimedia.org/wiki/File:Business_breakfast_meeting._(54641476579).jpg
c~~C · Computer & Coding~~Code on computer monitor (Unsplash)~~Markus Spiske markusspiske~~CC0~~http://creativecommons.org/publicdomain/zero/1.0/deed.en~~https://commons.wikimedia.org/wiki/File:Code_on_computer_monitor_(Unsplash).jpg
d~~D · Digital Technology~~NOIRLab HQ Server Racks (6V6A0395-CC)~~NOIRLab/NSF/AURA/T. Slovinský~~CC BY 4.0~~https://creativecommons.org/licenses/by/4.0~~https://commons.wikimedia.org/wiki/File:NOIRLab_HQ_Server_Racks_(6V6A0395-CC).jpg
e~~E · Engineering~~Making sparks for parts (8625882)~~U.S. Air Force photo by Senior Airman Luis E. Rios Calderon~~Public domain~~~~https://commons.wikimedia.org/wiki/File:Making_sparks_for_parts_(8625882).jpg
f~~F · Food & Cooking~~Biryani Dan-Bauk~~SarKaLay စာကလေး~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Biryani_Dan-Bauk.jpg
g~~G · Geography & Geology~~Sawtooth Valley ID1~~Acroterion~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Sawtooth_Valley_ID1.jpg
h~~H · History & Heritage~~Temple heritage site in india~~Arvind Shrivastava~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Temple_heritage_site_in_india.jpg
i~~I · Internet & Media~~Anime Expo television camera 20100703~~SupportPDX~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Anime_Expo_television_camera_20100703.jpg
j~~J · Journalism & Communication~~Press Interview (01612750) (49639603006)~~IAEA Imagebank~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Press_Interview_(01612750)_(49639603006).jpg
k~~K · Knowledge & Research~~Books on shelves (120)~~Mide min~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Books_on_shelves_(120).jpg
l~~L · Languages & Linguistics~~Gulzar calligraphic panel (Gulzar script)~~Hasan Zarrin Qalam~~Public domain~~~~https://commons.wikimedia.org/wiki/File:Gulzar_calligraphic_panel_(Gulzar_script).jpg
m~~M · Mathematics~~CMAP - Centre de Mathématiques Appliquées de l'Ecole polytechnique~~Jérémy Barande~~CC BY-SA 3.0~~https://creativecommons.org/licenses/by-sa/3.0~~https://commons.wikimedia.org/wiki/File:CMAP_-_Centre_de_Math%C3%A9matiques_Appliqu%C3%A9es_de_l%27Ecole_polytechnique.jpg
n~~N · Nature & Environment~~Graddiselva river in Junkerdalen, Saltdal, Nordland, Norway, 2018 September~~Ximonic (Simo Räsänen)~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Graddiselva_river_in_Junkerdalen,_Saltdal,_Nordland,_Norway,_2018_September.jpg
o~~O · Office & Productivity~~Middle-aged businesswoman typing on laptop at home office~~Shixart1985~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Middle-aged_businesswoman_typing_on_laptop_at_home_office.jpg
p~~P · Physics & Science~~Equipment and glassware~~Belikov Maxim~~CC BY 4.0~~https://creativecommons.org/licenses/by/4.0~~https://commons.wikimedia.org/wiki/File:Equipment_and_glassware.jpg
q~~Q · Quality & Professional Skills~~Solomon Water laboratory technician Susan Makabo shows Minister Parke how to test water quality (10721644154)~~Department of Foreign Affairs and Trade~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Solomon_Water_laboratory_technician_Susan_Makabo_shows_Minister_Parke_how_to_test_water_quality_(10721644154).jpg
r~~R · Robotics & Research~~Industrial automation setup shows robotic arm and equipment for laboratory tasks~~Shixart1985~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Industrial_automation_setup_shows_robotic_arm_and_equipment_for_laboratory_tasks.jpg
s~~S · Skills & Self Development~~Young people working in groups at tables 2022~~Halynamelnychuk~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Young_people_working_in_groups_at_tables_2022.jpg
t~~T · Transport & Technology~~Intercity Express Train leaving Swindon Station - geograph.org.uk - 6005497~~Vieve Forward~~CC BY-SA 2.0~~https://creativecommons.org/licenses/by-sa/2.0~~https://commons.wikimedia.org/wiki/File:Intercity_Express_Train_leaving_Swindon_Station_-_geograph.org.uk_-_6005497.jpg
u~~U · Utilities & Everyday Life~~Plumbing work - temporary closure of water pipes - on Pinsker Street in Jerusalem - A~~Korenn~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Plumbing_work_-_temporary_closure_of_water_pipes_-_on_Pinsker_Street_in_Jerusalem_-_A.jpg
v~~V · Visual & Performing Arts~~Concert stage of Severance Hall~~Cbusram~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Concert_stage_of_Severance_Hall.jpg
w~~W · World Cultures & Society~~Festival of lights Diwali celebrations Hamburg Germany November 2013 c~~John Heaven~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Festival_of_lights_Diwali_celebrations_Hamburg_Germany_November_2013_c.jpg
x~~X · Exploration & Advanced Science~~Stanford Dish February 2013 029~~King of Hearts~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Stanford_Dish_February_2013_029.jpg
y~~Y · Yoga, Wellness & Sports~~Beach asana class, Plage Pereire, Arcachon, 2015~~DimiTalen~~CC0~~http://creativecommons.org/publicdomain/zero/1.0/deed.en~~https://commons.wikimedia.org/wiki/File:Beach_asana_class,_Plage_Pereire,_Arcachon,_2015.jpg
z~~Z · Zero to Advanced Practical Learning~~Hands-on Training for Safety Regulators (02210558) (42682565642)~~IAEA Imagebank~~CC BY 2.0~~https://creativecommons.org/licenses/by/2.0~~https://commons.wikimedia.org/wiki/File:Hands-on_Training_for_Safety_Regulators_(02210558)_(42682565642).jpg
star~~★ · Additional Global Learning Areas~~Alexander Dalrymple's globe, Royal Scottish Museum~~Stephencdickson~~CC BY-SA 4.0~~https://creativecommons.org/licenses/by-sa/4.0~~https://commons.wikimedia.org/wiki/File:Alexander_Dalrymple%27s_globe,_Royal_Scottish_Museum.jpg
""".trimIndent().lines().filter { it.isNotBlank() }

    val all: List<Credit> = raw.map { line ->
        val f = line.split("~~")
        Credit(
            sectionId = f[0],
            section = f[1],
            title = f[2],
            author = f[3],
            license = f[4],
            licenseUrl = f.getOrElse(5) { "" },
            pageUrl = f.getOrElse(6) { "" }
        )
    }

    fun bySection(sectionId: String): Credit? = all.firstOrNull { it.sectionId == sectionId }

    fun summary(): String = all
        .groupingBy { it.license }
        .eachCount()
        .toList()
        .sortedBy { it.first }
        .joinToString(" · ") { "${it.second} x ${it.first}" }
}
