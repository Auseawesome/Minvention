package minvention;

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

        MinventionTechTree.load();
        Planets.serpulo.generator = new MinventionPlanetGenerator();
        MinventionSectorPresets.load();
	}

}
