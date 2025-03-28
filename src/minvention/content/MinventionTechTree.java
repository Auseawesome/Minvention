package minvention.content;

import mindustry.content.Planets;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import minvention.content.blocks.MinventionBlocksDistribution;
import minvention.content.blocks.MinventionBlocksProduction;

import static mindustry.content.TechTree.*;

public class MinventionTechTree{
    public static void load(){

        Planets.serpulo.techTree = nodeRoot("ivanor", Blocks.coreShard, true, () -> {
            node(Blocks.conveyor, () -> {
                node(Blocks.router, () -> {

                });
            });
            node(MinventionBlocksProduction.combustionMelter, () -> {
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