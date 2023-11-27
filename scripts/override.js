let drill = Blocks.pneumaticDrill;
drill.liquidBoostIntensity = 1;
drill.consumers = {} + new ConsumeLiquid(water, 0.6).optional(false,false)