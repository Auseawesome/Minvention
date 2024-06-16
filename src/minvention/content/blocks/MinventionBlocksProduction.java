package minvention.content.blocks;

import minvention.content.*;
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

import static mindustry.Vars.tilesize;
import static mindustry.type.ItemStack.with;

public class MinventionBlocksProduction {
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
			outputLiquid = new LiquidStack(
				Liquids.water, 0.1f
			);
        }};

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
                    input = new IOEntry(
                        Seq.with(ItemStack.with(MinventionItems.snow, 1)),
                        Seq.with(),
                        1f
                    );
                    output = new IOEntry(
                        Seq.with(),
                        Seq.with(LiquidStack.with(Liquids.water, 0.5f))
                    );
                    craftTime = 120f;
                }},
                new Recipe() {{
                    input = new IOEntry(
                        Seq.with(ItemStack.with(MinventionItems.ice, 1)),
                        Seq.with(),
                        2f
                    );
                    output = new IOEntry(
                        Seq.with(),
                        Seq.with(LiquidStack.with(Liquids.water, 0.5f))
                    );
                    craftTime = 600f;
                }}
            );
        }};
    }
}