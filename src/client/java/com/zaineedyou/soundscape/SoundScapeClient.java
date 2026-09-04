package com.zaineedyou.soundscape;

import com.zaineedyou.soundscape.gui.SoundScapeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class SoundScapeClient implements ClientModInitializer {
    private static KeyMapping openMenu;
    @Override public void onInitializeClient() {
        SoundScape.init();
        openMenu = new KeyMapping("key.soundscape.open_menu", GLFW.GLFW_KEY_O,
                KeyMapping.Category.register(SoundScape.id("soundscape")));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            SoundScape.AUDIO.tick();
            while (openMenu.consumeClick() && client.screen == null) client.setScreen(new SoundScapeScreen(null));
        });
        SoundScape.LOGGER.info("[SoundScape][Client] Client hooks registered; press O for settings");
    }
}
