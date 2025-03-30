package minvention.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;

public class MinventionStatusEffects {
    public static StatusEffect chilly;

    public static void load() {
        chilly = new StatusEffect("chilly") {{
            color = Color.valueOf("d2e6f5");
            speedMultiplier = 0.8f;
            effect = Fx.freezing;
            init(() -> {
                opposite(StatusEffects.melting,StatusEffects.burning);

                affinity(StatusEffects.freezing, (unit, result, time) -> {
                    result.set(StatusEffects.freezing, Math.max(time,result.time));
                });
            });
        }};
    }
}
