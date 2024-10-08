package dev.risas.module;

import dev.risas.Rise;
import dev.risas.event.impl.motion.MinimumMotionEvent;
import dev.risas.event.impl.motion.PostMotionEvent;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.motion.TeleportEvent;
import dev.risas.event.impl.other.*;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.event.impl.render.*;
import dev.risas.module.api.ModuleInfo;
import dev.risas.notifications.NotificationType;
import dev.risas.setting.Setting;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.util.InstanceAccess;
import dev.risas.util.math.MathUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the super class for all of the modules.
 * This class includes all the necessary methods and functions for a module.
 *
 * @author Tecnio
 * @since 02/12/2021
 */

@Getter
@Setter
public abstract class Module implements InstanceAccess {

    private boolean enabled;
    private int keyBind;

    //RenderPosition
    public float renderX, renderY;

    public float sizeInGui;
    public boolean expanded;
    public float clickGuiOpacity;
    public float descOpacityInGui = 1;
    public boolean hidden = false;

    //Module Settings
    public List<Setting> settings = new ArrayList<>();

    private ModuleInfo moduleInfo;

    public Module() {
        if (this.getClass().isAnnotationPresent(ModuleInfo.class)) {
            this.moduleInfo = this.getClass().getAnnotation(ModuleInfo.class);
        } else {
            throw new RuntimeException("ModuleInfo annotation not found on " + this.getClass().getSimpleName());
        }

        keyBind = getModuleInfo().defaultKey();
    }

    public boolean toggleModule() {
        enabled = !enabled;

        if (enabled) onEnable();
        else onDisable();

        renderX = mc.displayWidth;
        renderY = -mc.displayHeight;

        Rise.INSTANCE.getModuleManager().setEdited(true);

        if (((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().
                getSetting("Interface", "Show Notifications on Toggle"))).isEnabled() && !getModuleInfo().name().equals("ClickGui") && !getModuleInfo().name().equals("Freelook")) {
            Rise.INSTANCE.getNotificationManager().registerNotification(
                    (enabled ? "Enabled" : "Disabled") + " " + getModuleInfo().name(), 2000, NotificationType.NOTIFICATION);
        }

        return enabled;
    }

    public boolean toggleNoEvent() {
        enabled = !enabled;
        return enabled;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public Setting getSetting(final String name) {
        for (final Setting setting : settings) {
            if (setting.name.equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }
    public Setting getSettingAlternative(final String name) {
        for (final Setting setting : settings) {
            final String comparingName = setting.name.replaceAll(" ", "");

            if (comparingName.equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }

    protected double randomDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    protected int randomInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    protected double random() {
        return ThreadLocalRandom.current().nextDouble(1);
    }

    /**
     * Event methods are here and can be overridden from subclasses for event listening.
     */
    public void onPreMotion(final PreMotionEvent event) {
    }

    public void onPostMotion(final PostMotionEvent event) {
    }

    public void onMove(final MoveEvent event) {
    }


    public void onPacketReceive(final PacketReceiveEvent event) {
    }

    public void onPacketSend(final PacketSendEvent event) {
    }

    public void onStrafe(final StrafeEvent event) {
    }

    public void onShader3DEvent(final Shader3DEvent event) {
    }

    public void onRender3DEvent(final Render3DEvent event) {
    }

    public void onRender2DEvent(final Render2DEvent event) {
    }

    public void onCanPlaceBlockEvent(final CanPlaceBlockEvent event) {
    }

    public void onBlockBreak(final BlockBreakEvent event) {
    }

    public void onAttackEvent(final AttackEvent event) {
    }

    public void onMoveButton(final MoveButtonEvent event) {
    }

    public void onKey(final KeyEvent event) {
    }

    public void onUpdateAlwaysInGui() {
    }

    public void onUpdateAlways() {
    }

    public void onBlur(final BlurEvent event) {
    }

    public void onMouseOver(final MouseOverEvent event) {
    }

    public void onMinimumMotion(final MinimumMotionEvent event) {
    }

    public void onFadingOutline(final FadingOutlineEvent event) {
    }

    public void onPreBlur(final PreBlurEvent event) {
    }

    public void onOpenGUI(final OpenGUIEvent event) {
    }

    public void onWorldChanged(final WorldChangedEvent event) {
    }

    public void onUpdate(final UpdateEvent event) {
    }

    public void onBlockCollide(final BlockCollideEvent event) {
    }

    public void onTeleportEvent(final TeleportEvent event) {
    }
}
