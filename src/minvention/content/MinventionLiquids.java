package minvention.content;

import minvention.audiovisual.MinventionPal;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

public class MinventionLiquids {
    public static Liquid
        // Gas
        steam;

    public static void load() {
        steam = new Liquid("gas-steam", MinventionPal.steamFront) {{
            gas = true;
            alwaysUnlocked = true;
            explosiveness = 0f;
            temperature = 0.6f;
            effect = StatusEffects.wet;
        }};
    }
}
