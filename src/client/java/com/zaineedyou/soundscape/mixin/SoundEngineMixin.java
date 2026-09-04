package com.zaineedyou.soundscape.mixin;

import com.zaineedyou.soundscape.SoundScape;
import com.zaineedyou.soundscape.AcousticModel;
import net.minecraft.client.Camera;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Inject(method = "play", at = @At("HEAD"), require = 0)
    private void soundscape$onPlay(SoundInstance sound, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        AcousticModel.Result result = AcousticModel.evaluate(sound, SoundScape.CONFIG);
        if (SoundScape.CONFIG.debugLogging)
            SoundScape.LOGGER.info("[SoundScape][Source] id={} pos=({}, {}, {}) gain={} blockedRays={} reverbSend={}",
                    sound.getIdentifier(), sound.getX(), sound.getY(), sound.getZ(), result.gain(), result.blockedRays(), result.reverbSend());
    }

    @Inject(method = "updateSource", at = @At("TAIL"), require = 0)
    private void soundscape$onListenerUpdate(Camera camera, CallbackInfo ci) {
        if (SoundScape.CONFIG.debugLogging)
            SoundScape.LOGGER.debug("[SoundScape][Listener] position={} forward={} up={}", camera.position(), camera.forwardVector(), camera.upVector());
    }

    @Inject(method = "stopAll", at = @At("HEAD"), require = 0)
    private void soundscape$onStopAll(CallbackInfo ci) {
        SoundScape.LOGGER.debug("[SoundScape][Audio] SoundEngine stopAll observed");
    }
}
