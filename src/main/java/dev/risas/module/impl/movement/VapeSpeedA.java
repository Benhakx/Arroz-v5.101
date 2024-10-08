package dev.risas.module.impl.movement;

import dev.risas.module.Module;
import dev.risas.setting.impl.NumberSetting;

public class VapeSpeedA extends Module {
    private NumberSetting steps = new NumberSetting("Steps", this, 15, 43.5, 180, 0.1);

    private double C, z;
    private float v;
    private int x = 0;
}
