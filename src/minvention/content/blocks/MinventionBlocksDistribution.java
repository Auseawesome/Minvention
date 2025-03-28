package minvention.content.blocks;

import mindustry.content.Items;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import minvention.content.MinventionBlocks;

import static mindustry.type.ItemStack.*;

public class MinventionBlocksDistribution {
    public static Block
            // Conveyors
            mechanicalConveyor,

            // Routers
            mechanicalRouter

    ;

    public static void load(){
        mechanicalConveyor = new Conveyor("mechanical-conveyor") {{
            requirements(Category.distribution, with(Items.copper,1));
            health = 45;
            speed = 0.03f;
            displayedSpeed = 4.2f;
            buildCostMultiplier = 2f;
            researchCost = with(Items.copper, 5);
        }};

        mechanicalRouter = new Router("mechanical-router") {{
            requirements(Category.distribution, with(Items.copper,3));
            buildCostMultiplier = 4f;
        }};

        MinventionBlocks.ivanorBlocks.addAll(new Block[]{mechanicalConveyor});
    }
}
