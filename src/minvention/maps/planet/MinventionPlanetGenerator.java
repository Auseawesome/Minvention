package minvention.maps.planet;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Structs;
import arc.util.Tmp;
import arc.util.noise.Noise;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.Vars;
import mindustry.ai.Astar;
import mindustry.ai.BaseRegistry;
import mindustry.content.Blocks;
import mindustry.content.Liquids;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.game.Waves;
import mindustry.graphics.g3d.PlanetGrid;
import mindustry.maps.generators.BaseGenerator;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.TileGen;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;
import minvention.content.blocks.MinventionBlocksEnvironment;

public class MinventionPlanetGenerator extends PlanetGenerator {
    public static boolean alt = false;
    BaseGenerator basegen = new BaseGenerator();
    float scl = 5.0F;
    float waterOffset = 0.07F;
    boolean genLakes = false;
    Block[][] arr;
    ObjectMap<Block, Block> dec;
    ObjectMap<Block, Block> tars;
    float water;

    public MinventionPlanetGenerator() {
        this.arr = new Block[][]{
                {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass},
                {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sand, Blocks.sand, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.stone},
                {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.stone, Blocks.stone},
                {Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.salt, Blocks.salt, Blocks.sand, Blocks.stone, Blocks.grass, Blocks.grass, Blocks.grass, Blocks.iceSnow, Blocks.ice},
                {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.basalt, Blocks.snow, Blocks.grass, Blocks.snow, Blocks.snow, Blocks.ice},
                {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.snow, Blocks.ice},
                {Blocks.deepwater, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.moss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice},
                {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.darksand, Blocks.basalt, Blocks.moss, Blocks.basalt, Blocks.hotrock, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
                {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.moss, Blocks.sporeMoss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
                {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice},
                {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
                {Blocks.taintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.moss, Blocks.sporeMoss, Blocks.iceSnow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
                {Blocks.darksandWater, Blocks.darksand, Blocks.snow, Blocks.ice, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice}
        };
        this.dec = ObjectMap.of(new Object[]{Blocks.sporeMoss, Blocks.sporeCluster, Blocks.moss, Blocks.sporeCluster, Blocks.taintedWater, Blocks.water, Blocks.darksandTaintedWater, Blocks.darksandWater});
        this.tars = ObjectMap.of(new Object[]{Blocks.sporeMoss, Blocks.shale, Blocks.moss, Blocks.shale});
        this.water = 2.0F / (float)this.arr[0].length;
    }

    float rawHeight(Vec3 position) {
        position = Tmp.v33.set(position).scl(this.scl);
        return (Mathf.pow(Simplex.noise3d(this.seed, (double)7.0F, (double)0.5F, (double)0.33333334F, (double)position.x, (double)position.y, (double)position.z), 2.3F) + this.waterOffset) / (1.0F + this.waterOffset);
    }

    public void generateSector(Sector sector) {
        if (sector.id != 154 && sector.id != 0) {
            PlanetGrid.Ptile tile = sector.tile;
            boolean any = false;
            float poles = Math.abs(tile.v.y);
            float noise = Noise.snoise3(tile.v.x, tile.v.y, tile.v.z, 0.001F, 0.58F);
            if ((double)noise + (double)poles / 7.1 > 0.12 && (double)poles > 0.23) {
                any = true;
            }

            if ((double)noise < 0.16) {
                for(PlanetGrid.Ptile other : tile.tiles) {
                    Sector osec = sector.planet.getSector(other);
                    if (osec.id == sector.planet.startSector || osec.generateEnemyBase && (double)poles < 0.85 || sector.preset != null && (double)noise < 0.11) {
                        return;
                    }
                }
            }

            sector.generateEnemyBase = any;
        } else {
            sector.generateEnemyBase = true;
        }
    }

    public float getHeight(Vec3 position) {
        float height = this.rawHeight(position);
        return Math.max(height, this.water);
    }

    public Color getColor(Vec3 position) {
        Block block = this.getBlock(position);
        return block == Blocks.salt ? Blocks.sand.mapColor : Tmp.c1.set(block.mapColor).a(1.0F - block.albedo);
    }

    public void genTile(Vec3 position, TileGen tile) {
        tile.floor = this.getBlock(position);
        tile.block = tile.floor.asFloor().wall;
        if ((double)Ridged.noise3d(this.seed + 1, (double)position.x, (double)position.y, (double)position.z, 2, 22.0F) > 0.31) {
            tile.block = Blocks.air;
        }

    }

    //TODO: Fix this to allow for better weighting and block selection
    Block getBlock(Vec3 position) {
        float height = this.rawHeight(position);
        Tmp.v31.set(position);
        position = Tmp.v33.set(position).scl(this.scl);
        float rad = this.scl;
        float temp = Mathf.clamp(Math.abs(position.y * 2.0F) / rad);
        float tnoise = Simplex.noise3d(this.seed, (double)7.0F, 0.56, (double)0.33333334F, (double)position.x, (double)(position.y + 999.0F), (double)position.z);
        temp = Mathf.lerp(temp, tnoise, 0.5F);
        height *= 1.2F;
        height = Mathf.clamp(height);
        float tar = Simplex.noise3d(this.seed, (double)4.0F, (double)0.55F, (double)0.5F, (double)position.x, (double)(position.y + 999.0F), (double)position.z) * 0.3F + Tmp.v31.dst(0.0F, 0.0F, 1.0F) * 0.2F;
        Block res = this.arr[Mathf.clamp((int)(temp * (float)this.arr.length), 0, this.arr[0].length - 1)][Mathf.clamp((int)(height * (float)this.arr[0].length), 0, this.arr[0].length - 1)];
        return tar > 0.5F ? (Block)this.tars.get(res, res) : res;
    }

    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        Vec3 v = this.sector.rect.project(x, y).scl(5.0F);
        return Simplex.noise3d(this.seed, octaves, falloff, (double)1.0F / scl, (double)v.x, (double)v.y, (double)v.z) * (float)mag;
    }

    protected void generate() {
        this.cells(4);
        this.distort(10.0F, 12.0F);
        float constraint = 1.3F;
        float radius = (float)this.width / 2.0F / Mathf.sqrt3;
        int rooms = this.rand.random(2, 5);

        class Room {
            int x;
            int y;
            int radius;
            ObjectSet<Room> connected = new ObjectSet();

            Room(int x, int y, int radius) {
                this.x = x;
                this.y = y;
                this.radius = radius;
                this.connected.add(this);
            }

            void join(int x1, int y1, int x2, int y2) {
                float nscl = MinventionPlanetGenerator.this.rand.random(100.0F, 140.0F) * 6.0F;
                int stroke = MinventionPlanetGenerator.this.rand.random(3, 9);
                MinventionPlanetGenerator.this.brush(MinventionPlanetGenerator.this.pathfind(x1, y1, x2, y2, (tile) -> (tile.solid() ? 50.0F : 0.0F) + MinventionPlanetGenerator.this.noise((float)tile.x, (float)tile.y, (double)2.0F, (double)0.4F, (double)(1.0F / nscl)) * 500.0F, Astar.manhattan), stroke);
            }

            void connect(Room to) {
                if (this.connected.add(to) && to != this) {
                    Vec2 midpoint = Tmp.v1.set((float)to.x, (float)to.y).add((float)this.x, (float)this.y).scl(0.5F);
                    MinventionPlanetGenerator.this.rand.nextFloat();
                    if (MinventionPlanetGenerator.alt) {
                        midpoint.add(Tmp.v2.set(1.0F, 0.0F).setAngle(Angles.angle((float)to.x, (float)to.y, (float)this.x, (float)this.y) + 90.0F * (MinventionPlanetGenerator.this.rand.chance((double)0.5F) ? 1.0F : -1.0F)).scl(Tmp.v1.dst((float)this.x, (float)this.y) * 2.0F));
                    } else {
                        midpoint.add(Tmp.v2.setToRandomDirection(MinventionPlanetGenerator.this.rand).scl(Tmp.v1.dst((float)this.x, (float)this.y)));
                    }

                    midpoint.sub((float)MinventionPlanetGenerator.this.width / 2.0F, (float)MinventionPlanetGenerator.this.height / 2.0F).limit((float)MinventionPlanetGenerator.this.width / 2.0F / Mathf.sqrt3).add((float)MinventionPlanetGenerator.this.width / 2.0F, (float)MinventionPlanetGenerator.this.height / 2.0F);
                    int mx = (int)midpoint.x;
                    int my = (int)midpoint.y;
                    this.join(this.x, this.y, mx, my);
                    this.join(mx, my, to.x, to.y);
                }
            }

            void joinLiquid(int x1, int y1, int x2, int y2) {
                float nscl = MinventionPlanetGenerator.this.rand.random(100.0F, 140.0F) * 6.0F;
                int rad = MinventionPlanetGenerator.this.rand.random(7, 11);
                int avoid = 2 + rad;
                Seq<Tile> path = MinventionPlanetGenerator.this.pathfind(x1, y1, x2, y2, (tile) -> (!tile.solid() && tile.floor().isLiquid ? 0.0F : 70.0F) + MinventionPlanetGenerator.this.noise((float)tile.x, (float)tile.y, (double)2.0F, (double)0.4F, (double)(1.0F / nscl)) * 500.0F, Astar.manhattan);
                path.each((t) -> {
                    if (!(Mathf.dst2((float)t.x, (float)t.y, (float)x2, (float)y2) <= (float)(avoid * avoid))) {
                        for(int x = -rad; x <= rad; ++x) {
                            for(int y = -rad; y <= rad; ++y) {
                                int wx = t.x + x;
                                int wy = t.y + y;
                                if (Structs.inBounds(wx, wy, MinventionPlanetGenerator.this.width, MinventionPlanetGenerator.this.height) && Mathf.within((float)x, (float)y, (float)rad)) {
                                    Tile other = MinventionPlanetGenerator.this.tiles.getn(wx, wy);
                                    other.setBlock(Blocks.air);
                                    if (Mathf.within((float)x, (float)y, (float)(rad - 1)) && !other.floor().isLiquid) {
                                        Floor floor = other.floor();
                                        other.setFloor((Floor)(floor != Blocks.sand && floor != Blocks.salt ? Blocks.darksandTaintedWater : Blocks.sandWater));
                                    }
                                }
                            }
                        }

                    }
                });
            }

            void connectLiquid(Room to) {
                if (to != this) {
                    Vec2 midpoint = Tmp.v1.set((float)to.x, (float)to.y).add((float)this.x, (float)this.y).scl(0.5F);
                    MinventionPlanetGenerator.this.rand.nextFloat();
                    midpoint.add(Tmp.v2.setToRandomDirection(MinventionPlanetGenerator.this.rand).scl(Tmp.v1.dst((float)this.x, (float)this.y)));
                    midpoint.sub((float)MinventionPlanetGenerator.this.width / 2.0F, (float)MinventionPlanetGenerator.this.height / 2.0F).limit((float)MinventionPlanetGenerator.this.width / 2.0F / Mathf.sqrt3).add((float)MinventionPlanetGenerator.this.width / 2.0F, (float)MinventionPlanetGenerator.this.height / 2.0F);
                    int mx = (int)midpoint.x;
                    int my = (int)midpoint.y;
                    this.joinLiquid(this.x, this.y, mx, my);
                    this.joinLiquid(mx, my, to.x, to.y);
                }
            }
        }

        Seq<Room> roomseq = new Seq();

        for(int i = 0; i < rooms; ++i) {
            Tmp.v1.trns(this.rand.random(360.0F), this.rand.random(radius / constraint));
            float rx = (float)this.width / 2.0F + Tmp.v1.x;
            float ry = (float)this.height / 2.0F + Tmp.v1.y;
            float maxrad = radius - Tmp.v1.len();
            float rrad = Math.min(this.rand.random(9.0F, maxrad / 2.0F), 30.0F);
            roomseq.add(new Room((int)rx, (int)ry, (int)rrad));
        }

        Room spawn = null;
        Seq<Room> enemies = new Seq();
        int enemySpawns = this.rand.random(1, Math.max((int)(this.sector.threat * 4.0F), 1));
        int offset = this.rand.nextInt(360);
        float length = (float)this.width / 2.55F - (float)this.rand.random(13, 23);
        int angleStep = 5;
        int waterCheckRad = 5;

        for(int i = 0; i < 360; i += angleStep) {
            int angle = offset + i;
            int cx = (int)((float)(this.width / 2) + Angles.trnsx((float)angle, length));
            int cy = (int)((float)(this.height / 2) + Angles.trnsy((float)angle, length));
            int waterTiles = 0;

            for(int rx = -waterCheckRad; rx <= waterCheckRad; ++rx) {
                for(int ry = -waterCheckRad; ry <= waterCheckRad; ++ry) {
                    Tile tile = this.tiles.get(cx + rx, cy + ry);
                    if (tile == null || tile.floor().liquidDrop != null) {
                        ++waterTiles;
                    }
                }
            }

            if (waterTiles <= 4 || i + angleStep >= 360) {
                roomseq.add(spawn = new Room(cx, cy, this.rand.random(8, 15)));

                for(int j = 0; j < enemySpawns; ++j) {
                    float enemyOffset = this.rand.range(60.0F);
                    Tmp.v1.set((float)(cx - this.width / 2), (float)(cy - this.height / 2)).rotate(180.0F + enemyOffset).add((float)(this.width / 2), (float)(this.height / 2));
                    Room espawn = new Room((int)Tmp.v1.x, (int)Tmp.v1.y, this.rand.random(8, 16));
                    roomseq.add(espawn);
                    enemies.add(espawn);
                }
                break;
            }
        }

        for(Room room : roomseq) {
            this.erase(room.x, room.y, room.radius);
        }

        int connections = this.rand.random(Math.max(rooms - 1, 1), rooms + 3);

        for(int i = 0; i < connections; ++i) {
            ((Room)roomseq.random(this.rand)).connect((Room)roomseq.random(this.rand));
        }

        for(Room room : roomseq) {
            spawn.connect(room);
        }

        this.cells(1);
        int tlen = this.tiles.width * this.tiles.height;
        int total = 0;
        int waters = 0;

        for(int i = 0; i < tlen; ++i) {
            Tile tile = this.tiles.geti(i);
            if (tile.block() == Blocks.air) {
                ++total;
                if (tile.floor().liquidDrop == Liquids.water) {
                    ++waters;
                }
            }
        }

        boolean naval = (float)waters / (float)total >= 0.19F;
        if (naval) {
            for(Room room : enemies) {
                room.connectLiquid(spawn);
            }
        }

        this.distort(10.0F, 6.0F);
        Room finalSpawn = spawn;
        this.pass((xx, yx) -> {
            if (!this.block.solid) {
                Vec3 v = this.sector.rect.project((float)xx, (float)yx);
                float rr = Simplex.noise2d(this.sector.id, (double)2.0F, (double)0.6F, (double)0.14285715F, (double)xx, (double)yx) * 0.1F;
                float value = Ridged.noise3d(2, (double)v.x, (double)v.y, (double)v.z, 1, 0.018181818F) + rr - this.rawHeight(v) * 0.0F;
                float rrscl = rr * 44.0F - 2.0F;
                if (value > 0.17F && !Mathf.within((float)xx, (float)yx, (float) finalSpawn.x, (float) finalSpawn.y, 12.0F + rrscl)) {
                    boolean deep = value > 0.27F && !Mathf.within((float)xx, (float)yx, (float) finalSpawn.x, (float) finalSpawn.y, 15.0F + rrscl);
                    boolean spore = this.floor != Blocks.sand && this.floor != Blocks.salt;
                    if (this.floor != Blocks.ice && this.floor != Blocks.iceSnow && this.floor != Blocks.snow && !this.floor.asFloor().isLiquid) {
                        this.floor = spore ? (deep ? Blocks.taintedWater : Blocks.darksandTaintedWater) : (deep ? Blocks.water : (this.floor != Blocks.sand && this.floor != Blocks.salt ? Blocks.darksandWater : Blocks.sandWater));
                    }
                }

            }
        });
        this.pass((xx, yx) -> {
            int deepRadius = 3;
            if (this.floor.asFloor().isLiquid && this.floor.asFloor().shallow) {
                for(int cx = -deepRadius; cx <= deepRadius; ++cx) {
                    for(int cy = -deepRadius; cy <= deepRadius; ++cy) {
                        if (cx * cx + cy * cy <= deepRadius * deepRadius) {
                            int wx = cx + xx;
                            int wy = cy + yx;
                            Tile tile = this.tiles.get(wx, wy);
                            if (tile != null && (!tile.floor().isLiquid || tile.block() != Blocks.air)) {
                                return;
                            }
                        }
                    }
                }

                this.floor = this.floor == Blocks.darksandTaintedWater ? Blocks.taintedWater : Blocks.water;
            }

        });
        if (naval) {
            int deepRadius = 2;
            this.pass((xx, yx) -> {
                if (this.floor.asFloor().isLiquid && !this.floor.asFloor().isDeep() && !this.floor.asFloor().shallow) {
                    for(int cx = -deepRadius; cx <= deepRadius; ++cx) {
                        for(int cy = -deepRadius; cy <= deepRadius; ++cy) {
                            if (cx * cx + cy * cy <= deepRadius * deepRadius) {
                                int wx = cx + xx;
                                int wy = cy + yx;
                                Tile tile = this.tiles.get(wx, wy);
                                if (tile != null && (tile.floor().shallow || !tile.floor().isLiquid)) {
                                    return;
                                }
                            }
                        }
                    }

                    this.floor = this.floor == Blocks.water ? Blocks.deepwater : Blocks.taintedWater;
                }

            });
        }

        Seq<Block> ores = Seq.with(new Block[]{Blocks.oreCopper, Blocks.oreLead, MinventionBlocksEnvironment.oreIron});
        float poles = Math.abs(this.sector.tile.v.y);
        float nmag = 0.5F;
        float scl = 1.0F;
        float addscl = 1.3F;
        if (Simplex.noise3d(this.seed, (double)2.0F, (double)0.5F, (double)scl, (double)this.sector.tile.v.x, (double)this.sector.tile.v.y, (double)this.sector.tile.v.z) * nmag + poles > 0.25F * addscl) {
            ores.add(Blocks.oreCoal);
        }

        if (Simplex.noise3d(this.seed, (double)2.0F, (double)0.5F, (double)scl, (double)(this.sector.tile.v.x + 1.0F), (double)this.sector.tile.v.y, (double)this.sector.tile.v.z) * nmag + poles > 0.5F * addscl) {
            ores.add(Blocks.oreTitanium);
        }

        if (Simplex.noise3d(this.seed, (double)2.0F, (double)0.5F, (double)scl, (double)(this.sector.tile.v.x + 2.0F), (double)this.sector.tile.v.y, (double)this.sector.tile.v.z) * nmag + poles > 0.7F * addscl) {
            ores.add(Blocks.oreThorium);
        }

        if (this.rand.chance((double)0.25F)) {
            ores.add(Blocks.oreScrap);
        }

        FloatSeq frequencies = new FloatSeq();

        for(int i = 0; i < ores.size; ++i) {
            frequencies.add(this.rand.random(-0.1F, 0.01F) - (float)i * 0.01F + poles * 0.04F);
        }

        this.pass((xx, yx) -> {
            if (this.floor.asFloor().hasSurface()) {
                int offsetX = xx - 4;
                int offsetY = yx + 23;

                for(int i = ores.size - 1; i >= 0; --i) {
                    Block entry = (Block)ores.get(i);
                    float freq = frequencies.get(i);
                    if ((double)Math.abs(0.5F - this.noise((float)offsetX, (float)(offsetY + i * 999), (double)2.0F, 0.7, (double)(40 + i * 2))) > (double)0.22F + (double)i * 0.01 && Math.abs(0.5F - this.noise((float)offsetX, (float)(offsetY - i * 999), (double)1.0F, (double)1.0F, (double)(30 + i * 4))) > 0.37F + freq) {
                        this.ore = entry;
                        break;
                    }
                }

                if (this.ore == Blocks.oreScrap && this.rand.chance(0.33)) {
                    this.floor = Blocks.metalFloorDamaged;
                }

            }
        });
        this.trimDark();
        this.median(2);
        this.inverseFloodFill(this.tiles.getn(spawn.x, spawn.y));
        this.tech();
        this.pass((xx, yx) -> {
            if (this.floor == Blocks.sporeMoss && (double)Math.abs(0.5F - this.noise((float)(xx - 90), (float)yx, (double)4.0F, 0.8, (double)65.0F)) > 0.02) {
                this.floor = Blocks.moss;
            }

            if (this.floor == Blocks.darksand && Math.abs(0.5F - this.noise((float)(xx - 40), (float)yx, (double)2.0F, 0.7, (double)80.0F)) > 0.25F && Math.abs(0.5F - this.noise((float)xx, (float)(yx + this.sector.id * 10), (double)1.0F, (double)1.0F, (double)60.0F)) > 0.41F && !roomseq.contains((r) -> Mathf.within((float)xx, (float)yx, (float)r.x, (float)r.y, 30.0F))) {
                this.floor = Blocks.tar;
            }

            if (this.floor == Blocks.hotrock) {
                if ((double)Math.abs(0.5F - this.noise((float)(xx - 90), (float)yx, (double)4.0F, 0.8, (double)80.0F)) > 0.035) {
                    this.floor = Blocks.basalt;
                } else {
                    this.ore = Blocks.air;
                    boolean all = true;

                    for(Point2 p : Geometry.d4) {
                        Tile other = this.tiles.get(xx + p.x, yx + p.y);
                        if (other == null || other.floor() != Blocks.hotrock && other.floor() != Blocks.magmarock) {
                            all = false;
                        }
                    }

                    if (all) {
                        this.floor = Blocks.magmarock;
                    }
                }
            } else if (this.genLakes && this.floor != Blocks.basalt && this.floor != Blocks.ice && this.floor.asFloor().hasSurface()) {
                float noise = this.noise((float)(xx + 782), (float)yx, (double)5.0F, (double)0.75F, (double)260.0F, (double)1.0F);
                if (noise > 0.67F && !roomseq.contains((e) -> Mathf.within((float)xx, (float)yx, (float)e.x, (float)e.y, 14.0F))) {
                    if (noise > 0.72F) {
                        this.floor = noise > 0.78F ? Blocks.taintedWater : (this.floor == Blocks.sand ? Blocks.sandWater : Blocks.darksandTaintedWater);
                    } else {
                        this.floor = this.floor == Blocks.sand ? this.floor : Blocks.darksand;
                    }
                }
            }

            if (this.rand.chance(0.0075)) {
                boolean any = false;
                boolean all = true;

                for(Point2 p : Geometry.d4) {
                    Tile other = this.tiles.get(xx + p.x, yx + p.y);
                    if (other != null && other.block() == Blocks.air) {
                        any = true;
                    } else {
                        all = false;
                    }
                }

                if (any && (this.block == Blocks.snowWall || this.block == Blocks.iceWall || all && this.block == Blocks.air && this.floor == Blocks.snow && this.rand.chance(0.03))) {
                    this.block = this.rand.chance((double)0.5F) ? Blocks.whiteTree : Blocks.whiteTreeDead;
                }
            }

            int i = 0;

            while(true) {
                if (i >= 4) {
                    if (this.rand.chance(0.01) && this.floor.asFloor().hasSurface() && this.block == Blocks.air) {
                        this.block = (Block)this.dec.get(this.floor, this.floor.asFloor().decoration);
                    }
                    break;
                }

                Tile near = Vars.world.tile(xx + Geometry.d4[i].x, yx + Geometry.d4[i].y);
                if (near != null && near.block() != Blocks.air) {
                    break;
                }

                ++i;
            }

        });
        float difficulty = this.sector.threat;
        this.ints.clear();
        this.ints.ensureCapacity(this.width * this.height / 4);
        int ruinCount = this.rand.random(-2, 4);
        if (ruinCount > 0) {
            int padding = 25;

            for(int x = padding; x < this.width - padding; ++x) {
                for(int y = padding; y < this.height - padding; ++y) {
                    Tile tile = this.tiles.getn(x, y);
                    if (!tile.solid() && (tile.drop() != null || tile.floor().liquidDrop != null)) {
                        this.ints.add(tile.pos());
                    }
                }
            }

            this.ints.shuffle(this.rand);
            int placed = 0;
            float diffRange = 0.4F;

            for(int i = 0; i < this.ints.size && placed < ruinCount; ++i) {
                int val = this.ints.items[i];
                int x = Point2.x(val);
                int y = Point2.y(val);
                if (!Mathf.within((float)x, (float)y, (float)spawn.x, (float)spawn.y, 18.0F)) {
                    float range = difficulty + this.rand.random(diffRange);
                    Tile tile = this.tiles.getn(x, y);
                    BaseRegistry.BasePart part = null;
                    if (tile.overlay().itemDrop != null) {
                        part = (BaseRegistry.BasePart)Vars.bases.forResource(tile.drop()).getFrac(range);
                    } else if (tile.floor().liquidDrop != null && this.rand.chance(0.05)) {
                        part = (BaseRegistry.BasePart)Vars.bases.forResource(tile.floor().liquidDrop).getFrac(range);
                    } else if (this.rand.chance(0.05)) {
                        part = (BaseRegistry.BasePart)Vars.bases.parts.getFrac(range);
                    }

                    if (part != null && BaseGenerator.tryPlace(part, x, y, Team.derelict, (cx, cy) -> {
                        Tile other = this.tiles.getn(cx, cy);
                        if (other.floor().hasSurface()) {
                            other.setOverlay(Blocks.oreScrap);

                            for(int j = 1; j <= 2; ++j) {
                                for(Point2 p : Geometry.d8) {
                                    Tile t = this.tiles.get(cx + p.x * j, cy + p.y * j);
                                    if (t != null && t.floor().hasSurface() && this.rand.chance(j == 1 ? 0.4 : 0.2)) {
                                        t.setOverlay(Blocks.oreScrap);
                                    }
                                }
                            }
                        }

                    })) {
                        ++placed;
                        int debrisRadius = Math.max(part.schematic.width, part.schematic.height) / 2 + 3;
                        Geometry.circle(x, y, this.tiles.width, this.tiles.height, debrisRadius, (cx, cy) -> {
                            float dst = Mathf.dst((float)cx, (float)cy, (float)x, (float)y);
                            float removeChance = Mathf.lerp(0.05F, 0.5F, dst / (float)debrisRadius);
                            Tile other = this.tiles.getn(cx, cy);
                            if (other.build != null && other.isCenter()) {
                                if (other.team() == Team.derelict && this.rand.chance((double)removeChance)) {
                                    other.remove();
                                } else if (this.rand.chance((double)0.5F)) {
                                    other.build.health -= this.rand.random(other.build.health * 0.9F);
                                }
                            }

                        });
                    }
                }
            }
        }

        for(Tile tile : this.tiles) {
            if (tile.overlay().needsSurface && !tile.floor().hasSurface()) {
                tile.setOverlay(Blocks.air);
            }
        }

        Schematics.placeLaunchLoadout(spawn.x, spawn.y);

        for(Room espawn : enemies) {
            this.tiles.getn(espawn.x, espawn.y).setOverlay(Blocks.spawn);
        }

        if (this.sector.hasEnemyBase()) {
            this.basegen.generate(this.tiles, enemies.map((r) -> this.tiles.getn(r.x, r.y)), this.tiles.get(spawn.x, spawn.y), Vars.state.rules.waveTeam, this.sector, difficulty);
            Vars.state.rules.attackMode = this.sector.info.attack = true;
        } else {
            Vars.state.rules.winWave = this.sector.info.winWave = 10 + 5 * (int)Math.max(difficulty * 10.0F, 1.0F);
        }

        float waveTimeDec = 0.4F;
        Vars.state.rules.waveSpacing = Mathf.lerp(7800.0F, 3600.0F, Math.max(difficulty - waveTimeDec, 0.0F));
        Vars.state.rules.waves = true;
        Vars.state.rules.env = this.sector.planet.defaultEnv;
        Vars.state.rules.enemyCoreBuildRadius = 600.0F;
        Vars.state.rules.spawns = Waves.generate(difficulty, new Rand((long)this.sector.id), Vars.state.rules.attackMode, Vars.state.rules.attackMode && Vars.spawner.countGroundSpawns() == 0, naval);
    }

    public void postGenerate(Tiles tiles) {
        if (this.sector.hasEnemyBase()) {
            this.basegen.postGenerate();
            if (Vars.spawner.countGroundSpawns() == 0) {
                Vars.state.rules.spawns = Waves.generate(this.sector.threat, new Rand((long)this.sector.id), Vars.state.rules.attackMode, true, false);
            }
        }

    }
}
