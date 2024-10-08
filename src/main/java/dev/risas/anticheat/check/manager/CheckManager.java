package dev.risas.anticheat.check.manager;

import dev.risas.anticheat.check.Check;
import dev.risas.anticheat.check.impl.combat.autoclicker.AutoClickerA;
import dev.risas.anticheat.check.impl.combat.keepsprint.KeepSprintA;
import dev.risas.anticheat.check.impl.movement.other.Omnisprint;
import dev.risas.anticheat.check.impl.movement.speed.SpeedA;
import dev.risas.anticheat.check.impl.other.eagle.EagleA;
import dev.risas.anticheat.check.impl.other.groundspoof.GroundSpoofA;
import dev.risas.anticheat.check.impl.other.groundspoof.GroundSpoofB;
import dev.risas.anticheat.check.impl.other.invalid.InvalidA;
import dev.risas.anticheat.data.PlayerData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class CheckManager {

    public static final Class<?>[] CHECKS = new Class[]{
            /*
             * COMBAT
             */

            // AutoClicker
            AutoClickerA.class,

            // KeepSprint
            KeepSprintA.class,

            /*
             * MOVEMENT
             */

            // Speed
            SpeedA.class,

            // Fly

            // Other
            Omnisprint.class,

            /*
             * OTHER
             */

            // Eagle
            EagleA.class,

            // GroundSpoof
            GroundSpoofA.class,
            GroundSpoofB.class,

            // Invalid
            InvalidA.class
    };

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static List<Check> loadChecks(final PlayerData data) {
        final List<Check> checkList = new ArrayList<>();

        for (final Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checkList.add((Check) constructor.newInstance(data));
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }

        return checkList;
    }

    public static void setup() {
        for (final Class<?> clazz : CHECKS) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
