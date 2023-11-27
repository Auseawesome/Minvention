function drillRequireLiquid(drill,liquids) {
    drill.liquidBoostIntensity = 1;
    drill.consumers = drill.nonOptionalConsumers = liquids;
    drill.optionalConsumers = [];
}



Events.on(ClientLoadEvent, e => {
    drillRequireLiquid(Blocks.mechanicalDrill, [])
    drillRequireLiquid(Blocks.pneumaticDrill, [new ConsumeLiquid(Liquids.water, 0.06)])
    drillRequireLiquid(Blocks.laserDrill, [new ConsumeLiquid(Liquids.water, 0.08)])
    drillRequireLiquid(Blocks.airblastDrill, [new ConsumeLiquid(Liquids.water, 0.1)])
})