# Ultra Drive Connect

Standalone Android application source for the Ultra Drive Connect companion app.

## Build

Open this folder as an Android project in Android Studio, or run:

```bash
gradle :app:assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

This app currently provides companion UI and official setup links. It does not implement Android Auto projection, an Apple CarPlay receiver, or vehicle key commands. Those capabilities require supported platform APIs, compatible hardware/vehicles, and authorized integrations.

Build retry: triggered by the repository's GitHub Actions workflow on changes under this folder.
