package dev.risas.module.impl.combat;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.PacketUtil;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "AutoHead", description = "Eats heads when your health is getting low", category = Category.COMBAT)
public class AutoHead extends Module {

    private final NumberSetting health = new NumberSetting("Health", this, 15, 0, 20, 1);
    private final NumberSetting minDelay = new NumberSetting("Min Delay", this, 300, 0, 1000, 25);
    private final NumberSetting maxDelay = new NumberSetting("Max Delay", this, 500, 0, 1000, 25);

    private final TimeUtil timer = new TimeUtil();
    private boolean switchBack;
    private long decidedTimer;
    private int head = -37;

    @Override
    protected void onDisable() {
        switchBack = false;
        head = -37;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (switchBack) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            switchBack = false;
            return;
        }

        if (timer.hasReached(decidedTimer)) {
            if (mc.thePlayer.ticksExisted > 10 && mc.thePlayer.getHealth() < health.getValue()) {
                head = PlayerUtil.findHead() - 36;

                if (head != -37) {
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(head));
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(head)));
                    switchBack = true;
                }

                final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
                final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));

                decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
                timer.reset();
            }
        }
    }
}
