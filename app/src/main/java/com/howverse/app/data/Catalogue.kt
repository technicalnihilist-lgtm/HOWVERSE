package com.howverse.app.data

/**
 * The complete HOWVERSE A-to-Z learning catalogue: 26 lettered sections plus a
 * section of additional global areas, and the twelve learning modes every
 * section can be taught in.
 */
data class Section(
    val id: String,
    val letter: String,
    val title: String,
    val blurb: String,
    val art: String,
    val topics: List<String>
)

object Catalogue {

    val modes: List<String> = listOf(

        "Beginner to Advanced",
        "Step-by-Step Tutorials",
        "Practical Projects",
        "Interactive Lessons",
        "Examples and Demonstrations",
        "Exercises and Quizzes",
        "Troubleshooting",
        "Real-World Applications",
        "Learning Roadmaps",
        "Skill Assessment",
        "Personalized Learning",
        "Multilingual Teaching"
    )

    val sections: List<Section> = listOf(

        Section(
            id = "a",
            letter = "A",
            title = "Art & Design",
            blurb = "Make things people can see, use and feel.",
            art = "art/a.png",
            topics = listOf(
            "Art",
            "Drawing",
            "Painting",
            "Sketching",
            "Digital Art",
            "Graphic Design",
            "UI Design",
            "UX Design",
            "Logo Design",
            "Illustration",
            "Typography",
            "Calligraphy",
            "Sculpture",
            "Architecture Design",
            "Fashion Design",
            "Interior Design",
            "Animation",
            "3D Modelling",
            "Photography"
            )
        ),
        Section(
            id = "b",
            letter = "B",
            title = "Business & Management",
            blurb = "Run something, sell something, lead people.",
            art = "art/b.png",
            topics = listOf(
            "Business Management",
            "Entrepreneurship",
            "Marketing",
            "Digital Marketing",
            "Branding",
            "Advertising",
            "E-commerce",
            "Retail",
            "Sales",
            "Accounting",
            "Bookkeeping",
            "Business Planning",
            "Human Resources",
            "Leadership",
            "Project Management",
            "Customer Service",
            "Business Analytics"
            )
        ),
        Section(
            id = "c",
            letter = "C",
            title = "Computer & Coding",
            blurb = "Write code, ship software, understand the machine.",
            art = "art/c.png",
            topics = listOf(
            "Computer Science",
            "Computer Basics",
            "Programming",
            "HTML",
            "CSS",
            "JavaScript",
            "Python",
            "Java",
            "Kotlin",
            "C",
            "C++",
            "C#",
            "PHP",
            "SQL",
            "Web Development",
            "App Development",
            "Game Development",
            "Software Engineering",
            "Databases",
            "Operating Systems",
            "Linux",
            "Cloud Computing"
            )
        ),
        Section(
            id = "d",
            letter = "D",
            title = "Digital Technology",
            blurb = "The internet, data and the tools built on them.",
            art = "art/d.png",
            topics = listOf(
            "Digital Literacy",
            "Internet Technology",
            "Networking",
            "Cybersecurity Awareness",
            "Data Science",
            "Artificial Intelligence",
            "Machine Learning",
            "Deep Learning",
            "Blockchain Concepts",
            "Automation",
            "Robotics Software",
            "Cloud Storage",
            "Digital Privacy",
            "Computer Hardware",
            "Troubleshooting"
            )
        ),
        Section(
            id = "e",
            letter = "E",
            title = "Engineering",
            blurb = "Design and build physical systems.",
            art = "art/e.png",
            topics = listOf(
            "Electrical Engineering",
            "Electronics",
            "Mechanical Engineering",
            "Civil Engineering",
            "Chemical Engineering",
            "Aerospace Engineering",
            "Automotive Engineering",
            "Robotics",
            "Mechatronics",
            "Industrial Engineering",
            "Biomedical Engineering",
            "Renewable Energy Engineering",
            "Structural Engineering",
            "Environmental Engineering"
            )
        ),
        Section(
            id = "f",
            letter = "F",
            title = "Food & Cooking",
            blurb = "Cook it, bake it, understand it.",
            art = "art/f.png",
            topics = listOf(
            "Cooking",
            "Baking",
            "Food Science",
            "Nutrition Education",
            "World Cuisines",
            "Indian Cuisine",
            "Chinese Cuisine",
            "Italian Cuisine",
            "Japanese Cuisine",
            "Food Preservation",
            "Kitchen Skills",
            "Meal Planning",
            "Food Presentation",
            "Culinary Arts"
            )
        ),
        Section(
            id = "g",
            letter = "G",
            title = "Geography & Geology",
            blurb = "Where things are and what the ground is made of.",
            art = "art/g.png",
            topics = listOf(
            "World Geography",
            "Countries",
            "Capitals",
            "Maps",
            "Cartography",
            "Geology",
            "Rocks and Minerals",
            "Earth Science",
            "Climatology",
            "Oceanography",
            "Meteorology",
            "Natural Resources",
            "Environmental Geography"
            )
        ),
        Section(
            id = "h",
            letter = "H",
            title = "History & Heritage",
            blurb = "How we got here, and what we left behind.",
            art = "art/h.png",
            topics = listOf(
            "World History",
            "Ancient Civilizations",
            "Medieval History",
            "Modern History",
            "Archaeology",
            "Cultural Heritage",
            "World Religions (academic study)",
            "Historical Architecture",
            "Historical Art",
            "Museums",
            "Anthropology"
            )
        ),
        Section(
            id = "i",
            letter = "I",
            title = "Internet & Media",
            blurb = "Publish, record, edit and share.",
            art = "art/i.png",
            topics = listOf(
            "Internet Basics",
            "Website Management",
            "Blogging",
            "Content Creation",
            "Social Media Literacy",
            "Video Editing",
            "Audio Editing",
            "Podcasting",
            "Streaming Technology",
            "Search Engines",
            "Digital Publishing",
            "Online Communication"
            )
        ),
        Section(
            id = "j",
            letter = "J",
            title = "Journalism & Communication",
            blurb = "Find the story and tell it clearly.",
            art = "art/j.png",
            topics = listOf(
            "Journalism",
            "News Writing",
            "Reporting",
            "Editing",
            "Public Speaking",
            "Debate",
            "Storytelling",
            "Creative Writing",
            "Technical Writing",
            "Communication Skills",
            "Media Literacy",
            "Interviewing"
            )
        ),
        Section(
            id = "k",
            letter = "K",
            title = "Knowledge & Research",
            blurb = "Think straight, research properly.",
            art = "art/k.png",
            topics = listOf(
            "Research Methods",
            "Critical Thinking",
            "Logic",
            "Philosophy",
            "Information Literacy",
            "Academic Writing",
            "Scientific Method",
            "Data Interpretation",
            "Problem Solving",
            "Knowledge Management"
            )
        ),
        Section(
            id = "l",
            letter = "L",
            title = "Languages & Linguistics",
            blurb = "Speak, read and understand more languages.",
            art = "art/l.png",
            topics = listOf(
            "English",
            "Hindi",
            "Sanskrit",
            "Tamil",
            "Telugu",
            "Bengali",
            "Marathi",
            "Gujarati",
            "Punjabi",
            "Urdu",
            "Arabic",
            "Chinese",
            "Japanese",
            "Korean",
            "Spanish",
            "French",
            "German",
            "Italian",
            "Portuguese",
            "Russian",
            "Sign Languages",
            "Grammar",
            "Translation",
            "Pronunciation",
            "Linguistics"
            )
        ),
        Section(
            id = "m",
            letter = "M",
            title = "Mathematics",
            blurb = "Numbers, shapes and proof.",
            art = "art/m.png",
            topics = listOf(
            "Arithmetic",
            "Algebra",
            "Geometry",
            "Trigonometry",
            "Calculus",
            "Statistics",
            "Probability",
            "Number Theory",
            "Linear Algebra",
            "Discrete Mathematics",
            "Mathematical Logic",
            "Applied Mathematics",
            "Financial Mathematics"
            )
        ),
        Section(
            id = "n",
            letter = "N",
            title = "Nature & Environment",
            blurb = "Living systems and how to keep them alive.",
            art = "art/n.png",
            topics = listOf(
            "Botany",
            "Zoology",
            "Ecology",
            "Biodiversity",
            "Wildlife Education",
            "Marine Biology",
            "Forestry",
            "Gardening",
            "Agriculture",
            "Soil Science",
            "Conservation",
            "Climate Science",
            "Sustainable Living"
            )
        ),
        Section(
            id = "o",
            letter = "O",
            title = "Office & Productivity",
            blurb = "Get work done with less friction.",
            art = "art/o.png",
            topics = listOf(
            "Microsoft Office",
            "Word Processing",
            "Spreadsheets",
            "Presentations",
            "Email Skills",
            "Document Management",
            "Note Taking",
            "Time Management",
            "Organisation",
            "Workflow Automation",
            "Collaboration Tools"
            )
        ),
        Section(
            id = "p",
            letter = "P",
            title = "Physics & Science",
            blurb = "How matter, energy and life actually work.",
            art = "art/p.png",
            topics = listOf(
            "Classical Physics",
            "Quantum Physics",
            "Thermodynamics",
            "Optics",
            "Electricity",
            "Magnetism",
            "Chemistry",
            "Organic Chemistry",
            "Inorganic Chemistry",
            "Biology",
            "Genetics",
            "Microbiology",
            "Biochemistry",
            "Neuroscience",
            "Astronomy",
            "Astrophysics"
            )
        ),
        Section(
            id = "q",
            letter = "Q",
            title = "Quality & Professional Skills",
            blurb = "Do it right, and prove it.",
            art = "art/q.png",
            topics = listOf(
            "Quality Management",
            "Quality Assurance",
            "Quality Control",
            "Six Sigma Concepts",
            "Process Improvement",
            "Professional Ethics",
            "Workplace Safety",
            "Documentation",
            "Standards and Compliance"
            )
        ),
        Section(
            id = "r",
            letter = "R",
            title = "Robotics & Research",
            blurb = "Build machines and run experiments.",
            art = "art/r.png",
            topics = listOf(
            "Robotics",
            "Arduino",
            "Microcontrollers",
            "Sensors",
            "Embedded Systems",
            "Automation",
            "Research Projects",
            "Scientific Experiments",
            "Prototyping",
            "Product Development",
            "3D Printing",
            "Engineering Design"
            )
        ),
        Section(
            id = "s",
            letter = "S",
            title = "Skills & Self Development",
            blurb = "Learn faster and work better with people.",
            art = "art/s.png",
            topics = listOf(
            "Study Skills",
            "Memory Techniques",
            "Learning Methods",
            "Critical Thinking",
            "Creativity",
            "Decision Making",
            "Communication",
            "Teamwork",
            "Emotional Intelligence",
            "Goal Setting",
            "Leadership",
            "Career Development"
            )
        ),
        Section(
            id = "t",
            letter = "T",
            title = "Transport & Technology",
            blurb = "Vehicles, flight, rail and how they move.",
            art = "art/t.png",
            topics = listOf(
            "Automobile Technology",
            "Vehicle Mechanics",
            "Automotive Electronics",
            "Electric Vehicles",
            "Aviation",
            "Aerospace",
            "Railway Technology",
            "Maritime Technology",
            "Navigation",
            "Transportation Systems",
            "Traffic Safety Education"
            )
        ),
        Section(
            id = "u",
            letter = "U",
            title = "Utilities & Everyday Life",
            blurb = "The practical stuff nobody teaches you.",
            art = "art/u.png",
            topics = listOf(
            "Digital Payments",
            "Personal Finance Education",
            "Banking Basics",
            "Consumer Awareness",
            "Household Management",
            "Basic Repairs",
            "Sewing",
            "Cleaning",
            "Organisation",
            "Public Services",
            "Emergency Preparedness",
            "First Aid Education"
            )
        ),
        Section(
            id = "v",
            letter = "V",
            title = "Visual & Performing Arts",
            blurb = "Music, stage, film and sound.",
            art = "art/v.png",
            topics = listOf(
            "Music Theory",
            "Singing",
            "Instrument Education",
            "Piano",
            "Guitar",
            "Drums",
            "Music Production",
            "Sound Design",
            "Dance",
            "Theatre",
            "Acting",
            "Film Making",
            "Cinematography",
            "Video Production",
            "Voice Training"
            )
        ),
        Section(
            id = "w",
            letter = "W",
            title = "World Cultures & Society",
            blurb = "How people live together.",
            art = "art/w.png",
            topics = listOf(
            "World Cultures",
            "Anthropology",
            "Sociology",
            "Civics",
            "Political Science",
            "Economics",
            "International Relations",
            "Human Geography",
            "Cultural Studies",
            "Social History",
            "Public Administration"
            )
        ),
        Section(
            id = "x",
            letter = "X",
            title = "Exploration & Advanced Science",
            blurb = "The frontier, and what comes next.",
            art = "art/x.png",
            topics = listOf(
            "Space Exploration",
            "Planetary Science",
            "Exoplanets",
            "Astrobiology",
            "Advanced Materials",
            "Nanotechnology",
            "Biotechnology",
            "Genetic Engineering Concepts",
            "Quantum Computing",
            "Future Technologies"
            )
        ),
        Section(
            id = "y",
            letter = "Y",
            title = "Yoga, Wellness & Sports",
            blurb = "Body, breath and games.",
            art = "art/y.png",
            topics = listOf(
            "Yoga",
            "Meditation",
            "General Fitness Education",
            "Sports Science",
            "Swimming Education",
            "Athletics",
            "Football",
            "Cricket",
            "Basketball",
            "Tennis",
            "Badminton",
            "Chess",
            "Sports Rules",
            "Healthy Habits",
            "Sleep Education"
            )
        ),
        Section(
            id = "z",
            letter = "Z",
            title = "Zero to Advanced Practical Learning",
            blurb = "Start from nothing, build something real.",
            art = "art/z.png",
            topics = listOf(
            "Zero-to-Expert Learning Paths",
            "DIY Projects",
            "Practical Experiments",
            "Hands-on Skills",
            "Maker Education",
            "Open Source Projects",
            "Innovation",
            "Invention",
            "Prototyping",
            "Interdisciplinary Learning",
            "Real-World Problem Solving"
            )
        ),
        Section(
            id = "star",
            letter = "★",
            title = "Additional Global Learning Areas",
            blurb = "Everything else a complete catalogue needs.",
            art = "art/star.png",
            topics = listOf(
            "Law and Legal Literacy",
            "Ethics",
            "Education",
            "Medicine (general educational knowledge)",
            "Public Health Education",
            "Psychology",
            "Veterinary Science",
            "Library Science",
            "Archaeology",
            "Astronomy",
            "Urban Planning",
            "Real Estate Education",
            "Hospitality",
            "Tourism",
            "Logistics",
            "Supply Chain",
            "Sports Management",
            "Game Design",
            "Game Theory",
            "Fashion Technology",
            "Textile Science",
            "Disaster Preparedness",
            "Sustainable Development",
            "Accessibility",
            "Special Education",
            "Sign Language",
            "Human Rights",
            "Consumer Rights",
            "Financial Literacy",
            "Space Technology",
            "Marine Technology",
            "Water Management",
            "Energy Systems",
            "Agricultural Technology",
            "Food Technology",
            "Construction Technology",
            "Safety Education"
            )
        )
    )

    val totalTopics: Int get() = sections.sumOf { it.topics.size }

    fun byId(id: String): Section? = sections.firstOrNull { it.id == id }

    fun allTopics(): List<Pair<Section, String>> =
        sections.flatMap { section -> section.topics.map { section to it } }

    fun search(query: String): List<Pair<Section, String>> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return allTopics().filter { (section, topic) ->
            topic.lowercase().contains(q) || section.title.lowercase().contains(q)
        }
    }

    fun sectionsMatching(query: String): List<Section> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return sections
        return sections.filter { section ->
            section.title.lowercase().contains(q) ||
                section.topics.any { it.lowercase().contains(q) }
        }
    }
}
