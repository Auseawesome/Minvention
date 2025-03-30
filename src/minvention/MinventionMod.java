package minvention;

import mindustry.game.Team;
import minvention.content.*;
import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import minvention.maps.planet.MinventionPlanetGenerator;

public class MinventionMod extends Mod{

    public MinventionMod(){
        Log.info("Loaded Minvention");
    }

    @Override
	public void loadContent() {
		MinventionItems.load();
        MinventionLiquids.load();
		MinventionBlocks.load();
        MinventionStatusEffects.load();

        MinventionTechTree.load();
        Planets.serpulo.generator = new MinventionPlanetGenerator();
        Planets.serpulo.ruleSetter = r -> {
            r.waveTeam = Team.crux;
            r.placeRangeCheck = false;
            r.showSpawns = false;
            r.coreDestroyClear = true;
            r.hideBannedBlocks = true;
            r.bannedBlocks.add(Blocks.mechanicalDrill);
        };
        MinventionSectorPresets.load();
	}

}
