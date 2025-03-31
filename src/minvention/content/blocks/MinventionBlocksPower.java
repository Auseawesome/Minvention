package minvention.content.blocks;

import mindustry.content.Blocks;
import mindustry.type.*;
import mindustry.world.*;
import minvention.world.blocks.power.Wire;

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static mindustry.type.ItemStack.*;

public class MinventionBlocksPower {
    public static Block
        // Distributors
        copperWire
    ;

    public static void load() {
        copperWire = new Wire("copper-wire") {{
            requirements(Category.power,with(
                    copper, 1
            ));
        }};
    }
}
