package minvention.content.blocks;

import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import minvention.content.MinventionItems;

public class MinventionBlocksEnvironment {
    public static Block
        // Ores
        oreIron
    ;

    public static void load() {
        Blocks.snow.itemDrop = MinventionItems.snow;
        Blocks.iceSnow.itemDrop = MinventionItems.ice;
        Blocks.ice.itemDrop = MinventionItems.ice;

        oreIron = new OreBlock("ore-iron") {{
            itemDrop = MinventionItems.iron;
            oreDefault = true;
            oreThreshold = 0.815f;
            oreScale = 23.7621f;
        }};
    }
}
