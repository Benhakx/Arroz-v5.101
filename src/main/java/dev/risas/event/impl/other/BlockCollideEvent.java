package dev.risas.event.impl.other;

import dev.risas.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@Getter
@Setter
@AllArgsConstructor
public final class BlockCollideEvent extends Event {
    private AxisAlignedBB collisionBoundingBox;
    private Block block;
    private final BlockPos blockPos;
    private int x, y, z;
}
