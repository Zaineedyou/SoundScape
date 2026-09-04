package com.zaineedyou.soundscape;

import com.zaineedyou.soundscape.SoundScape;
import com.zaineedyou.soundscape.config.SoundScapeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class AcousticModel {
    private AcousticModel() {}
    public record Result(float gain, float pitch, float reverbSend, int blockedRays) {}

    public static Result evaluate(SoundInstance sound, SoundScapeConfig config) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.gameRenderer == null) return new Result(1.0f, 1.0f, 0.0f, 0);
        Vec3 listener = mc.gameRenderer.getMainCamera().position();
        Vec3 source = new Vec3(sound.getX(), sound.getY(), sound.getZ());
        int blocked = 0;
        if (config.occlusion) {
            Vec3[] offsets = {Vec3.ZERO, new Vec3(0, .45, 0), new Vec3(0, -.45, 0)};
            for (Vec3 offset : offsets) {
                HitResult hit = mc.level.clip(new ClipContext(listener, source.add(offset), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
                if (hit.getType() != HitResult.Type.MISS) blocked++;
            }
        }
        float occlusionGain = blocked == 0 ? 1.0f : blocked == 1 ? 0.72f : 0.42f;
        float room = config.reverb ? estimateRoom(mc, listener) : 0.0f;
        if (config.debugLogging && blocked > 0)
            SoundScape.LOGGER.debug("[SoundScape][Acoustics] sound={} blockedRays={} occlusionGain={} reverbSend={}", sound.getIdentifier(), blocked, occlusionGain, room);
        return new Result(occlusionGain, 1.0f, room, blocked);
    }

    private static float estimateRoom(Minecraft mc, Vec3 p) {
        int open = 0;
        Vec3[] dirs = {new Vec3(1,0,0), new Vec3(-1,0,0), new Vec3(0,1,0), new Vec3(0,-1,0), new Vec3(0,0,1), new Vec3(0,0,-1)};
        for (Vec3 d : dirs) {
            HitResult h = mc.level.clip(new ClipContext(p, p.add(d.scale(8)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
            if (h.getType() == HitResult.Type.MISS) open++;
        }
        return (6 - open) / 6.0f;
    }
}
