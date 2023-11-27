let drill = Blocks.pneumaticDrill;
drill.liquidBoostIntensity = 1;
drill.consumers = [];
drill.consumer = [new ConsumeLiquid(Liquids.water, 0.2).optional(false)]