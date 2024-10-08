/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.combat;

import dev.risas.Rise;
import dev.risas.event.impl.other.StrafeEvent;
import dev.risas.event.impl.render.Render3DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.player.MoveUtil;
import dev.risas.util.player.PlayerUtil;
import dev.risas.util.render.RenderUtil;
import dev.risas.util.render.theme.ThemeType;
import dev.risas.util.render.theme.ThemeUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

@ModuleInfo(name = "TargetStrafe", description = "Strafes around your target", category = Category.COMBAT)
public final class TargetStrafe extends Module {
    private Entity target;
    private float distance;
    private boolean direction, reset;
    private int ticksSinceLastInverted;

    public final NumberSetting range = new NumberSetting("Range", this, 3, 1, 6, 0.1);
    public final BooleanSetting thirdPerson = new BooleanSetting("Third Person", this, false);
    public final BooleanSetting controllable = new BooleanSetting("Controllable", this, true);
    public final BooleanSetting jumpOnly = new BooleanSetting("Jump Only", this, false);

    @Override
    protected void onDisable() {
        EntityPlayer.movementYaw = null;
        ticksSinceLastInverted = 0;
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        if (!mc.gameSettings.keyBindJump.isKeyDown() && jumpOnly.isEnabled()) {
            if (reset) {
                mc.gameSettings.thirdPersonView = 0;
                reset = false;
                }
                EntityPlayer.movementYaw = null;
                return;
            }

            final float range = (float) this.range.getValue();
            target = Aura.target;
            if (target == null) {
                if (reset) {
                    mc.gameSettings.thirdPersonView = 0;
                    reset = false;
                }
                EntityPlayer.movementYaw = null;
                return;
            }

            final float yaw = getYaw();

            distance = mc.thePlayer.getDistanceToEntity(target);

            if (thirdPerson.isEnabled()) {
                mc.gameSettings.thirdPersonView = 1;
                reset = true;
            }

            final double moveDirection = MoveUtil.getDirection(EntityPlayer.movementYaw == null ? yaw : EntityPlayer.movementYaw);
            final double posX = -Math.sin(moveDirection) * MoveUtil.getSpeed() * 5;
            final double posZ = Math.cos(moveDirection) * MoveUtil.getSpeed() * 5;
            if (!(PlayerUtil.getBlockRelativeToPlayer(posX, 0, posZ) instanceof BlockAir) || mc.thePlayer.isCollidedHorizontally || (!PlayerUtil.isBlockUnder(posX, posZ) && !Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Fly")).isEnabled()) && ticksSinceLastInverted > 1) {
                ticksSinceLastInverted = 0;
                direction = !direction;
            } else
                ticksSinceLastInverted++;

            if (controllable.isEnabled()) {
                if (mc.gameSettings.keyBindRight.isKeyDown())
                    direction = true;
                else if (mc.gameSettings.keyBindLeft.isKeyDown())
                    direction = false;
            }

            if (distance > range) {
                EntityPlayer.movementYaw = yaw;
            } else {
                if (direction) {
                    EntityPlayer.movementYaw = yaw + 78 + (distance - range) * 2;
                } else {
                    EntityPlayer.movementYaw = yaw - 78 - (distance - range) * 2;
                }
            }
        }

        @Override
        public void onRender3DEvent(final Render3DEvent event) {
            if (target == null) return;

            if (mc.gameSettings.keyBindJump.isKeyDown() || !jumpOnly.isEnabled())
                circle(target, range.getValue() - 0.5, ThemeUtil.getThemeColor(0, ThemeType.GENERAL, 0.5f));
        }

        private float getYaw() {
            final double x = (target.posX - (target.lastTickPosX - target.posX)) - mc.thePlayer.posX;
            final double z = (target.posZ - (target.lastTickPosZ - target.posZ)) - mc.thePlayer.posZ;

            return (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0F);
        }

        private void circle(final Entity entity, final double rad, Color color) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDepthMask(false);
            GlStateManager.disableCull();
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
            final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY) + 0.01;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

            for (int i = 0; i <= 90; ++i) {
                RenderUtil.color(color);

                GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GlStateManager.enableCull();
            GL11.glDisable(2848);
            GL11.glDisable(2848);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GL11.glColor3f(255, 255, 255);
        }
    }

