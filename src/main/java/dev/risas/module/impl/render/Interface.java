/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.Rise;
import dev.risas.event.impl.render.BlurEvent;
import dev.risas.event.impl.render.FadingOutlineEvent;
import dev.risas.event.impl.render.Render2DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.ui.ingame.IngameGUI;
import dev.risas.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Interface", description = "Shows information about Rise", category = Category.RENDER)
public final class Interface extends Module {

    private final ModeSetting mode = new ModeSetting("Theme", this, "Rise Blend",
            "Rise Rainbow", "Rise", "Rise Blend", "Rise Christmas", "Rise Cotton Candy", "Rise Sea", "Rise Cool", "Rise Blaze", "Rise Emo", /*"Rise 6 Old",*/ "Rice", "Comfort", "Comfort Rainbow",
            "Minecraft", "Minecraft Rainbow", /*"Classic Revamp" */ "Never Lose", "Never Lose Rainbow", "Skeet", "One Tap");

    private final ModeSetting list = new ModeSetting("List Animation", this, "Rise", "Rise", "Slide");

    private final NumberSetting red = new NumberSetting("Red", this, 19, 0, 255, 1);
    private final NumberSetting green = new NumberSetting("Green", this, 150, 0, 255, 1);
    private final NumberSetting blue = new NumberSetting("Blue", this, 255, 0, 255, 1);

    private final BooleanSetting renderOnArrayList = new BooleanSetting("Show Render Modules on List", this, false);
    private final BooleanSetting outLine = new BooleanSetting("Arraylist Outline", this, false);
    private final BooleanSetting customHotbar = new BooleanSetting("Custom Hotbar", this, true);
    private final BooleanSetting smoothHotbar = new BooleanSetting("Smooth Hotbar", this, true);
    private final BooleanSetting armorHUD = new BooleanSetting("Armor HUD", this, false);
    private final BooleanSetting restoreDefaults = new BooleanSetting("Restore Defaults", this, false);
    private final BooleanSetting chatBackground = new BooleanSetting("Transparent Chat", this, false);
    private final BooleanSetting customChatFont = new BooleanSetting("Custom Chat Font", this, false);
    private final BooleanSetting enableNoti = new BooleanSetting("Show Notifications on Toggle", this, false);
    private final BooleanSetting bps = new BooleanSetting("Blocks Per Second Counter", this, true);
    private final ModeSetting cape = new ModeSetting("Cape", this, "Electric Sky", "Night", "Electric Sky");

    public static boolean customChat, customHotbarEnabled;

    public static String theme;

    public static int red0, green0, blue0;

    @Override
    public void onUpdateAlways() {
        theme = mode.getMode();

        customChat = customChatFont.isEnabled();
        customHotbarEnabled = customHotbar.isEnabled();

        red0 = (int) red.getValue();
        green0 = (int) green.getValue();
        blue0 = (int) blue.getValue();

        outLine.hidden = !mode.is("Rice")
                && !mode.is("Rise Christmas")
                && !mode.is("Rise Blend")
                && !mode.is("Rise Cotton Candy")
                && !mode.is("Rise Blaze")
                && !mode.is("Rise Cool")
                && !mode.is("Rise Sea")
                && !mode.is("Rise Emo")
                && !mode.is("Never Lose");

        if (restoreDefaults.isEnabled()) {
            restoreDefaults.toggle();

            final Color color = new Color(Rise.CLIENT_THEME_COLOR_DEFAULT);

            red.value = color.getRed();
            green.value = color.getGreen();
            blue.value = color.getBlue();
        }

        if (!isEnabled()) toggleModule();
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);

        final int posX = (mc.displayWidth / (mc.gameSettings.guiScale * 2));
        final int x = posX - 5, y = sr.getScaledHeight() - 55;

        if (armorHUD.isEnabled()) {
            RenderUtil.drawArmorHUD(x, y);
        }
    }

    @Override
    public void onBlur(final BlurEvent event) {
        IngameGUI.onBlur();
    }

    @Override
    public void onFadingOutline(final FadingOutlineEvent event) {
        if (outLine.isEnabled()) {
            IngameGUI.onFadeOutline();
        }
    }
}
