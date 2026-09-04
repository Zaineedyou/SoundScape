package com.zaineedyou.soundscape.mixin;

import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Channel.class)
public abstract class ChannelSourceAccessorMixin implements ChannelEffectsMixin.ChannelSourceAccessor {
    @Shadow private int source;
    @Override public int soundscape$getSource() { return source; }
}
