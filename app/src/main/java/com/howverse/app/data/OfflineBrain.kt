package com.howverse.app.data

/**
 * The app works out of the box: when no AI backend URL is configured (or the
 * backend cannot be reached) HOWVERSE answers from this built-in library of
 * hand-written guides, plus a general-purpose lesson framework for anything
 * else. It is not a language model — the UI labels these answers as offline.
 */
object OfflineBrain {

    private data class Entry(val keys: List<String>, val body: String)

    private val entries = listOf(
        Entry(
            listOf("smartphone", "phone work", "mobile work", "how does a phone"),
            """
A smartphone is a small computer with four parts working together.

1. Processor (SoC) — one chip that runs apps, reads touch input and manages power. Most phones use ARM-based chips built on a 3–4 nm process.
2. Memory and storage — RAM holds what you are using right now; flash storage keeps photos and apps when the phone is off.
3. Display and touch — a capacitive panel senses the tiny charge from your finger and sends coordinates to the processor, which redraws the screen 60–120 times a second.
4. Radios and sensors — cellular, Wi-Fi and Bluetooth send and receive data, while the battery, camera and motion sensors keep the phone aware of the world.

Why it feels fast: the operating system keeps apps in memory, freezes the ones you are not using, and wakes them instantly when you tap the icon.
            """.trimIndent()
        ),
        Entry(
            listOf("biryani", "cook", "recipe", "khana"),
            """
Vegetable biryani, the simple way (serves 4).

1. Soak 2 cups basmati rice for 30 minutes, then drain.
2. Heat 3 tbsp ghee, add 1 sliced onion and fry until deep golden. Keep half aside for layering.
3. Add 1 tbsp ginger-garlic paste, 2 chopped tomatoes, 1 cup mixed vegetables, 1/2 cup curd, 1 tsp red chilli, 1/2 tsp turmeric, 1 tsp garam masala and salt. Cook 8 minutes.
4. Boil water with whole spices, add the rice and cook until 70% done — it should still have a bite. Drain.
5. Layer rice over the vegetables, scatter mint, fried onion and 2 tbsp warm milk with saffron. Seal with a tight lid.
6. Cook on the lowest flame for 20 minutes (place a tawa under the pot so the base does not burn). Rest 10 minutes, then open and mix gently.

Key mistakes: overcooking the rice before layering, and a flame that is too high during dum.
            """.trimIndent()
        ),
        Entry(
            listOf("publish", "android app", "play store", "playstore"),
            """
Publishing an Android app, step by step.

1. Build it properly — minSdk, targetSdk and a real versionCode/versionName in build.gradle.kts.
2. Create a release keystore: keytool -genkey -v -keystore howverse.jks -keyalg RSA -keysize 2048 -validity 10000 -alias howverse. Never lose this file; you cannot update the app without it.
3. Add a signingConfig in Gradle and run ./gradlew bundleRelease to get an .aab.
4. Test on a real device with the release build, not just debug.
5. Create a Google Play developer account (one-time $25), fill the store listing: title, short description, screenshots (phone + 7" tablet), feature graphic 1024x500, app icon 512x512.
6. Complete the Data safety form, content rating questionnaire and privacy policy URL — required if you collect any data.
7. Upload the .aab to internal testing first, then promote to production. Review usually takes a few days for new accounts.

Also needed: a support email, and honest screenshots of the real app.
            """.trimIndent()
        ),
        Entry(
            listOf("artificial intelligence", "machine learning", " ai ", "what is ai", "neural"),
            """
Artificial intelligence, without the hype.

An AI system is a program that learns patterns from examples instead of following hand-written rules.

1. Data — millions of examples (text, images, numbers). Quality and size decide how good the result can be.
2. Model — a very large set of numbers (weights) arranged in layers. A neural network is just stacked multiplication and addition.
3. Training — show the model an example, compare its output with the correct answer, and nudge every weight slightly in the direction that reduces the error. Repeat billions of times.
4. Inference — after training, a new input passes through the same maths in milliseconds and produces a prediction.

Large language models do one thing extremely well: predict the next token from everything before it. Fluency comes from that single objective repeated at enormous scale.

Limits: no memory of its own, no guaranteed truth, and it inherits every bias in its training data. Always verify important facts.
            """.trimIndent()
        ),
        Entry(
            listOf("electric", "wiring", "inverter", "ups", "circuit", "voltage"),
            """
Basic home electrical safety and checks.

Safety first: switch off the main breaker and test the wire with a tester before touching anything. If the work is inside a wall, in a bathroom, or anywhere near the meter, use a licensed electrician — this is not a DIY area.

What you can safely check:
1. Tripping MCB — unplug appliances on that circuit one by one to find the faulty one. A trip while nothing is plugged in means a wiring fault; call an electrician.
2. Flickering lights — usually a loose neutral at the fixture or an overloaded circuit, not a bad bulb.
3. Inverter backup time dropping — check battery water level (lead-acid), terminal corrosion and the charging voltage. Batteries lose capacity after 3–4 years.
4. Earthing — if you feel a mild tingle on a metal appliance body, stop using it and get the earth checked immediately.

Load maths: watts = volts x amps. A 16A socket on 230V safely carries about 3500W; keep a single heavy appliance per socket.
            """.trimIndent()
        ),
        Entry(
            listOf("car", "bike", "scooter", "service", "engine", "tyre"),
            """
Keeping a vehicle reliable.

1. Tyres — check pressure every two weeks when cold. Under-inflation is the most common cause of poor mileage and early tyre wear.
2. Engine oil — check the dipstick weekly on a level surface. Dark is normal; milky or gritty is not. Change at the interval in the manual, sooner in heavy traffic.
3. Coolant and brake fluid — both should sit between MIN and MAX. Never open a hot radiator cap.
4. Brakes — a soft pedal, grinding sound or a car that pulls to one side means inspect the pads immediately.
5. Battery — a battery older than 3 years that cranks slowly in the morning is close to failing; get it load-tested.
6. Sounds — a squeal on start-up is usually a belt; a knock under acceleration needs a mechanic now, not later.

Service records matter: a stamped book protects resale value and catches small problems before they become expensive.
            """.trimIndent()
        ),
        Entry(
            listOf("money", "budget", "save", "sip", "invest", "mutual fund"),
            """
A simple, boring money system that works.

1. Track for 30 days — write down every rupee you spend. You cannot fix what you cannot see.
2. Split income the day it arrives: 50% needs, 30% wants, 20% savings and investing. Adjust the split to your reality, but automate the 20% first.
3. Build an emergency fund of 3–6 months of expenses in a separate savings account or liquid fund. This is not an investment; it is insurance against debt.
4. Clear high-interest debt (credit cards, personal loans) before investing — no investment reliably beats 36% interest.
5. Invest monthly, not occasionally. For long horizons (7+ years), a broad index fund through a monthly SIP is the simplest sensible choice; keep equity and debt in a mix you can hold through a 30% fall.
6. Insure before you invest: term life insurance (pure protection, not ULIP) and a separate health cover.

This is general education, not personal financial advice — for decisions about your own money, talk to a SEBI-registered adviser.
            """.trimIndent()
        ),
        Entry(
            listOf("business", "startup", "customer", "sell", "shop"),
            """
Starting a small business without wasting money.

1. Pick one customer and one problem. "Everyone" is not a customer.
2. Talk to ten real people who have that problem before you build anything. Ask what they do today and what it costs them.
3. Sell it before you make it — a pre-order, a WhatsApp list, a single paying client. Revenue is the only real validation.
4. Start with the cheapest version that solves the core problem. Skip the logo, the app and the office.
5. Price for margin: know your cost per unit, add your time, then add profit. Under-pricing is the fastest way to work hard and stay broke.
6. Get the boring basics right from day one — GST registration if turnover requires it, a separate bank account, invoices, and a simple book of expenses.
7. Reinvest early profit into whatever brings customers, not into things that look impressive.
            """.trimIndent()
        ),
        Entry(
            listOf("plumbing", "tap", "leak", "pipe", "drain", "flush"),
            """
Fixing the common plumbing problems.

1. Dripping tap — close the valve under the sink, unscrew the handle, replace the washer or ceramic cartridge. Five-minute job, cheap part.
2. Leaking joint — dry the pipe fully, wrap PTFE tape clockwise 3–4 turns on the thread, re-tighten by hand plus a quarter turn. Do not overtighten plastic.
3. Slow drain — remove the trap and clean it by hand first. Chemical cleaners damage old pipes and rarely reach the real blockage.
4. Running cistern — the inlet valve or the flush seal is worn; replace the seal before replacing the whole cistern.
5. Low water pressure — check the aerator on the tap and the strainer on the inlet before blaming the supply.

Always know where your main shut-off valve is before you start, and never seal a joint on a pipe that is under pressure.
            """.trimIndent()
        ),
        Entry(
            listOf("paint", "wall", "repair home", "crack", "drill"),
            """
Painting and repairing a wall properly.

1. Surface first — scrape loose paint, fill cracks with wall putty, sand smooth, and dust off. Paint over a bad surface always looks bad.
2. Prime bare plaster or patched areas; it seals the surface and saves topcoat.
3. Two thin coats beat one thick coat. Let the first dry fully (4–6 hours for interior emulsion).
4. Cut in the edges with a brush before rolling the middle; keep a wet edge so overlaps do not show.
5. Coverage maths: one litre of emulsion covers roughly 120–140 sq ft per coat. Measure the wall area, not the floor area.
6. Small cracks that keep returning in the same place usually mean movement or moisture, not bad paint — fix the cause.
7. Ventilate for a day and keep children and pets out of the room while it dries.
            """.trimIndent()
        ),
        Entry(
            listOf("coding", "programming", "python", "learn to code", "developer"),
            """
Learning to code without getting lost.

1. Pick one language and stay with it for six months. Python if you are unsure; JavaScript if you want to build websites.
2. Learn these five things properly: variables, conditionals, loops, functions and lists. Everything else is built from them.
3. Build tiny programs that touch real files or real data — a bill splitter, a CSV cleaner, a habit tracker. Tutorials alone do not stick.
4. Read error messages from the bottom up; they usually name the exact line and the exact problem.
5. Use version control (git) from week one. Commit small and often.
6. When stuck, reduce the problem until it is almost trivial, then solve that piece.
7. After 3–4 months, contribute to one small open-source project or ship one small app. Shipping teaches more than another course.
            """.trimIndent()
        ),
        Entry(
            listOf("resume", "interview", "job", "cv"),
            """
Getting interviews and passing them.

Resume:
1. One page, one column, no photo, no fancy graphics — recruiters and ATS software both need plain text.
2. Lead every bullet with a result and a number: "Cut report time from 4 hours to 20 minutes by automating the weekly export".
3. Mirror the words in the job description; the first filter is often a keyword match.
4. Skip objectives and hobbies. Put skills, experience, projects, education in that order.

Interview:
1. Prepare three stories: a problem you solved, a conflict you handled, something you learned from failure. Structure each as situation, action, result.
2. Research the company's actual product and ask one specific question about it.
3. Answer "tell me about yourself" in 90 seconds: what you do now, one proof point, why this role.
4. Always ask about the next step and the timeline before you leave.
            """.trimIndent()
        ),
        Entry(
            listOf("wifi", "internet", "router", "slow network", "5g"),
            """
Fixing a slow or dropping connection.

1. Restart the router — power off 30 seconds. This clears most stalls.
2. Test wired vs wireless. If the wire is fast and Wi-Fi is slow, the problem is the wireless channel, not the line.
3. Use 5 GHz for speed in the same room, 2.4 GHz for range through walls. Never put the router inside a cupboard or behind a TV.
4. Change the Wi-Fi channel to 1, 6 or 11 on 2.4 GHz — neighbours crowd the same channels in flats.
5. Count devices. Video calls plus downloads plus smart TV saturate a 100 Mbps line quickly; use QoS to prioritise calls.
6. Check the actual plan speed with a wired test at off-peak hours before blaming the provider.
7. A repeater halves throughput; a mesh node or a wire to a second access point is better.
            """.trimIndent()
        ),
        Entry(
            listOf("solar", "panel", "electricity bill"),
            """
Rooftop solar, the practical version.

1. Size the system to your bill, not to the roof. A 3 kW system typically generates 12–15 units a day in most of India — roughly 350–450 units a month.
2. Check roof area and shadow: about 100 sq ft per kW of panels, unshaded between 9 am and 4 pm. One shadowed panel drags down its whole string.
3. Net metering changes the maths — apply to your DISCOM before installation and confirm the sanctioned load.
4. Components: panels (mono PERC or TOPCon), inverter (string or micro), mounting structure, DC/AC cabling and earthing. Do not skip surge protection.
5. Ask for the panel datasheet and warranty terms in writing: 25-year performance warranty on panels, 5–10 years on the inverter.
6. Clean the panels every 2–3 weeks in dusty months; soiling can cost 10–15% of generation.
7. Subsidy schemes and rates change — verify the current central/state subsidy on the official portal before signing.
            """.trimIndent()
        )
    )

