let drill = Blocks.pneumaticDrill;
drill.consumers = drill.nonOptionalConsumers = [new ConsumeLiquid(Liquids.water, 0.2)];
drill.optionalConsumers = [];