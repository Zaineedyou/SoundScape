package com.zaineedyou.soundscape.audio;

import com.zaineedyou.soundscape.SoundScape;
import com.zaineedyou.soundscape.config.SoundScapeConfig;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;

public final class AudioRuntime {
    private final SoundScapeConfig config;
    private boolean openAl10;
    private boolean efx;
    private boolean hrtf;
    private boolean capabilitiesReady;
    private long ticks;

    public AudioRuntime(SoundScapeConfig config) { this.config = config; }

    public void initialize() {
        try {
            var caps = AL.getCapabilities();
            openAl10 = caps.OpenAL10;
            efx = caps.ALC_EXT_EFX;
            long context = ALC10.alcGetCurrentContext();
            long device = context == 0L ? 0L : ALC10.alcGetContextsDevice(context);
            hrtf = config.hrtf && device != 0L &&
                    (ALC10.alcIsExtensionPresent(device, "ALC_SOFT_HRTF") ||
                     ALC10.alcIsExtensionPresent(device, "ALC_SOFTX_HRTF"));
            if (openAl10 && config.spatial3d) {
                AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
                AL10.alDopplerFactor(0.0f);
            }
            SoundScape.LOGGER.info("[SoundScape][Audio] OpenAL10={} EFX={} HRTF-extension={} backend={}",
                    openAl10, efx, hrtf, safeBackend());
            capabilitiesReady = true;
            if (config.hrtf && !hrtf) SoundScape.LOGGER.warn("[SoundScape][Audio] HRTF unavailable; using 3D stereo fallback");
            if (config.occlusion && !efx) SoundScape.LOGGER.warn("[SoundScape][Audio] EFX unavailable; occlusion uses lightweight fallback");
        } catch (Throwable t) {
            if (!capabilitiesReady)
                SoundScape.LOGGER.debug("[SoundScape][Audio] OpenAL context not ready yet; will retry on client tick", t);
            else
                SoundScape.LOGGER.warn("[SoundScape][Audio] OpenAL capability detection failed; vanilla-safe fallback active", t);
        }
    }

    public void tick() {
        ticks++;
        if (!capabilitiesReady && ticks % 20 == 0) initialize();
        if (config.debugLogging && ticks % 600 == 0)
            SoundScape.LOGGER.info("[SoundScape][Audio] heartbeat tick={} mode={} hrtf={} efx={} quality={}", ticks, mode(), hrtf, efx, config.quality);
    }

    public String mode() { return hrtf ? "HRTF+3D" : (openAl10 ? "3D-STEREO" : "VANILLA-FALLBACK"); }
    private String safeBackend() {
        try { return AL.getCapabilities().OpenAL10 ? "OpenALC" : "unknown"; }
        catch (Throwable ignored) { return "Android/driver-dependent"; }
    }
}
