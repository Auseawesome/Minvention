package minvention.content;

import arc.struct.*;
import mindustry.game.Objectives.*;
import minvention.content.MinventionPlanets;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import minvention.content.blocks.MinventionBlocksDistribution;
import minvention.content.blocks.MinventionBlocksProduction;

import static mindustry.Vars.*;
import static mindustry.content.TechTree.*;

public class IvanorTechTree{
    public static void load(){

        Seq<Objective> ivanorSector = Seq.with(new OnPlanet(MinventionPlanets.ivanor));

        MinventionPlanets.ivanor.techTree = nodeRoot("ivanor", Blocks.coreShard, true, () -> {
            node(MinventionBlocksDistribution.mechanicalConveyor, ivanorSector, () -> {

            });
            node(MinventionBlocksProduction.combustionMelter, ivanorSector, () -> {
                node(MinventionBlocksProduction.electricMelter);
            });

            nodeProduce(Items.copper, () -> {
                nodeProduce(Items.lead, () -> {} );
            });

            nodeProduce(MinventionItems.snow, () -> {
                nodeProduce(Liquids.water, () -> {} );
            });
        });
    }
}