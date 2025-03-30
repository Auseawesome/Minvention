package minvention.content;

import arc.struct.Seq;
import mindustry.world.Block;
import minvention.content.blocks.*;

public class MinventionBlocks {

	public static void load() {
		MinventionBlocksProduction.load();
		MinventionBlocksDistribution.load();
		MinventionBlocksEnvironment.load();
		MinventionBlocksTurret.load();
	}
}