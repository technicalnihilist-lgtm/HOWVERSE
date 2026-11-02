# HOWVERSE — web edition

A single-file web version of the HOWVERSE Android app: 392 topics across 27 sections, 47 teaching
languages, 12 learning modes, the offline lesson library, and live content pulled from Wikimedia
Commons at runtime. No build step, no server.

- **Live content.** Opening a section asks the Wikimedia Commons API for current free-licensed
  photos of that topic, so new uploads appear without a new build. "Latest videos" opens a YouTube
  search sorted by upload date.
- **Offline by default.** Every section still has an embedded photo and the built-in guide library,
  so the app works with no network at all. Profile -> Live content toggles this.
- **AI backend (optional).** Profile -> AI backend; point it at the Express server in `backend/`
  for live model answers, otherwise the offline library answers and labels itself as offline.
- **Photo credits.** All section photographs come from Wikimedia Commons (CC0, public domain,
  CC BY, CC BY-SA). Author, licence link and source page appear under each photo and in full under
  Profile -> Image credits.

Open `index.html` directly in a browser, or serve this folder. To publish it, point GitHub Pages at
`main` / `/web` in Settings -> Pages.
