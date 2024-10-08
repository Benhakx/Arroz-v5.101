/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.combat;

import dev.risas.Rise;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.other.AttackEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.module.impl.movement.Speed;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.MoveUtil;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Criticals", description = "Makes you always get a critical hit on your opponent", category = Category.COMBAT)
public final class Criticals extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "Hypixel", "Position", "Low", "Low 2", "Down", "Jump", "Vape AntiCheat");
    private final NumberSetting delay = new NumberSetting("Delay", this, 500, 0, 1000, 25);

    private final TimeUtil timer = new TimeUtil();
    private int ticks, offGroundTicks;
    private boolean attacked;

    @Override
    public void onUpdateAlwaysInGui() {
        delay.hidden = mode.is("Low");
        if (!Rise.devMode && mode.getMode().equals("Vape AntiCheat")) mode.set("Packet");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (attacked) {
            switch (mode.getMode()) {
                case "Down": {
                    if (ticks >= 5) {
                        mc.thePlayer.motionY = -MoveUtil.getJumpMotion(0.42F);
                        attacked = false;
                        ticks = 0;
                    } else
                        ticks++;
                    break;
                }

                case "Hypixel": {
                    if (mc.thePlayer.onGround && mc.thePlayer.posY % 1 == 0) {
                        ticks++;
                        switch (ticks) {
                            case 1:
                                event.setY(event.getY() + 0.046875 + Math.random() / 100);
                                event.setGround(false);
                                break;

                            case 2:
                                event.setY(event.getY() + 0.0234375 + Math.random() / 100);
                                event.setGround(false);
                                break;

                            case 3:
                                attacked = false;
                                ticks = 0;
                                break;
                        }
                    } else {
                        attacked = false;
                        ticks = 0;
                    }
                    break;
                }

                case "Position": {
                    if (mc.thePlayer.onGround) {
                        ticks++;

                        switch (ticks) {
                            case 1:
                                event.setY(event.getY() + 0.0625);
                                event.setGround(false);
                                break;

                            case 2:
                                event.setY(event.getY() + 0.015625);
                                event.setGround(false);
                                break;

                            case 3:
                                attacked = false;
                                ticks = 0;
                                break;
                        }
                    } else {
                        attacked = false;
                        ticks = 0;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (!mc.thePlayer.movementInput.jump && (timer.hasReached((long) delay.getValue()) || mode.is("Low")) && !mc.thePlayer.isInLiquid() && !mc.thePlayer.isOnLadder() && (!this.getModule(Speed.class).isEnabled() || mode.is("Hypixel"))) {
            switch (mode.getMode()) {
                case "Packet": {
                    if (mc.thePlayer.onGround) {
                        final double[] values = {0.0625, 0.001}; // CARPET VALUE
                        for (final double d : values) {
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, d == 0.0625));
                        }
                    }
                    break;
                }

                case "Vape AntiCheat": {
                    if (mc.thePlayer.onGround) {
                        final double[] values = {0.04F, 0.0, 0.03F, 0.0};
                        for (final double d : values)
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, d == 0.0));
                    }

                    return;
                }

                case "Low": {
                    if (mc.thePlayer.onGround)
                        offGroundTicks = 0;
                    else
                        offGroundTicks++;

                    if (offGroundTicks == 1)
                        mc.thePlayer.motionY = -(0.01 - Math.random() / 500);

                    if (mc.thePlayer.onGround)
                        mc.thePlayer.motionY = 0.1 + Math.random() / 500;
                    break;
                }

                case "Low 2": {
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (0.3 - Math.random() / 500), mc.thePlayer.posZ);
                    break;
                }

                case "Down":
                case "Jump": {
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.jump();
                    break;
                }
            }
            attacked = true;
            timer.reset();
        }
    }

    @Override
    protected void onDisable() {
        attacked = false;
        timer.reset();
        ticks = 0;
    }
}