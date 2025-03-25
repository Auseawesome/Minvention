package minvention.content;

import arc.struct.Seq;
import mindustry.world.Block;
import minvention.content.blocks.*;

public class MinventionBlocks {
	public static Seq<Block> ivanorBlocks = new Seq<Block>();

	public static void load() {
		MinventionBlocksProduction.load();
	}
}