/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.player;

import dev.risas.Rise;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.module.impl.render.Freecam;
import dev.risas.util.pathfinding.MainPathFinder;
import dev.risas.util.pathfinding.Vec3;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@ModuleInfo(name = "Infinite", description = "Allows you to interact with things from an infinite range", category = Category.PLAYER)
public final class Infinite extends Module {

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        if (freecamEnabled) {
            x = Freecam.startX;
            y = Freecam.startY;
            z = Freecam.startZ;
        }

        final double finalX = x;
        final double finalY = y;
        final double finalZ = z;

        if (p instanceof C02PacketUseEntity) {
            event.setCancelled(true);

            final C02PacketUseEntity c02 = (C02PacketUseEntity) p;
            final BlockPos position = c02.getEntityFromWorld(mc.theWorld).getPosition();
            final BlockPos actualPos = new BlockPos(position.getX(), position.getY() - 0.5D, position.getZ());
            new Thread(() -> {
                /* Getting path */
                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(actualPos.getX(), actualPos.getY(), actualPos.getZ()));

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                PacketUtil.sendPacketWithoutEvent(p);

                Collections.reverse(path);

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
            }).start();
        }

        if (p instanceof C08PacketPlayerBlockPlacement) {
            event.setCancelled(true);

            final C08PacketPlayerBlockPlacement c08 = (C08PacketPlayerBlockPlacement) p;
            final BlockPos pos = c08.getPosition();
            new Thread(() -> {
                /* Getting path */
                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(pos.getX(), pos.getY(), pos.getZ()));

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                PacketUtil.sendPacketWithoutEvent(p);

                Collections.reverse(path);

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
            }).start();
        }

        if (p instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging c07 = (C07PacketPlayerDigging) p;
            if (!(c07.getStatus().equals(C07PacketPlayerDigging.Action.DROP_ITEM) || c07.getStatus().equals(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS))) {
                event.setCancelled(true);
                final BlockPos pos = c07.getPosition();
                new Thread(() -> {
                    /* Getting path */
                    final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(pos.getX(), pos.getY(), pos.getZ()));

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                    PacketUtil.sendPacketWithoutEvent(p);

                    Collections.reverse(path);

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                }).start();
            }
        }
    }
}