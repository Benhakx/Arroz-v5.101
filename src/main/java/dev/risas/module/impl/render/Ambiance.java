/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.event.impl.render.Render3DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Ambiance", description = "Changes the time for you", category = Category.RENDER)
public final class Ambiance extends Module {

    private final NumberSetting time = new NumberSetting("Time", this, 0, 0, 22999, 1);
    private final NumberSetting timeSpeed = new NumberSetting("Time Speed", this, 0, 0, 20, 0.1);

    private final TimeUtil timer2 = new TimeUtil();
    private float timeIncrease;

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate)
            event.setCancelled(true);
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        mc.theWorld.setWorldTime((long) ((time.getValue() + (timer2.getElapsedTime() * timeSpeed.getValue()))));
    }
}
