package minvention.content;

import mindustry.type.SectorPreset;

import static mindustry.content.Planets.*;

public class MinventionSectorPresets {
    public static SectorPreset groundZero;

    public static void load() {
        groundZero = new SectorPreset("groundZero", serpulo, 15){{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
        }};
    }
}
