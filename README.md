# 🎬 OscarTuber — Android Video Streaming & Creator Platform

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-red.svg)](https://github.com/OscarTuber/oscartuber-android)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-green.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**OscarTuber** is a YouTube-inspired video streaming and creator ecosystem built with modern Android (Kotlin + Jetpack Compose + Material 3), designed for both online discovery and completely offline entertainment.

---

## 🚀 Features

- 💾 **Offline Video Watch:** Save videos directly to your device storage to enjoy movies, tutorials, and music completely offline without using mobile data. Includes an **Offline Simulator Toggle** to test off-grid playback.
- 🔔 **WEPER Subscriptions:** Replace traditional subscriptions with the signature **WEPER** system ("WEPE" → "WEPED ✓") and track creator channel Wepers in real-time.
- 🎬 **Oscar Studio (Video Editor):** Built-in video editing suite featuring:
  - Timeline trimming slider
  - 6 Cinematic LUTs / Color grading filters (*Cinema Warm, Oscar Gold, Noir Mono, Cyber Neon, Vintage Sepia, Normal*)
  - Audio volume & mute controls
  - Playback speed multiplier (0.5x, 1x, 1.5x, 2x)
  - Custom watermark overlay
- 📊 **Creator Studio & Analytics:** Dedicated creator dashboard with total views, wepers count, watch hours, and an interactive 7-day performance growth chart.
- 📱 **Vertical Oscar Shorts:** Fullscreen vertical reels with swipe navigation, likes, comments, vinyl disc animation, and "Save Short" functionality.
- 🔍 **Instant Search & Category Discovery:** Search videos with instant filtering, voice search simulation, trending tags, and category chips (*Cinema, Tech, Gaming, Music, Coding, Originals, Shorts*).
- ☁️ **Cross-Device Cloud Sync & History:** Real-time synchronization of watch history, watch later playlists, and creator uploads across user sessions.
- 🔔 **Push Notifications Center:** Engagement alerts for new wepers, video uploads, offline download completion, and comments.

---

## 📥 How to Download the App & Code

### Option 1: Direct Download from Google AI Studio
1. Open the project in **Google AI Studio Build**.
2. Click the **Settings / Export** menu in the top-right corner.
3. Select **"Push to GitHub"** to export this entire repository to your personal GitHub account.
4. Or select **"Export as ZIP"** / **"Generate APK"** to download the project or APK directly to your computer or Android phone.

### Option 2: Clone from GitHub & Build with Android Studio
```bash
# Clone the repository
git clone https://github.com/OscarTuber/oscartuber-android.git
cd oscartuber-android

# Build the debug APK
gradle :app:assembleDebug
```
The output APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose with Material Design 3 (M3)
- **Local Persistence:** Room Database with Kotlin Coroutines & Flow
- **Image & Thumbnail Loading:** Coil Compose
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Min SDK:** 24 (Android 7.0+)
- **Target SDK:** 36 (Android 15+)

---

## 📄 License
This project is open-source under the [MIT License](LICENSE). Contributions, pull requests, and feedback are welcome!
