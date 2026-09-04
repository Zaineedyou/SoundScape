package com.zaineedyou.soundscape;

import com.zaineedyou.soundscape.audio.AudioRuntime;
import com.zaineedyou.soundscape.config.SoundScapeConfig;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SoundScape {
    public static final String MOD_ID = "soundscape";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final SoundScapeConfig CONFIG = SoundScapeConfig.load();
    public static final AudioRuntime AUDIO = new AudioRuntime(CONFIG);

    private SoundScape() {}

    public static void init() {
        LOGGER.info("[SoundScape] Initializing client audio spatializer v{}", "1.0.0");
        AUDIO.initialize();
        LOGGER.info("[SoundScape] Features: stereo={}, spatial3d={}, hrtf={}, surround={}, occlusion={}, reverb={}",
                CONFIG.stereo, CONFIG.spatial3d, CONFIG.hrtf, CONFIG.surround, CONFIG.occlusion, CONFIG.reverb);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
