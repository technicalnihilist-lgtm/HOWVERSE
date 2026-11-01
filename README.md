# HOWVERSE

<img src="screenshots/banner.png" alt="HOWVERSE — Learn anything. Understand everything." width="100%">

<img src="screenshots/icon-512.png" alt="HOWVERSE app icon" width="96" align="right">

**HOWVERSE** is a Kotlin + Jetpack Compose learning app with a **complete A-to-Z how-to
catalogue** — 392 topics across 27 sections, 12 learning modes and 47 languages — plus an AI
chat, in-app video browsing, downloadable lessons and light/dark themes.

<p>
<img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white">
<img alt="Compose" src="https://img.shields.io/badge/Jetpack%20Compose-BOM%202024.12-4285F4?logo=jetpackcompose&logoColor=white">
<img alt="Topics" src="https://img.shields.io/badge/topics-392-9DBBFF">
<img alt="Languages" src="https://img.shields.io/badge/languages-47-9DBBFF">
<img alt="Release" src="https://img.shields.io/badge/release-v2.0.0-9DBBFF">
</p>

---

## Download the APK

**[⬇ Download HOWVERSE 2.0 (debug APK)](https://github.com/technicalnihilist-lgtm/HOWVERSE/releases/latest)**

Direct file: [`HOWVERSE-2.0-debug.apk`](https://github.com/technicalnihilist-lgtm/HOWVERSE/releases/download/v2.0.0/HOWVERSE-2.0-debug.apk) · 11 MB · Android 8.0 (API 26) and above

Debug-signed — enable "Install unknown apps" to install. It updates over 1.0/1.1 (same
package, same key). Not Play Store ready; see [Building from source](#building-from-source).

---

## What's new in 2.0

- **Complete A–Z catalogue** — every section from the master list is in the app: A Art &
  Design … Z Zero to Advanced Practical Learning, plus a ★ section of 37 additional global
  areas. **392 topics, 27 sections, 12 learning modes.**
- **Section artwork** — 27 illustrated section headers ship inside the APK (generated vector-style
  art, one per section) shown on the home grid, the catalogue list and every section page.
- **In-app video browsing** — every topic has a play button that opens an in-app YouTube
  search for that exact topic (`VideoActivity`, a WebView). Videos play in the YouTube
  player; nothing is bundled or scraped.
- **47 languages** — pick a teaching language; it is sent to the AI backend with every
  question and used for the read-aloud voice (`TextToSpeech` locale). See
  [Languages](#languages--voices) for what is and isn't translated.
- **Downloads** — every AI answer has a download button that writes the lesson as a `.txt`
  into `Downloads/HOWVERSE`, in the language you chose. Sections can be downloaded as a
  topic checklist, and the Downloads screen can fetch **topic packs** (JSON) from a URL you
  configure and keep them on the device.
- **Smaller and faster** — the 20 MB icon library was replaced with the core Material icon
  set: the APK went from 17 MB to **11 MB** and builds in minutes instead of tens of minutes.
- Light/dark themes, saved lessons, read-aloud and the AI chat from 1.1 are all still here.

---

## Screenshots

| Home — A–Z grid (dark) | Catalogue (dark) |
|:---:|:---:|
| <img src="screenshots/01-home-dark.png" width="300"> | <img src="screenshots/02-catalogue-dark.png" width="300"> |
| 392 topics, 27 sections, language and downloads in one tap | Every section with its artwork, topic count and blurb |

| Section page (light) | AI chat (dark) |
|:---:|:---:|
| <img src="screenshots/03-section-light.png" width="300"> | <img src="screenshots/04-ai-chat-dark.png" width="300"> |
| Artwork, learning modes, topic list with ask + video buttons | Answer bubbles with speak, save, download and copy |

| 47 languages (light) | Downloads (dark) |
|:---:|:---:|
| <img src="screenshots/05-languages-light.png" width="300"> | <img src="screenshots/06-downloads-dark.png" width="300"> |
| Teaching language picker with locale tags | Lesson saving and topic pack downloads |

Full-resolution PNGs (1080 × 2400), the 512 × 512 icon, a Play Store feature graphic and a
dark-vs-light comparison are in [`screenshots/`](screenshots).

---

## The catalogue

| | Section | Topics | | Section | Topics |
|---|---|---|---|---|---|
| **A** | Art & Design | 19 | **N** | Nature & Environment | 13 |
| **B** | Business & Management | 17 | **O** | Office & Productivity | 11 |
| **C** | Computer & Coding | 22 | **P** | Physics & Science | 16 |
| **D** | Digital Technology | 15 | **Q** | Quality & Professional Skills | 9 |
| **E** | Engineering | 14 | **R** | Robotics & Research | 12 |
| **F** | Food & Cooking | 14 | **S** | Skills & Self Development | 12 |
| **G** | Geography & Geology | 13 | **T** | Transport & Technology | 11 |
| **H** | History & Heritage | 11 | **U** | Utilities & Everyday Life | 12 |
| **I** | Internet & Media | 12 | **V** | Visual & Performing Arts | 15 |
| **J** | Journalism & Communication | 12 | **W** | World Cultures & Society | 11 |
| **K** | Knowledge & Research | 10 | **X** | Exploration & Advanced Science | 10 |
| **L** | Languages & Linguistics | 25 | **Y** | Yoga, Wellness & Sports | 15 |
| **M** | Mathematics | 13 | **Z** | Zero to Advanced Practical Learning | 11 |
| | | | **★** | Additional Global Learning Areas | 37 |

**Learning modes** (every section can be taught in each): Beginner to Advanced, Step-by-Step
Tutorials, Practical Projects, Interactive Lessons, Examples and Demonstrations, Exercises
and Quizzes, Troubleshooting, Real-World Applications, Learning Roadmaps, Skill Assessment,
Personalized Learning, Multilingual Teaching.

The whole catalogue lives in
[`data/Catalogue.kt`](app/src/main/java/com/howverse/app/data/Catalogue.kt) — add a topic by
adding one string.

---

## Images and videos — what is actually in the app

Being precise, because this matters:

- **Images** — the app ships **27 illustrated section headers** (`app/src/main/assets/art/`),
  one per section, generated in-house. They are category artwork, not photographs, and there
  is no per-topic photo library. A per-topic image would need a licensed photo/CDN per topic
  (~392 assets) — that is a content deal, not code.
- **Videos** — the app does **not** bundle video files (that would be hundreds of MB). Every
  topic's play button opens an **in-app YouTube search** for that topic in `VideoActivity`.
  Results and playback come from YouTube, so nothing is redistributed.
- Everything else (catalogue text, lesson text, downloads) is real and offline-capable.

---

## Languages & voices

47 languages are selectable: English, Hindi, Bengali, Tamil, Telugu, Marathi, Gujarati,
Punjabi, Urdu, Kannada, Malayalam, Odia, Assamese, Sanskrit, Nepali, Sinhala, Arabic,
Persian, Turkish, Hebrew, Russian, Ukrainian, Greek, German, French, Spanish, Portuguese,
Italian, Dutch, Polish, Swedish, Danish, Finnish, Czech, Romanian, Hungarian, Indonesian,
Malay, Vietnamese, Thai, Filipino, Swahili, Afrikaans, Chinese, Japanese, Korean and
Hinglish.

What the choice does:

1. **Lesson language** — sent as `language` with every `/api/learn` request, together with an
   instruction to answer in that language. Your AI backend does the translating; a model that
   knows the language answers in it.
2. **Read-aloud voice** — `TextToSpeech.setLanguage(tag)`. If the device has no voice
   installed for that locale the app says so in Settings and in the chat.
3. **Downloads** — the saved `.txt` records which language the lesson was produced in.

What it does **not** do: translate the app's own interface strings. The UI stays in English,
and the built-in offline library is English-only — both are stated inside the app rather than
hidden.

---

## How the AI works

```
                      ┌───────────────────────────────┐
   your question ───► │  LessonEngine.ask(            │
   + language         │    question, category,        │
                      │    backendUrl, language )     │
                      └───────────┬───────────────────┘
                                  │
             backend URL set?     │     no / unreachable
                    ┌─────────────┴──────────────┐
                    ▼                            ▼
        ┌───────────────────────┐      ┌────────────────────────┐
        │  POST <url>/api/learn │      │  OfflineBrain          │
        │  Express backend      │      │  built-in guide library│
        │  → AI model (+search) │      │  + lesson framework    │
        └───────────────────────┘      └────────────────────────┘
```

Set your backend URL in **Settings → AI backend** — no rebuild. The chat header shows a
**Live AI** chip when a backend is configured and **Offline** when answers come from the
built-in library. Offline answers are always labelled; nothing is passed off as model output.

Honest limits: the offline library is curated text with keyword matching, not a language
model, and it is English only.

---

## Downloads

| What | Where it goes | How |
|---|---|---|
| A single lesson | `Downloads/HOWVERSE/<question>.txt` | Download icon on any AI answer |
| A whole section | `Downloads/HOWVERSE/<section>-topics.txt` | Download button on the section page |
| Topic packs (JSON) | App-private storage, listed in Downloads | Paste a pack URL on the Downloads screen |

Lesson files use the MediaStore Downloads API on Android 10+, so no storage permission is
needed; on Android 8–9 they are written to the app's external downloads folder.

---

## Features

- Complete A–Z catalogue with search across all 392 topics
- Section artwork, learning modes, per-topic ask and video actions
- AI chat with read-aloud, save, download and copy on every answer
- 47-language teaching + voice selection
- Light and dark themes (a full second palette, not an inversion)
- Saved lessons and downloaded files persist on the device
- Adaptive app icon with an Android 13 themed monochrome layer
- Edge-to-edge Compose UI, keyboard-aware input, responsive layout

---

## Project structure

```
HOWVERSE/
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── assets/art/                        # 27 section illustrations (a.png … star.png)
│   ├── java/com/howverse/app/
│   │   ├── MainActivity.kt                # edge-to-edge host
│   │   ├── VideoActivity.kt               # in-app YouTube search per topic
│   │   ├── data/
│   │   │   ├── Catalogue.kt               # 392 topics, 27 sections, 12 learning modes
│   │   │   ├── Languages.kt               # 47 languages with BCP-47 tags
│   │   │   ├── LessonEngine.kt            # backend call + offline fallback
│   │   │   ├── OfflineBrain.kt            # built-in guide library
│   │   │   ├── Downloader.kt              # lesson/section/pack downloads
│   │   │   ├── Models.kt
│   │   │   └── Prefs.kt                   # theme, language, URLs, saved lessons
│   │   └── ui/
│   │       ├── HowverseApp.kt             # all screens
│   │       └── Theme.kt                   # dark + light palettes
│   └── res/                               # icons, theme, network security config
├── backend/                               # Express API that talks to the AI provider
├── screenshots/                           # 1080 × 2400 screenshots, banner, icon, feature graphic
├── gradle/wrapper/                        # Gradle wrapper 8.11.1
├── build.gradle.kts · settings.gradle.kts · gradle.properties
├── gradlew · gradlew.bat
└── README.md
```

---

## Building from source

```bash
cd backend && npm install && cp .env.example .env   # optional: only for live AI
./gradlew assembleDebug                            # app/build/outputs/apk/debug/app-debug.apk
```

Requirements: JDK 17, Android SDK 35 (platform + build-tools 35.0.0), Node 18+ for the
backend.

The bundled network security config allows cleartext HTTP only for `10.0.2.2`, `localhost`
and `127.0.0.1` (emulator development). Everything else must be HTTPS. Keep provider keys on
the server, never in the app or this repo.

---

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose (BOM 2024.12.01), Material 3, core Material icons |
| State | Compose state + coroutines, no ViewModel needed yet |
| Storage | SharedPreferences + MediaStore Downloads + app-private packs |
| Networking | OkHttp 4.12 + `org.json` |
| Speech | Android `TextToSpeech` (per-language locale) |
| Video | `WebView` → YouTube search per topic |
| Backend | Node.js 18+, Express 4, express-rate-limit, dotenv |
| Build | AGP 8.7.3, Gradle 8.11.1, JDK 17, compileSdk/targetSdk 35, minSdk 26 |

---

## Roadmap

- Per-topic images (needs a licensed photo source or an image-generation endpoint)
- Markdown rendering for lesson answers
- In-app video playback with a real video API instead of a YouTube search
- Room/DataStore for larger saved-lesson collections
- Streaming AI responses, backend authentication, crash reporting
- Automated UI and unit tests, Play Store release signing and privacy policy

---

## Safety and accuracy

HOWVERSE is a learning aid, not a substitute for licensed professionals. The AI request asks
the backend to refuse instructions that enable violence, illegal activity, dangerous
experiments, cyber abuse or other harm, and the app limits step-by-step instructions for
hazardous repairs, electrical/gas work, structural work, security bypasses and risky device
modifications. Always verify critical instructions against official manuals and qualified
professionals.

---

## Credits

App name, design and source: **HOWVERSE**. Screenshots were captured at 1080 × 2400 from the
app's Compose UI; the section artwork and the adaptive icon ship inside the APK.
