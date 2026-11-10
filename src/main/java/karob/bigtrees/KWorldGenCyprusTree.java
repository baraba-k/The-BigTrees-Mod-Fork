package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class KWorldGenCyprusTree {
    LevelAccessor wo;
    Random rand;
    private boolean planted;
    private int baseY;
    int rootRand;
    int rootAlt;
    private Block cyprusWoodBlock = Blocks.SPRUCE_LOG;
    private Block cyprusLeafBlock = Blocks.SPRUCE_LEAVES;
    private Block cyprusBaseBlock1 = Blocks.GRASS_BLOCK;
    private Block cyprusBaseBlock2 = Blocks.DIRT;
    private int woodMeta = 1;
    private int leafMeta = 1;
    private int stuntmin = 7;
    private int heightmin = 28;
    private int heightmax = 32;

    public KWorldGenCyprusTree(boolean planted) {
        this.planted = planted;
        this.rootRand = 0;
        this.rootAlt = 0;
    }

    public void setConfigOptions(Block wood, Block leaf, int woodmeta, int leafmeta, Block base1, Block base2, int height1, int height2, int stunt) {
        this.cyprusWoodBlock = wood != null ? wood : Blocks.SPRUCE_LOG;
        this.cyprusLeafBlock = leaf != null ? leaf : Blocks.SPRUCE_LEAVES;
        this.cyprusBaseBlock1 = base1 != null ? base1 : Blocks.GRASS_BLOCK;
        this.cyprusBaseBlock2 = base2 != null ? base2 : Blocks.DIRT;
        this.woodMeta = woodmeta;
        this.leafMeta = leafmeta;
        this.heightmin = height1;
        this.heightmax = height2;
        this.stuntmin = stunt;
    }

    void setBlock(int x, int y, int z, Block block) {
        setBlockAndMetadata(x, y, z, block, 0);
    }

    void setBlockAndMetadata(int x, int y, int z, Block block, int meta) {
        try {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state;
            if (block == this.cyprusLeafBlock) {
                state = BTBlockHelper.getLeafState(block, meta, this.planted);
            } else {
                state = BTBlockHelper.getWoodState(block, meta);
            }
            this.wo.setBlock(pos, state, 3);
        } catch (Exception ignored) {
        }
    }

    Block getBlock(int x, int y, int z) {
        try {
            return this.wo.getBlockState(new BlockPos(x, y, z)).getBlock();
        } catch (Exception e) {
            return Blocks.AIR;
        }
    }

    public boolean func_76484_a(LevelAccessor world, Random random, int i, int j, int k) {
        this.wo = world;
        this.rand = random;
        this.baseY = j;
        int l = random.nextInt(Math.max(1, this.heightmax - this.heightmin)) + this.heightmin;
        if (j < 1) return false;
        if (j + l + 1 > 310 && (l = 310 - j - 2) < this.stuntmin) {
            return false;
        }

        if (!this.planted) {
            if (this.cyprusBaseBlock1 != Blocks.AIR || this.cyprusBaseBlock2 != Blocks.AIR) {
                boolean flag = false;
                Block id = this.getBlock(i, j - 1, k);
                if (this.cyprusBaseBlock1 != Blocks.AIR && (id == this.cyprusBaseBlock1 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (this.cyprusBaseBlock2 != Blocks.AIR && (id == this.cyprusBaseBlock2 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (!flag) return false;
            }
            Block id = this.getBlock(i, j, k);
            if (!BTBlockHelper.isReplaceableByTree(this.wo, new BlockPos(i, j, k)) && id != this.cyprusLeafBlock) {
                return false;
            }
        }

        double pitch = 1.2566370614359172;
        double dir = this.rand.nextFloat();
        double spin = 3.8830085198369844;
        double rootSlope = -1.0471975511965976;

        this.growRoot(i - 1, j + 4, k, 0.5625, rootSlope);
        this.growRoot(i - 1, j + 4, k + 1, 0.4375, rootSlope);
        this.growRoot(i, j + 4, k + 2, 0.3125, rootSlope);
        this.growRoot(i + 1, j + 4, k + 2, 0.1875, rootSlope);
        this.growRoot(i + 2, j + 4, k + 1, 0.0625, rootSlope);
        this.growRoot(i + 2, j + 4, k, 0.9375, rootSlope);
        this.growRoot(i + 1, j + 4, k - 1, 0.8125, rootSlope);
        this.growRoot(i, j + 4, k - 1, 0.6875, rootSlope);

        for (int n = 0; n < l; ++n) {
            if (n < l - 2) {
                this.setBlockAndMetadata(i, j + n, k, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
            } else {
                this.setBlockAndMetadata(i, j + n, k, this.cyprusLeafBlock, this.leafMeta);
                this.setBlockAndMetadata(i + 1, j + n, k, this.cyprusLeafBlock, this.leafMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 1, this.cyprusLeafBlock, this.leafMeta);
                this.setBlockAndMetadata(i, j + n, k + 1, this.cyprusLeafBlock, this.leafMeta);
            }
            if (n <= 8) {
                this.setBlockAndMetadata(i, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k, this.cyprusWoodBlock, this.woodMeta);
            }
            if (n <= 3) {
                this.setBlockAndMetadata(i - 1, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
            }
            if (n <= 1) {
                this.setBlockAndMetadata(i, j + n, k + 3, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 3, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k - 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k - 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k + 1, this.cyprusWoodBlock, this.woodMeta);
            }
            if (n == 0) {
                this.setBlockAndMetadata(i - 2, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k - 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 3, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k + 2, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 3, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k - 1, this.cyprusWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k - 2, this.cyprusWoodBlock, this.woodMeta);
            }
        }
        for (int n = 5; n < l - 2; ++n) {
            if (this.rand.nextInt(l + 10) < n) {
                this.growBranch(i, j + n, k, (double) (l - n) * 0.618, dir, pitch, 0.0, 0.0, 0);
            }
            dir += spin;
        }
        return true;
    }

    private void growBranch(int i, int j, int k, double len, double dir, double pitch, double pbias, double pbias2, int size) {
        double dx = 0.0, dy = 0.0, dz = 0.0;
        double spin = 0.0, heave = 0.0;
        double blen = len * 0.75;
        this.plotWood(i, j, k, size);
        while (len > 1.0) {
            dy += Math.sin(pitch);
            double dd = Math.cos(pitch);
            dx += Math.cos(dir) * dd;
            dz += Math.sin(dir) * dd;
            boolean step = false;
            if (dx >= 1.0) { ++i; dx -= 1.0; step = true; }
            else if (dx <= -1.0) { --i; dx += 1.0; step = true; }
            if (dy >= 1.0) { ++j; dy -= 1.0; step = true; }
            else if (dy <= -1.0) { --j; dy += 1.0; step = true; }
            if (dz >= 1.0) { ++k; dz -= 1.0; step = true; }
            else if (dz <= -1.0) { --k; dz += 1.0; step = true; }

            if (step) {
                Block id = this.getBlock(i, j, k);
                if (!BTBlockHelper.isReplaceableByTree(this.wo, new BlockPos(i, j, k)) && id != this.cyprusWoodBlock && id != this.cyprusLeafBlock) break;
                this.plotWood(i, j, k, size);
            }
            if (len > 2.0 && len < blen) {
                int id1 = size == 0 ? this.rand.nextInt(8) : 0;
                if (id1 == 1) this.growBranch(i, j, k, len, dir + 0.7853981633974483, 0.0, 0.0, 0.0, 1);
                if (id1 == 2) this.growBranch(i, j, k, len, dir - 0.7853981633974483, 0.0, 0.0, 0.0, 1);
            }
            heave += (double) this.rand.nextFloat() * 0.1 - 0.05;
            spin += (double) this.rand.nextFloat() * 0.1 - 0.05;
            dir += spin;
            heave = pitch > pbias ? (heave - 0.01) : (heave + 0.01);
            heave = Math.max(-0.2, Math.min(0.2, heave));
            pitch += heave;
            pitch = (pitch - pbias2) * 0.8 + pbias2;
            len -= 1.0;
        }
        this.treeLeaf(i, j, k, 3);
    }

    private void plotWood(int i, int j, int k, int size) {
        this.setBlockAndMetadata(i, j, k, this.cyprusWoodBlock, this.woodMeta);
    }

    void treeLeaf(int i, int j, int k, int r) {
        if (r <= 0) return;
        int rr = r * r + 1;
        for (int ii = -r; ii <= r; ++ii) {
            for (int jj = 0; jj <= 1; ++jj) {
                for (int kk = -r; kk <= r; ++kk) {
                    if (ii * ii + jj * jj + kk * kk <= rr) {
                        BlockPos pos = new BlockPos(i + ii, j + jj, k + kk);
                        if (BTBlockHelper.isReplaceableByTree(this.wo, pos)) {
                            this.setBlockAndMetadata(i + ii, j + jj, k + kk, this.cyprusLeafBlock, this.leafMeta);
                        }
                    }
                }
            }
        }
    }

    private int getMedium(int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = this.wo.getBlockState(pos);
        if (state.isAir() || state.is(Blocks.WATER) || BTBlockHelper.isReplaceableByTree(this.wo, pos)) {
            return 1;
        }
        Block qq = state.getBlock();
        if (BTBlockHelper.isSoil(qq) || qq == Blocks.SAND || qq == Blocks.GRAVEL) {
            return 2;
        }
        return 0;
    }

    void growRoot(int l, int m, int n, double theta, double phi) {
        if (!KTreeCfg.rootsEnable) return;
        if (this.rootAlt == 1) {
            this.rootRand = this.rand.nextInt(2);
            m -= this.rootRand;
            this.rootAlt = 2;
        } else if (this.rootAlt == 2) {
            if (this.rootRand == 0) --m;
            this.rootAlt = 0;
        } else if (this.rootAlt == 10) {
            m -= this.rand.nextInt(2);
        }
        ++m;
        theta += (double) this.rand.nextFloat() * 0.1 - 0.05;
        double direction = Math.PI * 2 * theta;
        double curl = this.rand.nextFloat() * 0.4f - 0.2f;
        phi -= (double) this.rand.nextFloat() * 0.05;
        double pitch = Math.PI * 2 * phi;
        int length = 20 + this.rand.nextInt(4);
        double x = l > 0 ? (double) l + 0.5 : (double) l - 0.5;
        double y = (double) m + 0.5;
        double z = n > 0 ? (double) n + 0.5 : (double) n - 0.5;
        int i = (int) x, j = (int) y, k = (int) z;
        int med = this.getMedium(i, j, k);
        int cnt = 0;
        while (length > 0) {
            --length;
            curl += (double) (this.rand.nextFloat() * 0.06f) - 0.03;
            boolean dug = !(pitch < 0.0);
            pitch = med == 1 ? (pitch + 1.5707963267948966) * 0.7 - 1.5707963267948966 : (pitch - 1.5707963267948966) * 0.7 + 1.5707963267948966;
            double hoz = Math.cos(pitch);
            double x2 = x + Math.cos(direction) * hoz;
            double y2 = y + Math.sin(pitch);
            double z2 = z + Math.sin(direction) * hoz;
            int i2 = (int) x2, j2 = (int) y2, k2 = (int) z2;
            if (i2 == i && j2 == j && k2 == k) continue;
            this.setBlockAndMetadata(i, j, k, this.cyprusWoodBlock, this.woodMeta);
            if (dug) {
                if (this.getBlock(i - 1, j, k) == Blocks.AIR || this.getBlock(i + 1, j, k) == Blocks.AIR ||
                    this.getBlock(i, j, k - 1) == Blocks.AIR || this.getBlock(i, j, k + 1) == Blocks.AIR) {
                    return;
                }
            }
            if (++cnt < 4 && (j2 != j - 1 || i2 != i || k2 != k)) {
                this.setBlockAndMetadata(i, j - 1, k, this.cyprusWoodBlock, this.woodMeta);
            }
            if ((med = this.getMedium(i2, j2, k2)) != 0) {
                x = x2; y = y2; z = z2;
                i = i2; j = j2; k = k2;
                continue;
            }
            med = this.getMedium(i, j - 1, k);
            if (med != 0) {
                y -= 1.0;
                --j;
                pitch = -1.5707963267948966;
                continue;
            }
            x2 = x + Math.cos(direction);
            i2 = (int) x2;
            z2 = z + Math.sin(direction);
            k2 = (int) z2;
            med = this.getMedium(i2, j, k2);
            if (med != 0) {
                x = x2; z = z2;
                i = i2; k = k2;
                pitch = 0.0;
                continue;
            }
            break;
        }
    }
}
