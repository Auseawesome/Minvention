package minvention.world.consumers;

import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.consumers.ConsumeLiquid;

public class ConsumeLiquidScaling extends ConsumeLiquid {
    public ConsumeLiquidScaling(Liquid liquid, float amount) {
        super(liquid, amount);
    }

    @Override
    public void update(Building build) {
        build.liquids.remove(this.liquid, this.amount * build.edelta() * this.multiplier.get(build) * build.getProgressIncrease(build.edelta()));
    }
}
