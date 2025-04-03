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
        Blocks.conveyor.requirements = with(
                iron, 1
        );
        Blocks.conveyor.researchCost = with(iron, 5);
        Blocks.junction.requirements = with(
                iron, 2
        );
        Blocks.itemBridge.requirements = with(
                iron, 6,
                copper, 6
        );
        Blocks.sorter.requirements = with(
                iron, 2,
                copper, 2
        );
        Blocks.invertedSorter.requirements = with(
                iron, 2,
                copper, 2
        );
        Blocks.router.requirements = with(
                iron, 2,
                copper, 1
        );
        Blocks.overflowGate.requirements = with(
                iron, 2,
                copper, 2
        );
        Blocks.underflowGate.requirements = with(
                iron, 2,
                copper, 2
        );
    }
}
