package minvention.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.Item;

public class MinventionItems {
	public static Item
			// Metals
			iron,

			// Natural
			snow, ice;
    
    public static void load() {
		snow = new Item("item-snow", Color.valueOf("e1e9f0")) {{
			lowPriority = true;
			buildable = false;
		}};
        ice = new Item("item-ice", Color.valueOf("c2bffb")) {{
			hardness = 3;
			lowPriority = true;
			buildable = false;
		}};
	}
}