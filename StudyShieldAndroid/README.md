# StudyShield Android

Third-party Android app that blocks reels on TikTok & Instagram using an
Accessibility Service + system overlay.

## Open & Run

1. Open `StudyShieldAndroid/` in **Android Studio** (Hedgehog or newer).
2. Let Gradle sync, then Run on a device/emulator (API 26+).
3. In the app:
   - Tap **Enable Accessibility Service** → find "StudyShield" → toggle it on.
   - Tap **Allow Overlay Permission** → enable "Display over other apps".
4. Open Instagram/TikTok — reels surfaces now trigger a "Focus Mode" overlay,
   while DMs (`direct`/`inbox`/`thread` view IDs) and static feeds still work.

## Files

- `MainActivity.kt` — toggles + blocked counter dashboard
- `StudyShieldAccessibilityService.kt` — detects TikTok/Instagram foreground and
  scans the view hierarchy for reel/clip IDs vs. DM inbox IDs
- `BlockerOverlayService.kt` — full-screen glass "Focus Mode" card
- `res/xml/studyshield_accessibility_config.xml` — service config
- `AndroidManifest.xml` — permissions: `BIND_ACCESSIBILITY_SERVICE`,
  `SYSTEM_ALERT_WINDOW`, `FOREGROUND_SERVICE`

Counter persists in SharedPreferences and shows in the dashboard.
