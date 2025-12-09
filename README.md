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

## Building

### Prerequisites
- JDK 17+
- Android SDK (for Android build)

### Build Android APK
```bash
./gradlew android:assembleDebug
```

The APK will be at `android/build/outputs/apk/debug/android-debug.apk`

### GitHub Actions
This repo includes a workflow that automatically builds the Android APK on push. Check the Actions tab for artifacts.

## Credits

- Original game: [Frogue by necrashter](https://github.com/necrashter/frogue.git)
- License: MIT

## License

MIT License - See [LICENSE](LICENSE) for details.
