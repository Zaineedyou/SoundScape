package com.zaineedyou.soundscape.config;

import com.zaineedyou.soundscape.SoundScape;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SoundScapeConfig {
    public boolean stereo = true;
    public boolean spatial3d = true;
    public boolean hrtf = true;
    public boolean surround = true;
    public boolean occlusion = true;
    public boolean reverb = true;
    public boolean debugLogging = true;
    public String quality = "medium";
    public float masterGain = 1.0f;

    private static Path path() { return Path.of("config", "soundscape.properties"); }

    public static SoundScapeConfig load() {
        SoundScapeConfig c = new SoundScapeConfig();
        Path p = path();
        try {
            if (Files.exists(p)) {
                Properties x = new Properties();
                try (var in = Files.newInputStream(p)) { x.load(in); }
                c.stereo = bool(x, "stereo", c.stereo); c.spatial3d = bool(x, "spatial3d", c.spatial3d);
                c.hrtf = bool(x, "hrtf", c.hrtf); c.surround = bool(x, "surround", c.surround);
                c.occlusion = bool(x, "occlusion", c.occlusion); c.reverb = bool(x, "reverb", c.reverb);
                c.debugLogging = bool(x, "debug-logging", c.debugLogging);
                c.quality = x.getProperty("quality", c.quality);
                c.masterGain = Float.parseFloat(x.getProperty("master-gain", "1.0"));
                SoundScape.LOGGER.info("[SoundScape][Config] Loaded {}", p.toAbsolutePath());
            } else c.save();
        } catch (Exception e) { SoundScape.LOGGER.warn("[SoundScape][Config] Load failed; defaults active", e); }
        return c;
    }

    public void save() {
        try {
            Files.createDirectories(path().getParent());
            Properties x = new Properties();
            x.setProperty("stereo", Boolean.toString(stereo)); x.setProperty("spatial3d", Boolean.toString(spatial3d));
            x.setProperty("hrtf", Boolean.toString(hrtf)); x.setProperty("surround", Boolean.toString(surround));
            x.setProperty("occlusion", Boolean.toString(occlusion)); x.setProperty("reverb", Boolean.toString(reverb));
            x.setProperty("debug-logging", Boolean.toString(debugLogging)); x.setProperty("quality", quality);
            x.setProperty("master-gain", Float.toString(masterGain));
            try (var out = Files.newOutputStream(path())) { x.store(out, "SoundScape client audio configuration"); }
            SoundScape.LOGGER.info("[SoundScape][Config] Saved {}", path().toAbsolutePath());
        } catch (IOException e) { SoundScape.LOGGER.warn("[SoundScape][Config] Save failed", e); }
    }
    private static boolean bool(Properties p, String k, boolean d) { return Boolean.parseBoolean(p.getProperty(k, Boolean.toString(d))); }
}
