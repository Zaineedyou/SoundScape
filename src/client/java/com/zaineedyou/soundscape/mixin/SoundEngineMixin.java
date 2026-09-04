package com.zaineedyou.soundscape.mixin;

import com.zaineedyou.soundscape.SoundScape;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Inject(method = "loadLibrary", at = @At("TAIL"), require = 0)
    private void soundscape$onAudioLibraryLoaded(CallbackInfo ci) {
        SoundScape.LOGGER.info("[SoundScape][Audio] Minecraft sound library loaded; active mode={}", SoundScape.AUDIO.mode());
    }
    @Inject(method = "stopAll", at = @At("HEAD"), require = 0)
    private void soundscape$onStopAll(CallbackInfo ci) {
        SoundScape.LOGGER.debug("[SoundScape][Audio] SoundEngine stopAll observed");
    }
}
