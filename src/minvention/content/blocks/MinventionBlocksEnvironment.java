package minvention.content.blocks;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import minvention.content.MinventionItems;

public class MinventionBlocksEnvironment {
    public static Block
            // Ores
            oreIron
    ;

    public static void load() {
        oreIron = new OreBlock("ore-iron") {{
            itemDrop = MinventionItems.iron;
            oreDefault = true;
            oreThreshold = 0.815f;
            oreScale = 23.7621f;
        }};
    }
}
