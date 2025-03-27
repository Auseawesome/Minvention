package minvention.content.blocks;

import minvention.world.consumers.ConsumeLiquidScaling;
import minvention.world.drawer.DrawBoilerSmoke;
import minvention.audiovisual.MinventionPal;
import minvention.content.*;
import minvention.world.blocks.production.steam.AttributeSteamBoiler;
import minvention.world.blocks.production.steam.SteamBoiler;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;
import multicraft.*;

import static minvention.Vars.*;
import static mindustry.type.ItemStack.with;

public class MinventionBlocksProduction {
	public static Block
	// Drills

	// Melters
	combustionMelter, electricMelter,

    // Steam Production
    steamKettle, pressureBoiler, industrialBoiler, geothermalBoiler;

    public static void load() {

        // Melters
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

        // Steam Production
        steamKettle = new SteamBoiler("steam-kettle") {{
			requirements(Category.crafting, with(
				Items.copper, 12,
				Items.lead, 12
			));
			size = 1;

			float baseWaterInput = 0.25f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam);
			liquidCapacity = 180f;

			squareSprite = false;
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawWarmupRegion(),
				new DrawBoilerSmoke() {{
					size = 1.5f;
					particles = 18;
					lifetime = 150f;
					spreadRadius = 3f;
				}}
			);

		}};
		industrialBoiler = new SteamBoiler("industrial-boiler") {{
			requirements(Category.crafting, with(
				Items.copper, 55,
				Items.graphite, 35,
				Items.silicon, 35,
				Items.lead, 35
			));
			size = 2;

			float baseWaterInput = 0.5f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam);
			liquidCapacity = steamKettle.liquidCapacity * 2;

			squareSprite = false;
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawWarmupRegion() {{
					sinMag = 0.4f;
					sinScl = sinMag * 15f;
				}},
				new DrawBoilerSmoke() {{
					particles = 28;
					lifetime = 160f;
					size = 2.5f;
					spreadRadius = 5f;
				}}
			);
		}};
		pressureBoiler = new SteamBoiler("pressure-boiler") {{
			requirements(Category.crafting, with(
				Items.copper, 125,
				Items.lead, 100,
				Items.titanium, 85,
				Items.metaglass, 65,
				Items.silicon, 65,
				Items.graphite, 60
			));
			size = 4;

			float baseWaterInput = 2f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam);
			liquidCapacity = steamKettle.liquidCapacity * 4;

			squareSprite = false;
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawGlowRegion() {{
					glowScale = 3f;
					color = MinventionPal.drawGlowOrange;
				}},
				new DrawBoilerSmoke() {{
					particles = 40;
					lifetime = 210f;
					size = 5f;
					spreadRadius = 14f;
				}}
			);
		}};
		geothermalBoiler = new AttributeSteamBoiler("geothermal-boiler") {{
			requirements(Category.crafting, with(
				Items.copper, 70,
				Items.graphite, 45,
				Items.silicon, 40,
				Items.lead, 50,
				Items.metaglass, 30
			));
			size = 3;
			updateEffect = Fx.steam;
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawGlowRegion() {{
					glowScale = 3f;
					color = MinventionPal.drawGlowOrange;
				}}
			);
			attribute = Attribute.heat;
			float baseWaterInput = 1.5f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam);
		}};
		MinventionBlocks.ivanorBlocks.addAll(new Block[]{combustionMelter, electricMelter, steamKettle, pressureBoiler, industrialBoiler, geothermalBoiler});
    }
}