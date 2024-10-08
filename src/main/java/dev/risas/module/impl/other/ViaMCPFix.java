/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.risas.event.impl.motion.MinimumMotionEvent;
import dev.risas.event.impl.other.BlockCollideEvent;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.event.impl.render.MouseOverEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.viamcp.ViaMCP;

/**
 * This module is designed to disable an anticheat or some of its checks
 * in order to make bypassing easier/having extreme bypasses.
 */
@ModuleInfo(name = "ViaMCPFix", description = "Fixes ViaMCP (No sirve)", category = Category.OTHER)
public final class ViaMCPFix extends Module {

    private final BooleanSetting place = new BooleanSetting("BlockPlace", this, true);
    private final BooleanSetting c03 = new BooleanSetting("C03", this, true);
    private final BooleanSetting ladder = new BooleanSetting("Ladder", this, true);
    private final BooleanSetting hitbox = new BooleanSetting("Hitbox", this, true);
    private final BooleanSetting motion = new BooleanSetting("Motion", this, true);

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (place.isEnabled()) {
          if (ViaMCP.NATIVE_VERSION >= ProtocolVersion.v1_11.getVersion()) {
            final Packet<?> packet = event.getPacket();

            if (packet instanceof C08PacketPlayerBlockPlacement) {
                final C08PacketPlayerBlockPlacement wrapper = ((C08PacketPlayerBlockPlacement) packet);

                wrapper.facingX /= 16.0F;
                wrapper.facingY /= 16.0F;
                wrapper.facingZ /= 16.0F;

                event.setPacket(wrapper);
                }
            }
        }

        if (c03.isEnabled()) {
            if (ViaMCP.NATIVE_VERSION > ProtocolVersion.v1_8.getVersion()) {
                final Packet<?> packet = event.getPacket();

                if (packet instanceof C03PacketPlayer) {
                    final C03PacketPlayer wrapper = ((C03PacketPlayer) packet);

                    if (!wrapper.isMoving() && !wrapper.isRotating()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void onBlockCollide(final BlockCollideEvent event) {
        if (ladder.isEnabled()) {
            if (ViaMCP.NATIVE_VERSION > ProtocolVersion.v1_8.getVersion()) {
                final Block block = event.getBlock();

                if (block instanceof BlockLadder) {
                    final BlockPos blockPos = event.getBlockPos();
                    final IBlockState iblockstate = mc.theWorld.getBlockState(blockPos);

                    if (iblockstate.getBlock() == block) {
                        final float f = 0.125F + 0.0625f;

                        switch (iblockstate.getValue(BlockLadder.FACING)) {
                            case NORTH:
                                event.setCollisionBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F)
                                        .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                                break;

                            case SOUTH:
                                event.setCollisionBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f)
                                        .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                                break;

                            case WEST:
                                event.setCollisionBoundingBox(new AxisAlignedBB(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                                        .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                                break;

                            case EAST:
                            default:
                                event.setCollisionBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F)
                                        .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMouseOver(final MouseOverEvent event) {
        if (hitbox.isEnabled()) {
            if (ViaMCP.NATIVE_VERSION > ProtocolVersion.v1_8.getVersion()) {
                event.setExpand(event.getExpand() - 0.1f);
            }
        }
    }

    @Override
    public void onMinimumMotion(final MinimumMotionEvent event) {
        if (motion.isEnabled()) {
            if (ViaMCP.NATIVE_VERSION > ProtocolVersion.v1_8.getVersion()) {
                event.setMinimumMotion(0.003D);
            }
        }
    }

}