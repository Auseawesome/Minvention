package minvention.content;

import arc.Core;
import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.Item;

public class MinventionItems {
    public static Item
        // Metals
        iron,

        // Refined Resources
        refinedCarbon,

        // Natural
        snow, ice;

    public static void load() {
        // Increase copper's cost to be same as lead as it is now primarily a secondary material
        Items.copper.cost = 0.7f;

        iron = new Item("iron", Color.valueOf("888faa")) {{
            hardness = 1;
            cost = 0.5f;
            alwaysUnlocked = true;
        }};
        refinedCarbon = new Item("refined-carbon", Color.valueOf("54545e")) {{
            cost = 1f;
        }};
        snow = new Item("snow", Color.valueOf("e1e9f0")) {{
            lowPriority = true;
            buildable = false;
        }};
        ice = new Item("ice", Color.valueOf("c2bffb")) {{
            hardness = 3;
            lowPriority = true;
            buildable = false;
        }};
    }
}