package minvention.content;

import mindustry.content.Planets;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import minvention.content.blocks.MinventionBlocksDefense;
import minvention.content.blocks.MinventionBlocksDistribution;
import minvention.content.blocks.MinventionBlocksProduction;

import static mindustry.content.TechTree.*;

public class MinventionTechTree{
    public static void load(){

        Planets.serpulo.techTree = nodeRoot("ivanor", Blocks.coreShard, true, () -> {
            node(Blocks.conveyor, () -> {
                node(Blocks.router, () -> {
                    node(Blocks.overflowGate, () -> {
                        node(Blocks.underflowGate, () -> {

                        });
                    });
                    node(Blocks.sorter, () -> {
                        node(Blocks.invertedSorter, () -> {

                        });
                    });
                });
                node(Blocks.junction, () -> {
                    node(Blocks.itemBridge, () -> {

                    });
                });
            });
            node(MinventionBlocksProduction.mechanicalDrill, () -> {

            });
            node(Blocks.duo, () -> {
                node(Blocks.scatter);
                node(MinventionBlocksDefense.ironWall, () -> {
                    node(MinventionBlocksDefense.ironWallLarge);
                });
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