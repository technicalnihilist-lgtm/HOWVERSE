# patches

`live-section.patch` adds the live Wikimedia Commons section screen and the Profile image-credits
card to `app/src/main/java/com/howverse/app/ui/HowverseApp.kt`. Everything else it needs
(`LiveMedia.kt`, `ImageCredits.kt`, the `VideoActivity` sort flag) is already on `main`.

Apply it from the repository root:

```bash
git apply patches/live-section.patch
./gradlew assembleDebug
```

If your working tree already has the file changed, check first:

```bash
git apply --check patches/live-section.patch
```

The patch was generated against commit `d08c1f2` and the file has not changed since, so it applies
cleanly on a fresh checkout of `main`.
