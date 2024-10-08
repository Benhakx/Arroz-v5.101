/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.render.Render3DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.render.RenderUtil;
import net.minecraft.util.Vec3;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.ArrayList;
import java.util.List;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Breadcrumbs", description = "Shows a trail where you walk", category = Category.RENDER)
public final class Breadcrumbs extends Module {

    List<Vec3> path = new ArrayList<>();

    private final BooleanSetting timeoutBool = new BooleanSetting("Timeout", this, true);
    private final NumberSetting timeout = new NumberSetting("Time", this, 15, 1, 150, 0.1);

    @Override
    public void onUpdateAlwaysInGui() {
        timeout.hidden = !timeoutBool.isEnabled();
    }

    @Override
    protected void onEnable() {
        path.clear();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {

        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }

        if (timeoutBool.isEnabled())
            while (path.size() > (int) timeout.getValue()) {
                path.remove(0);
            }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        RenderUtil.renderBreadCrumbs(path);
    }
}
