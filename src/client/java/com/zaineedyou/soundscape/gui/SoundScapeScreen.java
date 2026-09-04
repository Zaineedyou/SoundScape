package com.zaineedyou.soundscape.gui;

import com.zaineedyou.soundscape.SoundScape;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SoundScapeScreen extends Screen {
    private final Screen parent;
    public SoundScapeScreen(Screen parent) { super(Component.literal("SoundScape Audio Settings")); this.parent = parent; }
    @Override protected void init() {
        int x = this.width / 2 - 100, y = 45;
        addRenderableWidget(toggle(x, y, "3D Spatialization", () -> SoundScape.CONFIG.spatial3d, v -> SoundScape.CONFIG.spatial3d = v));
        addRenderableWidget(toggle(x, y += 25, "HRTF (headphones)", () -> SoundScape.CONFIG.hrtf, v -> SoundScape.CONFIG.hrtf = v));
        addRenderableWidget(toggle(x, y += 25, "Stereo / Surround", () -> SoundScape.CONFIG.surround, v -> SoundScape.CONFIG.surround = v));
        addRenderableWidget(toggle(x, y += 25, "Block Occlusion", () -> SoundScape.CONFIG.occlusion, v -> SoundScape.CONFIG.occlusion = v));
        addRenderableWidget(toggle(x, y += 25, "Room Reverb", () -> SoundScape.CONFIG.reverb, v -> SoundScape.CONFIG.reverb = v));
        addRenderableWidget(Button.builder(Component.literal("Quality: " + SoundScape.CONFIG.quality), b -> {
            SoundScape.CONFIG.quality = SoundScape.CONFIG.quality.equals("low") ? "medium" : SoundScape.CONFIG.quality.equals("medium") ? "high" : "low";
            b.setMessage(Component.literal("Quality: " + SoundScape.CONFIG.quality));
            SoundScape.LOGGER.info("[SoundScape][GUI] Quality changed to {}", SoundScape.CONFIG.quality);
        }).bounds(x, y += 30, 200, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Save & Close"), b -> { SoundScape.CONFIG.save(); onClose(); }).bounds(x, y += 28, 200, 20).build());
    }
    private Button toggle(int x, int y, String label, java.util.function.BooleanSupplier get, java.util.function.Consumer<Boolean> set) {
        return Button.builder(Component.literal(label + ": " + (get.getAsBoolean() ? "ON" : "OFF")), b -> { boolean v = !get.getAsBoolean(); set.accept(v); b.setMessage(Component.literal(label + ": " + (v ? "ON" : "OFF"))); SoundScape.LOGGER.info("[SoundScape][GUI] {}={}", label, v); }).bounds(x, y, 200, 20).build();
    }
    @Override public void onClose() { if (this.minecraft != null) this.minecraft.setScreen(parent); }
}
