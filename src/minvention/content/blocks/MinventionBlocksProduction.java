package minvention.content.blocks;

import mindustry.world.blocks.production.Drill;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import minvention.world.blocks.production.FilterGenericCrafter;
import minvention.world.consumers.ConsumeItemFuelFlammable;
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

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static minvention.Vars.*;
import static mindustry.type.ItemStack.with;

public class MinventionBlocksProduction {
	public static Block
			// Drills
			mechanicalDrill,

			// Melters
			combustionMelter, electricMelter,

    		// Steam Production
    		steamKettle, pressureBoiler, industrialBoiler, geothermalBoiler;

    public static void load() {
		// Drills
		mechanicalDrill = new Drill("mechanical-drill") {{
			requirements(Category.production, with(
					iron, 8,
					copper, 4
			));
			tier = 2;
			drillTime = 600;
			size = 2;
			envEnabled ^= Env.space;
			researchCost = with(iron, 10);
			liquidBoostIntensity = 1f;
			liquidCapacity = 0f;
			hasLiquids = false;
		}};

		// Melters
		combustionMelter = new FilterGenericCrafter("combustion-melter") {{
			requirements(Category.crafting, with(
					copper, 40,
					lead, 10
			));
			size = 1;

			hasItems = true;
			hasLiquids = true;
			liquidCapacity = 12;

			craftTime = 120f;
			consume(new ConsumeItemFuelFlammable());
			consumeItems(
					new ItemStack(snow, 4)
			);
			outputLiquid = new LiquidStack(
					Liquids.water, waterPerSnow * 4 / craftTime * waterBasicEff
			);
		}};

		electricMelter = new MultiCrafter("electric-melter") {{
			requirements(Category.crafting, with(
					copper, 100,
					lead, 40,
					graphite, 20
			));
			size = 2;

			hasItems = true;
			hasLiquids = true;
			liquidCapacity = 60;

			resolvedRecipes = Seq.with(
					new Recipe() {{
						craftTime = 20f;
						input = new IOEntry(
								Seq.with(ItemStack.with(snow, 1)),
								Seq.with(),
								0.5f
						);
						output = new IOEntry(
								Seq.with(),
								Seq.with(LiquidStack.with(Liquids.water, waterPerSnow / craftTime * waterAdvancedEff))
						);
					}},
					new Recipe() {{
						craftTime = 30f;
						input = new IOEntry(
								Seq.with(ItemStack.with(ice, 1)),
								Seq.with(),
								1f
						);
						output = new IOEntry(
								Seq.with(),
								Seq.with(LiquidStack.with(Liquids.water, waterPerIce / craftTime * waterAdvancedEff))
						);
					}}
			);
		}};

		// Steam Production
		steamKettle = new SteamBoiler("steam-kettle") {{
			requirements(Category.crafting, with(
					copper, 12,
					lead, 12
			));
			size = 1;

			float baseWaterInput = 0.1f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam * waterCrudeEff);
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
					copper, 55,
					graphite, 35,
					silicon, 35,
					lead, 35
			));
			size = 2;

			float baseWaterInput = 0.5f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam * waterBasicEff);
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
					copper, 125,
					lead, 100,
					titanium, 85,
					metaglass, 65,
					silicon, 65,
					graphite, 60
			));
			size = 4;

			float baseWaterInput = 2f;
			consume(new ConsumeLiquidScaling(Liquids.water, baseWaterInput));
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam * waterAdvancedEff);
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
					copper, 70,
					graphite, 45,
					silicon, 40,
					lead, 50,
					metaglass, 30
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
			outputLiquid = new LiquidStack(MinventionLiquids.steam, baseWaterInput / waterPerSteam * waterAdvancedEff);
		}};
	}
}