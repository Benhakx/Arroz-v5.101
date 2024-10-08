package dev.risas.event.impl.motion;

import dev.risas.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alan
 * @since 13.03.2022
 */
@Getter
@Setter
@AllArgsConstructor
public class MinimumMotionEvent extends Event {
    private double minimumMotion;
}
