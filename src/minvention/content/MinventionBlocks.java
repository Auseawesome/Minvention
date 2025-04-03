package minvention.content;

import mindustry.content.Items;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import minvention.content.blocks.*;

public class MinventionBlocks {
    // Performs a basic item to item conversion to new replacement items
    // Only use for quick compatibility, not ideal
    public static ItemStack[] convertItems(ItemStack[] items) {
        for (ItemStack itemStack : items) {
            if (itemStack.item == Items.copper) {
                itemStack.item = MinventionItems.iron;
            } else if (itemStack.item == Items.graphite) {
                itemStack.item = MinventionItems.refinedCarbon;
            }
        }
        return items;
    }

    // Performs a basic item to item conversion to hopefully make a block properly obtainable in the new campaign
    // Only use for quick compatibility, not ideal
    public static void convertRequirements(Block block) {
        block.requirements = convertItems(block.requirements);
    };

    public static void load() {
        MinventionBlocksProduction.load();
        MinventionBlocksDistribution.load();
        MinventionBlocksEnvironment.load();
        MinventionBlocksTurret.load();
        MinventionBlocksDefense.load();
        MinventionBlocksPower.load();
    }
}