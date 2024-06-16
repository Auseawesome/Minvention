package minvention.content;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.type.Item;

public class UAWItems {
	public static Item placeholder,
	// Natural
	snow, ice;
    
    public static void load() {
		snow = new Item("item-snow", Color.valueOf("e1e9f0")) {{
		}};
        ice = new Item("item-ice", Color.valueOf("c2bffb")) {{
		}};
	}
}