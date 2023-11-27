let drill = Blocks.pneumaticDrill;
drill.liquidBoostIntensity = 1;
drill.consumes = [];
drill.consumes = [new ConsumeLiquid(Liquids.water, 0.2).optional(false)]