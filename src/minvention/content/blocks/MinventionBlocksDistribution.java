package minvention.content.blocks;

import mindustry.content.Blocks;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import minvention.content.MinventionBlocks;

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static mindustry.type.ItemStack.*;

public class MinventionBlocksDistribution {
    public static Block
            // Conveyors
            placeholder

    ;

    public static void load(){
        Blocks.conveyor.requirements(Category.distribution, with(
                iron, 1
        ));
        Blocks.router.requirements(Category.distribution, with(
                iron, 2,
                copper, 1
        ));
    }
}
