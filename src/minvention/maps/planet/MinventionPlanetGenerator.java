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
    //TODO: Figure out purpose of this static boolean, assuming it means alternate but unsure what it actually does
    public static boolean alt = false;
    BaseGenerator baseGen = new BaseGenerator();
    //TODO: Figure out purpose of this scale variable
    float scale = 5.0F;
    boolean genLakes = false;

    public MinventionPlanetGenerator() {

    }

    float rawHeight(Vec3 position) {
        //TODO: Figure out purpose of this temporary variable to try to eliminate it
        position = Tmp.v33.set(position).scl(this.scale);
        return (Mathf.pow(Simplex.noise3d(this.seed, 7.0F, 0.5F, 0.33333334F, position.x, position.y, position.z), 2.3F));
    }

    //TODO: Update enemy base logic to prefer certain locations and to stop them generating as much in watery sectors
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
                    Sector otherSector = sector.planet.getSector(other);
                    if (otherSector.id == sector.planet.startSector || otherSector.generateEnemyBase && (double)poles < 0.85 || sector.preset != null && (double)noise < 0.11) {
                        return;
                    }
                }
            }

            sector.generateEnemyBase = any;
        } else {
            sector.generateEnemyBase = true;
        }
    }

    //TODO: Probably get rid of this function as it no longer serves much of a purpose
    public float getHeight(Vec3 position) {
        return this.rawHeight(position);
    }

    //TODO: Probably look at ice's colour and maybe remove ternary statement blocking salt from being shown
    public Color getColor(Vec3 position) {
        Block block = this.getBlock(position);
        return block == Blocks.salt ? Blocks.sand.mapColor : Tmp.c1.set(block.mapColor).a(1.0F - block.albedo);
    }

    //TODO: Update logic for placing walls, allow for more open sector generation
    public void genTile(Vec3 position, TileGen tile) {
        tile.floor = this.getBlock(position);
        tile.block = tile.floor.asFloor().wall;
        if ((double)Ridged.noise3d(this.seed + 1, position.x, position.y, position.z, 2, 22.0F) > 0.31) {
            tile.block = Blocks.air;
        }

    }

    // Just returns whether water should be made shallower or frozen based off an if statement since this logic was repeated many times
    public static Block freezeWater(Block defaultWater, Block shallowWater, Block frozenWater, double temperature) {
        if (temperature < 0f) {
            return frozenWater;
        }
        if (temperature < 0.1f) {
            return shallowWater;
        }
        return defaultWater;
    }

    //TODO: Fix this to allow for better weighting and block selection
    Block getBlock(Vec3 position) {
        float height = this.rawHeight(position);
        // Calculate latitude ranging from -0.5 to 0.5 using inverse sin, hypotenuse is the radius which is 1 so no division is needed
        double latitude = Math.asin(position.y) / Mathf.PI;
        // Subtract absolute attitude from 0.5 to get poleDistance starting at 0 at poles and ranging to 0.5 at the equator
        double poleDistance = 0.5 - Math.abs(latitude);
        // Adds 0.5 to the latitude to change it from ranging from 0 to 1
        latitude += 0.5;
        // Scale height based on distance from poles to create more oceans near the equator and mountainous poles
        height *= (float) (1.1 - poleDistance/2);
        // Set temperature based on square of the distance from the poles, max value of temperature becomes 0.625
        double temperature = poleDistance * poleDistance * 2.5;
        // Height subtracted from temperature, should always lower the temperature to make poles and some peaks negative while most places remain in the low positives
        temperature -= height;
        // Longitude calculated using the inverse tangent of the z coordinate over the x coordinate. Ranges between 0.5 and 0.5
        double longitude = Math.atan(position.z / position.x) / Mathf.PI;
        // Shifts longitude to ranging from 0 to 2. Represents radians without the Pi component to make math easier
        if (position.x < 0) {
            longitude += 1;
        } else if (position.z < 0) {
            longitude += 2;
        }
        // Default block of sand for midranges
        Block block = Blocks.sand;
        // Create water at low elevations, grass at mid and stone at high. Water freezes at low temperatures and is made shallow at mid-low temperatures to aid transitions
        if (height < 0.1f) {
            block = freezeWater(Blocks.deepwater, Blocks.water, Blocks.ice, temperature);
        } else if (height < 0.175f) {
            block = freezeWater(Blocks.water, Blocks.sandWater, Blocks.ice, temperature);
        } else if (height < 0.25f) {
            block = freezeWater(Blocks.sandWater, Blocks.sandWater, Blocks.ice, temperature);
        } else if (height > 0.45f) {
            block = Blocks.stone;
        } else if (height > 0.325f) {
            block = Blocks.grass;
        }
        // If the temperature is low and the height is above water level, snow will cover the terrain
        if (temperature < -0.1f && height > 0.25) {
            block = Blocks.snow;
        }
        return block;
    }

    //TODO: Figure out what exactly the project method is doing, maybe adjust the noise too
    protected float noise(float x, float y, double octaves, double falloff, double scale, double mag) {
        Vec3 v = this.sector.rect.project(x, y).scl(5.0F);
        return Simplex.noise3d(this.seed, octaves, falloff, (double)1.0F / scale, v.x, v.y, v.z) * (float)mag;
    }

    //TODO: Completely rewrite this method to add patches of interest and remove features that cut through terrain too much
    protected void generate() {
        this.cells(4);
        this.distort(10.0F, 12.0F);
        float constraint = 1.3F;
        float radius = (float)this.width / 2.0F / Mathf.sqrt3;
        int rooms = this.rand.random(2, 5);

        class Room {
            final int x;
            final int y;
            final int radius;
            final ObjectSet<Room> connected = new ObjectSet<>();

            Room(int x, int y, int radius) {
                this.x = x;
                this.y = y;
                this.radius = radius;
                this.connected.add(this);
            }

            void join(int x1, int y1, int x2, int y2) {
                // I'm pretty sure this is supposed to be noiseScale
                float noiseScale = MinventionPlanetGenerator.this.rand.random(100.0F, 140.0F) * 6.0F;
                int stroke = MinventionPlanetGenerator.this.rand.random(3, 9);
                MinventionPlanetGenerator.this.brush(MinventionPlanetGenerator.this.pathfind(x1, y1, x2, y2, (tile) -> (tile.solid() ? 50.0F : 0.0F) + MinventionPlanetGenerator.this.noise(tile.x, tile.y, 2.0F, 0.4F, (1.0F / noiseScale)) * 500.0F, Astar.manhattan), stroke);
            }

            void connect(Room to) {
                if (this.connected.add(to) && to != this) {
                    Vec2 midpoint = Tmp.v1.set((float)to.x, (float)to.y).add((float)this.x, (float)this.y).scl(0.5F);
                    MinventionPlanetGenerator.this.rand.nextFloat();
                    if (MinventionPlanetGenerator.alt) {
                        midpoint.add(Tmp.v2.set(1.0F, 0.0F).setAngle(Angles.angle((float)to.x, (float)to.y, (float)this.x, (float)this.y) + 90.0F * (MinventionPlanetGenerator.this.rand.chance(0.5F) ? 1.0F : -1.0F)).scl(Tmp.v1.dst((float)this.x, (float)this.y) * 2.0F));
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
                // I'm pretty sure this is supposed to be noiseScale
                float noiseScale = MinventionPlanetGenerator.this.rand.random(100.0F, 140.0F) * 6.0F;
                int rad = MinventionPlanetGenerator.this.rand.random(7, 11);
                int avoid = 2 + rad;
                Seq<Tile> path = MinventionPlanetGenerator.this.pathfind(x1, y1, x2, y2, (tile) -> (!tile.solid() && tile.floor().isLiquid ? 0.0F : 70.0F) + MinventionPlanetGenerator.this.noise(tile.x, tile.y, 2.0F, 0.4F, 1.0F / noiseScale) * 500.0F, Astar.manhattan);
                path.each((t) -> {
                    if (!(Mathf.dst2(t.x, t.y, (float)x2, (float)y2) <= (float)(avoid * avoid))) {
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
            //TODO: Figure out how to make this organic and better looking
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

        Seq<Room> roomseq = new Seq<>();

        for(int i = 0; i < rooms; ++i) {
            Tmp.v1.trns(this.rand.random(360.0F), this.rand.random(radius / constraint));
            float rx = (float)this.width / 2.0F + Tmp.v1.x;
            float ry = (float)this.height / 2.0F + Tmp.v1.y;
            // I think this is the max radius
            float maxRadius = radius - Tmp.v1.len();
            float randomRadius = Math.min(this.rand.random(9.0F, maxRadius / 2.0F), 30.0F);
            roomseq.add(new Room((int)rx, (int)ry, (int)randomRadius));
        }

        Room spawn = null;
        Seq<Room> enemies = new Seq<>();
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
                    Room enemySpawn = new Room((int)Tmp.v1.x, (int)Tmp.v1.y, this.rand.random(8, 16));
                    roomseq.add(enemySpawn);
                    enemies.add(enemySpawn);
                }
                break;
            }
        }

        for(Room room : roomseq) {
            this.erase(room.x, room.y, room.radius);
        }

        int connections = this.rand.random(Math.max(rooms - 1, 1), rooms + 3);

        for(int i = 0; i < connections; ++i) {
            roomseq.random(this.rand).connect(roomseq.random(this.rand));
        }

        for(Room room : roomseq) {
            spawn.connect(room);
        }

        this.cells(1);
        int tilesLength = this.tiles.width * this.tiles.height;
        int total = 0;
        int waters = 0;

        for(int i = 0; i < tilesLength; ++i) {
            Tile tile = this.tiles.geti(i);
            if (tile.block() == Blocks.air) {
                ++total;
                if (tile.floor().liquidDrop == Liquids.water) {
                    ++waters;
                }
            }
        }

        boolean naval = (float)waters / (float)total >= 0.25F;
        if (naval) {
            for(Room room : enemies) {
                room.connectLiquid(spawn);
            }
        }

        this.distort(10.0F, 6.0F);

        Seq<Block> ores = Seq.with(Blocks.oreCopper, Blocks.oreLead, MinventionBlocksEnvironment.oreIron);
        float poles = Math.abs(this.sector.tile.v.y);
        float noiseMagnitude = 0.5F;
        float scale = 1.0F;
        float addScale = 1.3F;
        if (Simplex.noise3d(this.seed, 2.0F, 0.5F, scale, this.sector.tile.v.x, this.sector.tile.v.y, this.sector.tile.v.z) * noiseMagnitude + poles > 0.25F * addScale) {
            ores.add(Blocks.oreCoal);
        }

        if (Simplex.noise3d(this.seed, 2.0F, 0.5F, scale, this.sector.tile.v.x + 1.0F, this.sector.tile.v.y, this.sector.tile.v.z) * noiseMagnitude + poles > 0.5F * addScale) {
            ores.add(Blocks.oreTitanium);
        }

        if (Simplex.noise3d(this.seed, 2.0F, 0.5F, scale, this.sector.tile.v.x + 2.0F, this.sector.tile.v.y, this.sector.tile.v.z) * noiseMagnitude + poles > 0.7F * addScale) {
            ores.add(Blocks.oreThorium);
        }

        if (this.rand.chance(0.25F)) {
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
                    Block entry = ores.get(i);
                    float freq = frequencies.get(i);
                    if ((double)Math.abs(0.5F - this.noise((float)offsetX, (float)(offsetY + i * 999), 2.0F, 0.7, 40 + i * 2)) > (double)0.22F + (double)i * 0.01 && Math.abs(0.5F - this.noise((float)offsetX, (float)(offsetY - i * 999), 1.0F, 1.0F, 30 + i * 4)) > 0.37F + freq) {
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
            if (this.floor == Blocks.darksand && Math.abs(0.5F - this.noise((float)(xx - 40), (float)yx, 2.0F, 0.7, 80.0F)) > 0.25F && Math.abs(0.5F - this.noise((float)xx, (float)(yx + this.sector.id * 10), 1.0F, 1.0F, 60.0F)) > 0.41F && !roomseq.contains((r) -> Mathf.within((float)xx, (float)yx, (float)r.x, (float)r.y, 30.0F))) {
                this.floor = Blocks.tar;
            }

            if (this.floor == Blocks.hotrock) {
                if ((double)Math.abs(0.5F - this.noise((float)(xx - 90), (float)yx, 4.0F, 0.8, 80.0F)) > 0.035) {
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
                float noise = this.noise((float)(xx + 782), (float)yx, 5.0F, 0.75F, 260.0F, 1.0F);
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
                    this.block = this.rand.chance(0.5F) ? Blocks.whiteTree : Blocks.whiteTreeDead;
                }
            }

            int i = 0;

            while(true) {
                if (i >= 4) {
                    if (this.rand.chance(0.01) && this.floor.asFloor().hasSurface() && this.block == Blocks.air) {
                        // Used to be decoration placing code
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
                        part = Vars.bases.forResource(tile.drop()).getFrac(range);
                    } else if (tile.floor().liquidDrop != null && this.rand.chance(0.05)) {
                        part = Vars.bases.forResource(tile.floor().liquidDrop).getFrac(range);
                    } else if (this.rand.chance(0.05)) {
                        part = Vars.bases.parts.getFrac(range);
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
                                if (other.team() == Team.derelict && this.rand.chance(removeChance)) {
                                    other.remove();
                                } else if (this.rand.chance(0.5F)) {
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

        for(Room enemySpawn : enemies) {
            this.tiles.getn(enemySpawn.x, enemySpawn.y).setOverlay(Blocks.spawn);
        }

        if (this.sector.hasEnemyBase()) {
            this.baseGen.generate(this.tiles, enemies.map((r) -> this.tiles.getn(r.x, r.y)), this.tiles.get(spawn.x, spawn.y), Vars.state.rules.waveTeam, this.sector, difficulty);
            Vars.state.rules.attackMode = this.sector.info.attack = true;
        } else {
            Vars.state.rules.winWave = this.sector.info.winWave = 10 + 5 * (int)Math.max(difficulty * 10.0F, 1.0F);
        }

        float waveTimeDec = 0.4F;
        Vars.state.rules.waveSpacing = Mathf.lerp(7800.0F, 3600.0F, Math.max(difficulty - waveTimeDec, 0.0F));
        Vars.state.rules.waves = true;
        Vars.state.rules.env = this.sector.planet.defaultEnv;
        Vars.state.rules.enemyCoreBuildRadius = 600.0F;
        Vars.state.rules.spawns = Waves.generate(difficulty, new Rand(this.sector.id), Vars.state.rules.attackMode, Vars.state.rules.attackMode && Vars.spawner.countGroundSpawns() == 0, naval);
    }

    public void postGenerate(Tiles tiles) {
        if (this.sector.hasEnemyBase()) {
            this.baseGen.postGenerate();
            if (Vars.spawner.countGroundSpawns() == 0) {
                Vars.state.rules.spawns = Waves.generate(this.sector.threat, new Rand(this.sector.id), Vars.state.rules.attackMode, true, false);
            }
        }

    }
}
