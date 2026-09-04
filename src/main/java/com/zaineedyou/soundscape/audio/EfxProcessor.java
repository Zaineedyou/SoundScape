package com.zaineedyou.soundscape.audio;

import com.zaineedyou.soundscape.SoundScape;
import com.zaineedyou.soundscape.config.SoundScapeConfig;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public final class EfxProcessor {
    private final SoundScapeConfig config;
    private int lowPass;
    private int reverb;
    private int slot;
    private boolean ready;
    public EfxProcessor(SoundScapeConfig config) { this.config = config; }

    public void initialize() {
        try {
            if (!AL.getCapabilities().ALC_EXT_EFX) return;
            lowPass = EXTEfx.alGenFilters();
            EXTEfx.alFilteri(lowPass, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
            reverb = EXTEfx.alGenEffects();
            EXTEfx.alEffecti(reverb, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_REVERB);
            EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DECAY_TIME, 1.25f);
            EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DENSITY, 0.65f);
            slot = EXTEfx.alGenAuxiliaryEffectSlots();
            EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverb);
            ready = true;
            SoundScape.LOGGER.info("[SoundScape][EFX] Initialized low-pass filter={} reverb={} slot={}", lowPass, reverb, slot);
        } catch (Throwable t) { SoundScape.LOGGER.warn("[SoundScape][EFX] Initialization failed; EFX disabled", t); }
    }

    public void apply(int source, float occlusion, float reverbSend) {
        if (!ready) return;
        try {
            float hf = Math.max(0.05f, Math.min(1.0f, occlusion));
            EXTEfx.alFilterf(lowPass, EXTEfx.AL_LOWPASS_GAIN, hf);
            EXTEfx.alFilterf(lowPass, EXTEfx.AL_LOWPASS_GAINHF, hf);
            AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, config.occlusion ? lowPass : EXTEfx.AL_FILTER_NULL);
            AL11.alSource3i(source, EXTEfx.AL_AUXILIARY_SEND_FILTER, slot, 0, EXTEfx.AL_FILTER_NULL);
            EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, Math.max(0.0f, Math.min(1.0f, reverbSend)));
        } catch (Throwable t) { SoundScape.LOGGER.debug("[SoundScape][EFX] Source update failed", t); }
    }
}
