package minvention.world.blocks.production.steam;

import minvention.world.blocks.production.AttributeFilterCrafter;
import mindustry.world.meta.Stat;

public class AttributeSteamBoiler extends AttributeFilterCrafter {
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
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(Stat.productionTime);
	}
}
