package minvention.world.blocks.production.steam;

import minvention.world.blocks.production.FilterGenericCrafter;
import minvention.world.consumers.ConsumeItemFuelFlammable;
import mindustry.content.Fx;
import mindustry.world.consumers.ConsumeItemExplode;
import mindustry.world.meta.Stat;

public class SteamBoiler extends FilterGenericCrafter {

	public SteamBoiler(String name) {
		super(name);
		warmupSpeed = 0.0025f;
		craftTime = 120f;
		hasItems = true;
		hasLiquids = true;
		updateEffect = Fx.steam;
		consume(new ConsumeItemFuelFlammable());
		consume(new ConsumeItemExplode());
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(Stat.productionTime);
	}

}
