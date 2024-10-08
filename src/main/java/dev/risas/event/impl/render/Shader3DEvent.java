package dev.risas.event.impl.render;

import dev.risas.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Shader3DEvent extends Event {
    private final float partialTicks;
}
