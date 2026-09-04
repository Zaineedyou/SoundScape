package com.zaineedyou.soundscape.mixin;

import com.zaineedyou.soundscape.SoundScape;
import com.zaineedyou.soundscape.AcousticModel;
import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.phys.Vec3;

@Mixin(Channel.class)
public abstract class ChannelEffectsMixin {
    @Inject(method = "setVolume", at = @At("TAIL"), require = 0)
    private void soundscape$applyEffects(float volume, CallbackInfo ci) {
        SoundScape.AUDIO.applyEffects(soundscape$getSource());
    }

    @Inject(method = "setSelfPosition", at = @At("TAIL"), require = 0)
    private void soundscape$applyPositionEffects(Vec3 position, CallbackInfo ci) {
        AcousticModel.Result r = AcousticModel.evaluatePosition(position, SoundScape.CONFIG);
        SoundScape.AUDIO.applyEffects(soundscape$getSource(), r.gain(), r.reverbSend());
    }

    private int soundscape$getSource() {
        return ((ChannelSourceAccessor) (Object) this).soundscape$getSource();
    }

    public interface ChannelSourceAccessor {
        int soundscape$getSource();
    }
}
