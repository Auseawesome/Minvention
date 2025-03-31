package minvention.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.gen.Building;
import mindustry.world.Block;
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

    public class WireBuild extends Building {

        int currentRegion = 0;
        @Override
        public void draw() {
            Draw.rect(wireRegions[currentRegion],x,y);
        }

        @Override
        public void onProximityUpdate() {
            currentRegion = 0;
            if (tile.nearbyBuild(0) != null) {
                currentRegion += tile.nearbyBuild(0).block.hasPower ? 1 : 0;
            }
            if (tile.nearbyBuild(1) != null) {
                currentRegion += tile.nearbyBuild(1).block.hasPower ? 2 : 0;
            }
            if (tile.nearbyBuild(2) != null) {
                currentRegion += tile.nearbyBuild(2).block.hasPower ? 4 : 0;
            }
            if (tile.nearbyBuild(3) != null) {
                currentRegion += tile.nearbyBuild(3).block.hasPower ? 8 : 0;
            }
        }
    }
}
