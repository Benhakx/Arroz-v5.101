/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.player;

import dev.risas.Rise;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.module.impl.movement.Fly;
import dev.risas.module.impl.movement.HighJump;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.player.MoveUtil;
import dev.risas.util.player.PacketUtil;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Vec3;

import java.util.concurrent.ConcurrentLinkedQueue;

@ModuleInfo(name = "NoVoid", description = "Prevents you from falling into the void", category = Category.PLAYER)
public final class NoVoid extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Flag", "Flag", "Position", "Jump", "Ground", "Matrix", "Hypixel", "Boost", "Blink");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", this, 5, 1, 10, 0.1);
    private final BooleanSetting voidCheck = new BooleanSetting("Void Check", this, true);
    private final BooleanSetting showNotifications = new BooleanSetting("Show Notifications", this, false);

    private Vec3 lastGround = new Vec3(0, 0, 0);
    private boolean saved;
    private boolean aBoolean, aBoolean2;

    private static final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private Vec3 lastServerPosition = null;

    @Override
    protected void onDisable() {
        saved = false;
        lastServerPosition = null;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.onGround)
            lastGround = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        final boolean isBlockUnder = PlayerUtil.isBlockUnder();

        if ((!isBlockUnder || !voidCheck.isEnabled()) && mc.thePlayer.fallDistance > fallDistance.getValue() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.capabilities.isFlying && !this.getModule(Fly.class).isEnabled() && !this.getModule(HighJump.class).isEnabled()) {
            if (!saved) {
                switch (mode.getMode()) {
                    case "Flag":
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition());
                        break;

                    case "Jump":
                        mc.thePlayer.jump();
                        break;

                    case "Ground":
                        event.setGround(true);
                        break;

                    case "Matrix":
                        if ((mc.thePlayer.motionY + mc.thePlayer.posY) < Math.floor(mc.thePlayer.posY)) {
                            mc.thePlayer.motionY = Math.floor(mc.thePlayer.posY) - mc.thePlayer.posY;

                            if (mc.thePlayer.motionY == 0)
                                event.setGround(true);
                        }
                        break;

                    case "Position":
                        event.setY(event.getY() + mc.thePlayer.fallDistance);
                        break;

                    case "Boost":
                        mc.thePlayer.motionY = 1;
                        mc.thePlayer.fallDistance = 0;
                        event.setGround(true);
                        break;

                    case "Blink":
                        if (lastServerPosition == null)
                            return;

                        mc.thePlayer.setPosition(lastServerPosition.xCoord, lastServerPosition.yCoord, lastServerPosition.zCoord);
                        mc.thePlayer.fallDistance = 0;
                        mc.thePlayer.motionY = 0;
                        MoveUtil.stop();

                        aBoolean = true;
                        break;

                    case "Hypixel":

                        break;
                }

                if (!saved) {
                    if (showNotifications.isEnabled())
                        this.registerNotification("Attempted to save you from the void.");

                    Rise.amountOfVoidSaves++;
                    saved = true;
                }
            } else
                saved = false;
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        switch (mode.getMode()) {
            case "Blink": {
                if (!(p instanceof C03PacketPlayer || p instanceof C0FPacketConfirmTransaction))
                    return;

                final boolean blink = mc.thePlayer.fallDistance <= 6 && !PlayerUtil.isBlockUnder() && !this.getModule(Fly.class).isEnabled() && !this.getModule(HighJump.class).isEnabled() && !mc.gameSettings.keyBindSneak.isKeyDown();

                if (blink) {
                    if (lastServerPosition == null)
                        lastServerPosition = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

                    event.setCancelled(true);
                    packets.add(p);
                } else if (p instanceof C03PacketPlayer) {
                    if (!(PlayerUtil.isBlockUnder() && !aBoolean))
                        packets.removeIf(packet -> packet instanceof C03PacketPlayer);
                    packets.forEach(PacketUtil::sendPacketWithoutEvent);
                    packets.clear();
                    lastServerPosition = null;
                    aBoolean = false;
                }
                break;
            }

            case "Hypixel": {
                if (p instanceof C03PacketPlayer && (!PlayerUtil.isBlockUnder() ||
                        !voidCheck.isEnabled()) && mc.thePlayer.fallDistance > fallDistance.getValue() &&
                        !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.capabilities.isFlying &&
                        !this.getModule(Fly.class).isEnabled() && !this.getModule(HighJump.class).isEnabled()) {
                    event.setPacket(new C03PacketPlayer(false));
                }
                break;
            }
        }
    }
}