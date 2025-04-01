package minvention.world.blocks.power;

import arc.Core;
import arc.func.Cons;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import arc.util.Log;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.Autotiler;
import mindustry.world.blocks.power.PowerDistributor;

public class Wire extends PowerDistributor {

    TextureRegion[] wireRegions = new TextureRegion[16];
    public Wire(String name){
        super(name);
        conveyorPlacement = true;
        solid = false;
        squareSprite = false;
    }

    @Override
    public void load() {
        super.load();
        for (int i = 0; i < 16; i++) {
            wireRegions[i] = Core.atlas.find(name+"-"+i);
        }
    }

    public static int getRegionFromNeighbours(Block[] neighbours) {
        int region = 0;
        if (neighbours[0] != null) {
            region += neighbours[0].hasPower ? 1 : 0;
        }
        if (neighbours[1] != null) {
            region += neighbours[1].hasPower ? 2 : 0;
        }
        if (neighbours[2] != null) {
            region += neighbours[2].hasPower ? 4 : 0;
        }
        if (neighbours[3] != null) {
            region += neighbours[3].hasPower ? 8 : 0;
        }
        return region;
    }

    public static int getRegionFromNeighbours(Building[] neighbours) {
        Block[] blockNeighbours = new Block[4];
        if (neighbours[0] != null) {
            blockNeighbours[0] = neighbours[0].block;
        }
        if (neighbours[1] != null) {
            blockNeighbours[1] = neighbours[1].block;
        }
        if (neighbours[2] != null) {
            blockNeighbours[2] = neighbours[2].block;
        }
        if (neighbours[3] != null) {
            blockNeighbours[3] = neighbours[3].block;
        }
        return getRegionFromNeighbours(blockNeighbours);
    }

    @Override
    public void loadIcon() {
        super.loadIcon();
        fullIcon = Core.atlas.find(name+"-0");
        uiIcon = Core.atlas.find(name+"-5");
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Block[] neighbours = new Block[4];

        list.each(other -> {
            if (other.x == plan.x + 1 && other.y == plan.y) neighbours[0] = other.block;
            if (other.y == plan.y + 1 && other.x == plan.x) neighbours[1] = other.block;
            if (other.x == plan.x - 1 && other.y == plan.y) neighbours[2] = other.block;
            if (other.y == plan.y - 1 && other.x == plan.x) neighbours[3] = other.block;
        });
        Draw.rect(wireRegions[getRegionFromNeighbours(neighbours)],plan.drawx(),plan.drawy());
    }

    public class WireBuild extends Building {

        int currentRegion = 0;
        @Override
        public void draw() {
            Draw.rect(wireRegions[currentRegion],x,y);
        }

        public Building[] nearbyBuilds() {
            Building[] nearby = new Building[4];
            for (int i = 0; i < 4; i++) {
                nearby[i] = tile.nearbyBuild(i);
            }
            return nearby;
        }

        public int getRegion() {
            return getRegionFromNeighbours(nearbyBuilds());
        }

        @Override
        public void onProximityUpdate() {
            currentRegion = getRegion();
        }
    }
}
