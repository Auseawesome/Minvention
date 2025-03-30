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
        placeholder;

    public static void load(){
        Blocks.conveyor.requirements(Category.distribution, with(
                iron, 1
        ));
        Blocks.conveyor.researchCost = with(iron, 5);
        Blocks.junction.requirements(Category.distribution, with(
                iron, 2
        ));
        Blocks.itemBridge.requirements(Category.distribution, with(
                iron, 6,
                copper, 6
        ));
        Blocks.sorter.requirements(Category.distribution, with(
                iron, 2,
                copper, 2
        ));
        Blocks.invertedSorter.requirements(Category.distribution, with(
                iron, 2,
                copper, 2
        ));
        Blocks.router.requirements(Category.distribution, with(
                iron, 2,
                copper, 1
        ));
        Blocks.overflowGate.requirements(Category.distribution, with(
                iron, 2,
                copper, 2
        ));
        Blocks.underflowGate.requirements(Category.distribution, with(
                iron, 2,
                copper, 2
        ));
    }
}
