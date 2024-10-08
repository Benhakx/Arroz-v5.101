package dev.risas.event.impl.other;

import dev.risas.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.World;

@Getter
@Setter
@AllArgsConstructor
public class WorldChangedEvent extends Event {
    private World oldWorld, newWorld;
}
