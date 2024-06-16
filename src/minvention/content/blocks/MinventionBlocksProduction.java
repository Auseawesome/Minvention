package minvention.content.blocks;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import multicraft.*;

public class UAWBlocksProduction {
	public static Block placeholder,
	// Drills

	// Melters
	combustionMelter, electricMelter;

    public static void load() {

        combustionMelter = new GenericCrafter("combustion-melter") {{
			requirements(Category.crafting, with(
				Items.copper, 40,
				Items.lead, 10
			));
			size = 1;

			hasItems = true;
			hasLiquids = true;
			liquidCapacity = 12;

			craftTime = 600f;
			consumePower(3.5f);
			consumeItems(
				new ItemStack(MinventionItems.snow, 3)
			);
			outputItem = new ItemStack(
				UAWItems.stoutsteel, 3
			);

        electricMelter = new MultiCrafter("electric-melter") {{
            requirements(Category.crafting, with(
                Items.copper, 100,
                Items.lead, 40,
                Items.graphite, 20
            ));
            size = 2;

            hasItems = true;
            hasLiquids = true;
            liquidCapacity = 60;

            resolvedRecipes = Seq.with(
                new Recipe() {{
                    input = new IOEntry() {{
                        items = ItemStack.with(
                            MinventionItems.snow, 1
                        );
                        power = 1
                    }};
                    output = new IOEntry() {{
                        items = FluidStack.with(
                            Liquids.water, 0.5f
                        );
                    }};
                    craftTime = 120f;
                }},
                new Recipe() {{
                    input = new IOEntry() {{
                        items = ItemStack.with(
                            MinventionItems.ice, 1
                        );
                        power = 2;
                    }};
                    output = new IOEntry() {{
                        items = FluidStack.with(
                            Liquids.water, 0.5f
                        );
                    }};
                    craftTime = 600f;
                }}
            );
        }}
    }
}