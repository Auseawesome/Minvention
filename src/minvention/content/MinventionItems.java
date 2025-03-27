package minvention.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.Item;

public class MinventionItems {
	public static Item
	// Natural
	snow, ice;
	public static final Seq<Item> ivanorItems = new Seq<Item>();
    
    public static void load() {
		snow = new Item("item-snow", Color.valueOf("e1e9f0"));
        ice = new Item("item-ice", Color.valueOf("c2bffb"));

		ivanorItems.addAll(new Item[]{Items.copper,Items.lead,snow,ice});
	}
}