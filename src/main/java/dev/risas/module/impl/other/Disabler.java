/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import dev.risas.Rise;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.motion.TeleportEvent;
import dev.risas.event.impl.other.AttackEvent;
import dev.risas.event.impl.other.StrafeEvent;
import dev.risas.event.impl.other.UpdateEvent;
import dev.risas.event.impl.other.WorldChangedEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.event.impl.render.Render2DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NoteSetting;
import dev.risas.util.pathfinding.MainPathFinder;
import dev.risas.util.pathfinding.Vec3;
import dev.risas.util.player.PacketUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This module is designed to disable an anticheat or some of its checks
 * in order to make bypassing easier/having extreme bypasses.
 */
@ModuleInfo(name = "Disabler", description = "Disables some servers AntiCheats", category = Category.OTHER)
public final class Disabler extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);

    private final NoteSetting Trojan = new NoteSetting("Trojan", this);

    private final BooleanSetting trojantest = new BooleanSetting("Trojan (Test)", this, false);

    private final NoteSetting verus = new NoteSetting("Verus", this);

    private final BooleanSetting oldVerusCombat = new BooleanSetting("Verus Combat (Old)", this, false);
    private final BooleanSetting VerusCombat = new BooleanSetting("Verus Combat", this, false);
    private final BooleanSetting onlyCombat = new BooleanSetting("OnlyCombat", this, false);
    private final BooleanSetting VerusPacketFixer = new BooleanSetting("Packet Fixer", this, false);

    private final NoteSetting others = new NoteSetting("Others", this);

    private final BooleanSetting tbalance = new BooleanSetting("Timer Balance", this, false);
    private final BooleanSetting c0ftbalance = new BooleanSetting("Ignore C0F (tbalance)", this, false);

    private List<C0FPacketConfirmTransaction> tpackets = new ArrayList<>();
    private long balance = 0L;
    private long last = 0L;
    private boolean IgnoreC0F = false;

    private boolean transaction = false;
    private boolean isOnCombat = false;
    private boolean tp;
    int transactions = 0;
    short newTransaction = 0;
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private float yaw = 0.0F;
    private float pitch = 0.0F;
    private int jam = 0;
    private int packetCount = 0;
    private int prevSlot = -1;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        isOnCombat = true;
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        isOnCombat = false;
    }

    @Override
    protected void onEnable() {
        transactions = 0;
        newTransaction = 0;
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
        tp = false;
        balance = 0;
        last = 0;
        IgnoreC0F = false;

        if (mc.thePlayer != null && mc.thePlayer.ticksExisted > 50) {
            for (C0FPacketConfirmTransaction packet : tpackets) {
                PacketUtil.sendPacketWithoutEvent(packet);
            }
            tpackets.clear();
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
     if (trojantest.isEnabled()) {
               if (mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 5 == 0) {
                   event.setY(event.getY() - 0.9);
                   tp = true;
               }
          }
    }

    @Override
    public void onTeleportEvent(final TeleportEvent event) {

    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (tbalance.isEnabled()) {
            if (mc.thePlayer.ticksExisted % 20 == 0)
                Rise.addChatMessage("Balance: " + balance);
          }

       if (oldVerusCombat.isEnabled()) {
           if (mc.thePlayer.ticksExisted % 600 == 0) {
               PacketUtil.sendPacket(new C00PacketKeepAlive());
           }
       }

       if (trojantest.isEnabled()) {
           BlockPos pos = mc.thePlayer.getPosition().add(0, mc.thePlayer.posY > 0 ? -255 : 255, 0);
           if (pos == null) return;

           if (mc.thePlayer.onGround) {
               PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                       pos,
                       256,
                       new ItemStack(Items.water_bucket),
                       0F,
                       0.5F + (float) Math.random() * 0.44F,
                       0F
               ));
               PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                       pos,
                       256,
                       new ItemStack(Blocks.stone),
                       0F,
                       0.5F + (float) Math.random() * 0.44F,
                       0F
               ));
           }
       }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.isIntegratedServerRunning()) return;

        final Packet<?> packet = event.getPacket();

        if (trojantest.isEnabled()) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && tp) {
                final S08PacketPlayerPosLook p = (S08PacketPlayerPosLook) event.getPacket();

                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(p.x, p.y, p.z), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(p.x, p.y, p.z, p.yaw, p.pitch, true));

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec.getX(), vec.getY(), vec.getZ(), p.yaw, p.pitch, true));

                event.setCancelled(true);
                tp = false;
            }
        }

        if (VerusCombat.isEnabled()) {
            if (mc.thePlayer == null) return;

            if (mc.thePlayer.ticksExisted <= 20) {
                isOnCombat = false;
                return;
            }

            if (onlyCombat.isEnabled() && !isOnCombat) {
                return;
            }

            if (packet instanceof S32PacketConfirmTransaction) {
                event.setCancelled(true);
                PacketUtil.sendPacket(new C0FPacketConfirmTransaction(transaction ? 1 : -1, (short) (transaction ? -1 : 1), transaction));
                transaction = !transaction;
            }
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.isIntegratedServerRunning())
            return;

        final Packet<?> packet = event.getPacket();

        if (tbalance.isEnabled()) {
            if (packet instanceof C0FPacketConfirmTransaction && c0ftbalance.isEnabled()) {
                if (IgnoreC0F) {
                    event.setCancelled(true);
                    tpackets.add((C0FPacketConfirmTransaction) packet);
                } else if (!tpackets.isEmpty()) {
                    event.setCancelled(true);
                    tpackets.add((C0FPacketConfirmTransaction) packet);

                    PacketUtil.sendPacketWithoutEvent(tpackets.remove(0));
                }
            }

            if (packet instanceof C03PacketPlayer) {
                if (last == 0L) {
                    last = System.currentTimeMillis();
                    return;
                }

                if (!((C03PacketPlayer) packet).isMoving() && !((C03PacketPlayer) packet).isRotating()) {
                    event.setCancelled(true);
                }

                IgnoreC0F = event.isCancelled();

                if (IgnoreC0F && c0ftbalance.isEnabled()) {
                    balance -= 50;
                    return;
                }

                balance += 50;
                balance -= System.currentTimeMillis() - last;

                last = System.currentTimeMillis();
            }

        }

        if (trojantest.isEnabled()) {

        }

           if (oldVerusCombat.isEnabled()) {
               if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 20) return;
               if (packet instanceof C00PacketKeepAlive) {
                   event.setCancelled(true);
               }
               if (packet instanceof C0FPacketConfirmTransaction) {
                   transactions++;
                   if (transactions <= 1) {
                       newTransaction = ((C0FPacketConfirmTransaction) packet).getUid();
                   }
                   ((C0FPacketConfirmTransaction) packet).uid = newTransaction;
               }

               if (packet instanceof S37PacketStatistics) {
                   transactions = 0;
               }
           }

           if (VerusPacketFixer.isEnabled()) {
               if (mc.thePlayer == null || mc.theWorld == null || event.isCancelled()) {
                   return;
               }

               if (packet instanceof C03PacketPlayer && !(packet instanceof C04PacketPlayerPosition) && !(packet instanceof C06PacketPlayerPosLook)) {
                   if ((mc.thePlayer.motionY == 0.0 || (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) && !((C03PacketPlayer) packet).onGround) {
                       ((C03PacketPlayer) packet).onGround = true;
                   }
               }

               if (packet instanceof C04PacketPlayerPosition) {
                   x = ((C04PacketPlayerPosition) packet).x;
                   y = ((C04PacketPlayerPosition) packet).y;
                   z = ((C04PacketPlayerPosition) packet).z;
                   jam = 0;
               }

               if (packet instanceof C05PacketPlayerLook) {
                   yaw = ((C05PacketPlayerLook) packet).yaw;
                   pitch = ((C05PacketPlayerLook) packet).pitch;
               }

               if (packet instanceof C06PacketPlayerPosLook) {
                   x = ((C06PacketPlayerPosLook) packet).x;
                   y = ((C06PacketPlayerPosLook) packet).y;
                   z = ((C06PacketPlayerPosLook) packet).z;
                   jam = 0;

                   yaw = ((C06PacketPlayerPosLook) packet).yaw;
                   pitch = ((C06PacketPlayerPosLook) packet).pitch;
               }

               if (packet instanceof C03PacketPlayer && !(packet instanceof C04PacketPlayerPosition) && !(packet instanceof C06PacketPlayerPosLook)) {
                   jam++;
                   if (jam > 20) {
                       jam = 0;
                       event.setCancelled(true);
                       PacketUtil.sendPacketWithoutEvent(new C06PacketPlayerPosLook(x, y, z, yaw, pitch, ((C03PacketPlayer) packet).onGround));
                   }
               }

               if (!mc.isSingleplayer() && packet instanceof C09PacketHeldItemChange) {
                   if (((C09PacketHeldItemChange) packet).slotId == prevSlot) {
                       event.setCancelled(true);
                   } else {
                       prevSlot = ((C09PacketHeldItemChange) packet).slotId;
                   }
               }

               if (packet instanceof C08PacketPlayerBlockPlacement) {
                   ((C08PacketPlayerBlockPlacement) packet).facingX = Math.max(-1.00000F, Math.min(1.00000F, ((C08PacketPlayerBlockPlacement) packet).facingX));
                   ((C08PacketPlayerBlockPlacement) packet).facingY = Math.max(-1.00000F, Math.min(1.00000F, ((C08PacketPlayerBlockPlacement) packet).facingY));
                   ((C08PacketPlayerBlockPlacement) packet).facingZ = Math.max(-1.00000F, Math.min(1.00000F, ((C08PacketPlayerBlockPlacement) packet).facingZ));
               }

               if ((Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Blink")).isEnabled()) || Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled() && packet instanceof C00PacketKeepAlive) {
                   event.setCancelled(true);
               }

               if (packet instanceof C03PacketPlayer && !((C03PacketPlayer) packet).onGround) {
                   if (!(packet instanceof C04PacketPlayerPosition) && !(packet instanceof C05PacketPlayerLook) && !(packet instanceof C06PacketPlayerPosLook)) {
                       packetCount++;
                       if (packetCount >= 2) {
                           event.setCancelled(true);
                       }
                   } else {
                       packetCount = 0;
                   }
               }
           }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
      if (tbalance.isEnabled()) {
          if (balance >= 49)
              mc.timer.timerSpeed = 1.0F;
        }
    }

}