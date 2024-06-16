const ivanor_gen = extend(SerpuloPlanetGenerator, {});
const grassa = Vars.content.block("aeyama-floor-grassa");
const grassb = Vars.content.block("aeyama-floor-grassb");
const grassc = Vars.content.block("aeyama-floor-grassc");
ivanor_gen.rid = new Packages.arc.util.noise.RidgedPerlin(1, 2);
ivanor_gen.basegen = new BaseGenerator();
ivanor_gen.scl = 5;
ivanor_gen.waterOffset = 0.07;
ivanor_gen.water = 2 / ivanor_gen.arr[0].length;
Events.on(ContentInitEvent, e => {
    Vars.content.planet("ivanor").generator = ivanor_gen();
    Vars.content.planet("ivanor").meshLoader = () => new HexMesh(Vars.content.planet("ivanor"), 6);
});
//endregion planet Ivanor

exports.gen = ivanor_gen;