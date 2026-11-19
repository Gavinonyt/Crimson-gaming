# Crimson Launcher for Android

This is a lightweight Android WebView app that packages the current Crimson Gaming website inside an installable Android application. The page is bundled in the APK, while online account, friend, and other Supabase-backed features require an internet connection.

## Build

The GitHub Actions workflow at `.github/workflows/build-android-apk.yml` copies the repository's latest `index.html` into the app assets and builds a debug APK. Open the repository's **Actions** tab, choose **Build Crimson Launcher APK**, run the workflow, then download the `crimson-launcher-debug-apk` artifact.

The debug APK is for testing and direct installation. A signed release APK/AAB for Google Play requires a release signing key and a separate release configuration.

## Themes

The launcher includes Crimson, PS5-inspired blue, and Xbox-inspired green themes. These are original visual themes and are not official Sony or Microsoft products.
