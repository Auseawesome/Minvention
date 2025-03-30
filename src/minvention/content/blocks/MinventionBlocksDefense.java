package minvention.content.blocks;

import mindustry.content.Blocks;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static mindustry.type.ItemStack.*;

public class MinventionBlocksDefense {
    public static Block
            ironWall, ironWallLarge
    ;

    public static void load() {

        int wallHealthMultiplier = 4;

        ironWall = new Wall("iron-wall"){{
            requirements(Category.defense, with(iron, 6));
            health = 80 * wallHealthMultiplier;
            researchCostMultiplier = 0.1f;
        }};

        ironWallLarge = new Wall("iron-wall-large"){{
            requirements(Category.defense, mult(ironWall.requirements, 4));
            size = 2;
            health = 80 * size * size * wallHealthMultiplier;
        }};
    }
}
