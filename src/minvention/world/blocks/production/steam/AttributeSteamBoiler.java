package minvention.world.blocks.production.steam;

import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.meta.Stat;

public class AttributeSteamBoiler extends AttributeCrafter {
	public AttributeSteamBoiler(String name) {
		super(name);
		warmupSpeed = 0.0025f;
		craftTime = 120f;
		hasItems = false;
		hasLiquids = true;
		squareSprite = false;
		acceptsItems = false;
		minEfficiency = 0.1f;
		baseEfficiency = 0f;
		boostScale = 0.25f;
		maxBoost = 3f;
		scaleLiquidConsumption = true;
	}
}
