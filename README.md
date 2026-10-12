# FARIS Android Starter

A starter scaffold for the FARIS Android assistant concept. This is **not a complete or tested APK**.

## Included in this starter
- Kotlin + Jetpack Compose app shell
- Cyan holographic-style orb screen
- Explicit UI states: idle, listening, thinking, speaking
- Expand/collapse hologram UI interaction
- Permission and security implementation notes

## Important
- Wake-word listening, speaker verification, anti-spoofing, online AI, web search, encrypted voice enrollment, lock-screen operation, and floating overlays are not implemented in this starter.
- These need separate implementation and device testing.
- Do not use voice alone to authorize sensitive actions.
- Do not place provider API keys in the Android app. Use a secure backend or a provider-supported secure approach.
- The supplied audio is a short movie/Shorts clip and should be treated only as a style reference, not as the owner's enrollment sample.

## Build
Open this folder in Android Studio with a recent Android Gradle Plugin / Kotlin / Compose setup. The project files here are a starter source scaffold and may need Gradle/plugin versions added by Android Studio to match your installed toolchain.

## Build an APK on GitHub from an Android phone
1. Upload all files in this project to a GitHub repository, keeping the `.github/workflows/android-build.yml` file.
2. In GitHub, open the repository's **Actions** tab and enable Actions if prompted.
3. Push/upload the files to `main` or `master`, or choose **Build FARIS APK** → **Run workflow**.
4. Open the completed workflow run, scroll to **Artifacts**, and download `FARIS-debug-APK`.
5. Extract the downloaded artifact ZIP to find `app-debug.apk`.

This workflow builds the current UI starter only. It does not add AI, microphone, wake word, owner voice verification, overlay, or production security.
