package minvention.content;

import mindustry.content.Liquids;
import minvention.audiovisual.MinventionPal;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

import static minvention.MinventionVars.*;

public class MinventionLiquids {
    public static Liquid
        // Gas
        steam;

    public static void load() {
        steam = new Liquid("gas-steam", MinventionPal.steamFront) {{
            gas = true;
            heatCapacity = Liquids.water.heatCapacity * waterPerSteam;
            temperature = 0.8f;
            effect = StatusEffects.wet;
        }};
    }
}
