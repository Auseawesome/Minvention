let drill = Blocks.pneumaticDrill;
drill.liquidBoostIntensity = 1;
drill.ConsumeLiquid = new ConsumeLiquid(Liquids.water, 0.2).optional(false,false)