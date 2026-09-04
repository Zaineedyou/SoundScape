# SoundScape

Client-only Fabric audio spatializer for Minecraft **26.1.2**, designed for desktop and Android launchers such as Zalith Launcher 2.

## Features

SoundScape uses Minecraft's existing OpenAL/LWJGL runtime, so it does not ship a second native audio library. It detects HRTF and EFX support at runtime, applies a lightweight 3D distance model when available, and falls back safely to vanilla-compatible stereo behavior on Android drivers that lack extensions. Settings are available in-game with the **O** key.

The configuration file is `config/soundscape.properties`. All feature state changes, capability detection, fallback decisions, configuration loads/saves, and periodic health heartbeats are written with the `[SoundScape]` prefix to `logs/latest.log`.

## Build

Minecraft 26.1.2 requires Java 25. The project uses the official Fabric 26.1.2 template, unobfuscated Loom, and Fabric API 0.155.2+26.1.2. Run `./gradlew build`; the release artifact is produced by the normal `jar` task.

## License

See `LICENSE`. The license permits use and sharing of unmodified original builds, but does not permit modification, forks, or derivative builds.
