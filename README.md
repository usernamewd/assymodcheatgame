# Frogue Mod - Cheat Menu Edition

A modded version of [Frogue](https://github.com/necrashter/frogue.git), an open-source FPS game built with libGDX.

## Features

### Mod Menu
A floating, draggable mod menu button appears when the game starts. Tap it to toggle cheats:

| Cheat | Effect |
|-------|--------|
| **Bunnyhop** | Jump anytime (even mid-air), 1.3x jump velocity boost |
| **AirStrafe** | Full air control, build speed while airborne |
| **Third Person** | Camera moves behind player for 3rd person view |
| **Rapid Fire** | 5x faster fire rate and reload for all weapons |
| **Infinite Ammo** | Never run out of ammo in current clip |
| **One Hit Kill** | All enemies die in one shot |

## Setup Instructions

This repository contains the mod files. To build the complete game, you need to merge with the original repository:

### Option 1: Quick Setup (Recommended)
```bash
# Clone the original game
git clone https://github.com/necrashter/frogue.git frogue-mod
cd frogue-mod

# Add this repo as a remote and merge
git remote add mod https://github.com/usernamewd/assymodcheatgame.git
git fetch mod
git merge mod/main --allow-unrelated-histories -m "Merge mod files"

# Build the Android APK
./gradlew android:assembleDebug
```

### Option 2: Manual Setup
1. Clone the original: `git clone https://github.com/necrashter/frogue.git`
2. Copy these mod files into the cloned repo:
   - `core/src/main/java/io/github/necrashter/natural_revenge/mod/` (new folder)
   - Replace `Player.java`, `Firearm.java`, `GameScreen.java`
   - Add `.github/workflows/build-android.yml`
3. Build: `./gradlew android:assembleDebug`

## Building

### Prerequisites
- JDK 17+
- Android SDK (for Android build)

### Build Commands
```bash
# Debug APK
./gradlew android:assembleDebug

# Release APK  
./gradlew android:assembleRelease
```

The APK will be at `android/build/outputs/apk/debug/android-debug.apk`

### GitHub Actions
This repo includes a workflow that automatically builds the Android APK on push. Check the Actions tab for artifacts.

## Modified Files

| File | Changes |
|------|---------|
| `mod/ModConfig.java` | NEW - Singleton holding cheat toggle states |
| `mod/ModMenuUI.java` | NEW - Floating draggable UI with checkboxes |
| `Player.java` | Added bunnyhop, airstrafe, third person camera |
| `Firearm.java` | Added rapid fire, infinite ammo, one-hit kill |
| `GameScreen.java` | Integrated ModMenuUI |

## Credits

- Original game: [Frogue by necrashter](https://github.com/necrashter/frogue.git)
- Mod by: Matrix Agent
- License: MIT

## License

MIT License - See [LICENSE](LICENSE) for details.
