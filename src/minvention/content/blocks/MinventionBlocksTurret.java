package minvention.content.blocks;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import minvention.content.MinventionItems;

import static mindustry.content.Items.*;
import static minvention.content.MinventionItems.*;
import static mindustry.type.ItemStack.*;

public class MinventionBlocksTurret {
    public static void modifyAmmo(Block turret_block, Object... objects) {
        if (turret_block instanceof ItemTurret turret) {
            turret.ammo(objects);
        }
    }

    public static void load() {
        Blocks.duo.requirements(Category.turret, with(
                iron, 30,
                copper, 5
        ));
        modifyAmmo(Blocks.duo,
                MinventionItems.iron, new BasicBulletType(2.5f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;
                    ammoMultiplier = 2;
                }},
                MinventionItems.snow, new BasicBulletType(2f, 0) {{
                    width = 7f;
                    height = 7f;
                    lifetime = 60f;
                    ammoMultiplier = 1;
                    status = StatusEffects.slow;
                    statusDuration = 60f;
                    backColor = Color.valueOf("d2e6f5");
                    frontColor = Color.valueOf("f3f5ff");
                }}
        );
        Blocks.scatter.requirements(Category.turret, with(
                iron, 100,
                copper, 30
        ));
    }
}
