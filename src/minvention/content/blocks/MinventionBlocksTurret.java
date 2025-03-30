package minvention.content.blocks;

import mindustry.content.Blocks;
import mindustry.type.Category;

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static mindustry.type.ItemStack.*;

public class MinventionBlocksTurret {
    public static void load() {
        Blocks.duo.requirements(Category.turret, with(
                iron, 30,
                copper, 5
        ));
        Blocks.scatter.requirements(Category.turret, with(
                iron, 100,
                copper, 30
        ));
    }
}