    private fun generic(question: String, category: String): String {
        val topic = question.trim().ifBlank { category }
        val cat = if (category.isBlank() || category == "Everything") "this subject" else category.lowercase()
        return """
A structured way to learn "$topic".

1. Define it in one sentence. If you cannot, you have found your first task: read a short overview and write the definition in your own words.
2. Break it into 3–5 parts. Almost every skill or system is a small set of pieces that interact — list them before you study any of them in depth.
3. Learn the vocabulary. Note every unfamiliar term you meet and look it up once; most confusion is missing words, not missing intelligence.
4. Find the smallest real practice. Not a course — one small thing you can do today that uses the idea (measure it, draw it, build it, explain it to someone).
5. Study one worked example end to end, then repeat it from memory without looking. The gap between those two attempts is exactly what you still need to learn.
6. Collect your mistakes in a list. Mistakes are the syllabus; repeating a mistake means the lesson has not landed yet.
7. Teach it back. Explaining "$topic" in five sentences to a friend exposes every fuzzy part.
8. Set a review point — revisit in three days and again in two weeks.

Related areas worth exploring next: $cat fundamentals, the tools people in this field actually use, and one real case study you can take apart.
        """.trimIndent()
    }

    fun lesson(question: String, category: String): Lesson {
        val q = " " + question.lowercase() + " "
        val hit = entries.firstOrNull { entry -> entry.keys.any { q.contains(it) } }
        val body = hit?.body ?: generic(question, category)
        return Lesson(
            answer = body,
            sources = emptyList(),
            fromAI = false,
            notice = "Offline guide from the built-in HOWVERSE library. Add your AI backend URL in Profile for live AI answers."
        )
    }
}
