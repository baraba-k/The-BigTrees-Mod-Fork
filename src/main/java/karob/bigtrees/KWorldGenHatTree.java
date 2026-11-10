package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

public class KWorldGenHatTree {
    LevelAccessor wo;
    Random rand;
    int rootRand;
    int rootAlt;
    private boolean planted;
    private int baseY;
    private Block hatWoodBlock = Blocks.OAK_LOG;
    private Block hatLeafBlock = Blocks.OAK_LEAVES;
    private Block hatBaseBlock1 = Blocks.GRASS_BLOCK;
    private Block hatBaseBlock2 = Blocks.DIRT;
    private int woodMeta = 0;
    private int leafMeta = 0;
    private int stuntmin = 7;
    private int heightmin = 28;
    private int heightmax = 32;

    public KWorldGenHatTree(boolean planted) {
        this.planted = planted;
        this.rootRand = 0;
        this.rootAlt = 0;
    }

    public void setConfigOptions(Block wood, Block leaf, int woodmeta, int leafmeta, Block base1, Block base2, int height1, int height2, int stunt) {
        this.hatWoodBlock = wood != null ? wood : Blocks.OAK_LOG;
        this.hatLeafBlock = leaf != null ? leaf : Blocks.OAK_LEAVES;
        this.hatBaseBlock1 = base1 != null ? base1 : Blocks.GRASS_BLOCK;
        this.hatBaseBlock2 = base2 != null ? base2 : Blocks.DIRT;
        this.woodMeta = woodmeta;
        this.leafMeta = leafmeta;
        this.heightmin = height1;
        this.heightmax = height2;
        this.stuntmin = stunt;
    }

    void setBlockAndMetadata(int x, int y, int z, Block block, int meta) {
        try {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state;
            if (block == this.hatLeafBlock) {
                state = BTBlockHelper.getLeafState(block, meta, this.planted);
            } else {
                state = BTBlockHelper.getWoodState(block, meta);
            }
            this.wo.setBlock(pos, state, 3);
        } catch (Exception ignored) {
        }
    }

    void setVine(int x, int y, int z, int l) {
        try {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState vine = Blocks.VINE.defaultBlockState();
            if (l == 1) vine = vine.setValue(VineBlock.SOUTH, true);
            else if (l == 2) vine = vine.setValue(VineBlock.WEST, true);
            else if (l == 4) vine = vine.setValue(VineBlock.NORTH, true);
            else if (l == 8) vine = vine.setValue(VineBlock.EAST, true);
            this.wo.setBlock(pos, vine, 3);
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
        return this.generateCustom(world, random, i, j, k, 4, 30);
    }

    public boolean generateCustom(LevelAccessor world, Random random, int i, int j, int k, int hatBase, int hatHeight) {
        this.wo = world;
        this.rand = random;
        this.baseY = j;
        int l = random.nextInt(Math.max(1, this.heightmax - this.heightmin)) + this.heightmin;
        if (j < 1) return false;
        if (j + l + 1 > 310 && (l = 310 - j - 2) < this.stuntmin) {
            return false;
        }

        if (!this.planted) {
            if (this.hatBaseBlock1 != Blocks.AIR || this.hatBaseBlock2 != Blocks.AIR) {
                boolean flag = false;
                Block id = this.getBlock(i, j - 1, k);
                if (this.hatBaseBlock1 != Blocks.AIR && (id == this.hatBaseBlock1 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (this.hatBaseBlock2 != Blocks.AIR && (id == this.hatBaseBlock2 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (!flag) return false;
            }
            Block id = this.getBlock(i, j, k);
            if (!BTBlockHelper.isReplaceableByTree(this.wo, new BlockPos(i, j, k)) && id != this.hatLeafBlock) {
                return false;
            }
        }

        double pbias = 0.0;
        double dir = (double) this.rand.nextFloat() * Math.PI / 2.0;
        double spin = 3.8830085198369844;
        double rootSlope = 0.0;

        this.growRoot(i, j, k - 1, 0.6625, rootSlope);
        this.growRoot(i + 1, j, k - 1, 0.8375, rootSlope);
        this.growRoot(i + 2, j, k, 0.9125, rootSlope);
        this.growRoot(i + 2, j, k + 1, 0.0875, rootSlope);
        this.growRoot(i + 1, j, k + 2, 0.1625, rootSlope);
        this.growRoot(i, j, k + 2, 0.3375, rootSlope);
        this.growRoot(i - 1, j, k + 1, 0.4125, rootSlope);
        this.growRoot(i - 1, j, k, 0.5875, rootSlope);

        if (hatBase == 4) {
            for (int n = 0; n < l; ++n) {
                this.setBlockAndMetadata(i, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k, this.hatWoodBlock, this.woodMeta);
            }
        } else if (hatBase == 5) {
            for (int n = 0; n < l - 1; ++n) {
                this.setBlockAndMetadata(i + 2, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k - 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k - 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k - 2, this.hatWoodBlock, this.woodMeta);
            }
        } else if (hatBase == 7) {
            for (int n = 0; n < l - 2; ++n) {
                this.setBlockAndMetadata(i + 3, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 3, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 3, j + n, k - 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 3, j + n, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 3, j + n, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k + 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k + 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k + 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 1, j + n, k - 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + n, k - 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + n, k - 3, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 2, j + n, k - 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k + 2, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i - 2, j + n, k - 2, this.hatWoodBlock, this.woodMeta);
            }
        }

        for (int n = 0; n < l; ++n) {
            this.growBranch(i, j + n, k, (double) (l - n) * 0.8, dir, 0.0, pbias, 0.0, 1);
            this.growBranch(i, j + n, k, (double) (l - n) * 0.8, dir + Math.PI, 0.0, pbias, 0.0, 1);
            dir += spin;
            pbias = (pbias - 1.5707963267948966) * 0.9 + 1.5707963267948966;
        }

        for (int n = 0; n < l - 1; ++n) {
            this.wo.setBlock(new BlockPos(i, j + n, k), Blocks.AIR.defaultBlockState(), 3);
            this.wo.setBlock(new BlockPos(i + 1, j + n, k), Blocks.AIR.defaultBlockState(), 3);
            this.wo.setBlock(new BlockPos(i + 1, j + n, k + 1), Blocks.AIR.defaultBlockState(), 3);
            this.wo.setBlock(new BlockPos(i, j + n, k + 1), Blocks.AIR.defaultBlockState(), 3);
        }
        if (hatBase >= 5) {
            for (int n = 0; n < l - 2; ++n) {
                this.wo.setBlock(new BlockPos(i - 1, j + n, k + 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 1, j + n, k), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 1, j + n, k - 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i, j + n, k - 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 1, j + n, k - 1), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        if (hatBase == 7) {
            for (int n = 0; n < l - 3; ++n) {
                this.wo.setBlock(new BlockPos(i - 2, j + n, k - 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 2, j + n, k), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 2, j + n, k + 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 2, j + n, k - 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 2, j + n, k), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 2, j + n, k + 1), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 1, j + n, k - 2), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i, j + n, k - 2), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 1, j + n, k - 2), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i - 1, j + n, k + 2), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i, j + n, k + 2), Blocks.AIR.defaultBlockState(), 3);
                this.wo.setBlock(new BlockPos(i + 1, j + n, k + 2), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return true;
    }

    private void growBranch(int i, int j, int k, double len, double dir, double pitch, double pbias, double pbias2, int size) {
        double dx = 0.0, dy = 0.0, dz = 0.0;
        double spin = 0.0, heave = 0.0;
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
                if (!BTBlockHelper.isReplaceableByTree(this.wo, new BlockPos(i, j, k)) && id != this.hatWoodBlock && id != this.hatLeafBlock) break;
                this.plotWood(i, j, k, size);
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
        this.setBlockAndMetadata(i, j, k, this.hatWoodBlock, this.woodMeta);
        if (size <= 1) {
            this.setBlockAndMetadata(i + 1, j, k, this.hatWoodBlock, this.woodMeta);
            this.setBlockAndMetadata(i + 1, j, k + 1, this.hatWoodBlock, this.woodMeta);
            this.setBlockAndMetadata(i, j, k + 1, this.hatWoodBlock, this.woodMeta);
            if (size == 0) {
                this.setBlockAndMetadata(i, j + 1, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + 1, k, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i + 1, j + 1, k + 1, this.hatWoodBlock, this.woodMeta);
                this.setBlockAndMetadata(i, j + 1, k + 1, this.hatWoodBlock, this.woodMeta);
            }
        }
    }

    private void treeLeaf(int i, int j, int k, int r) {
        if (r <= 0) return;
        int rr = r * r + 1;
        int rrr = (r - 1) * (r - 1);
        for (int ii = 0; ii <= r; ++ii) {
            for (int jj = -r / 2; jj <= r; ++jj) {
                for (int kk = 0; kk <= r; ++kk) {
                    int zz = ii * ii + jj * jj + kk * kk;
                    if (zz > rr) continue;
                    boolean flag = zz >= rrr;
                    this.treeLeafSpot(i + ii, j + jj, k + kk, flag);
                    this.treeLeafSpot(i + ii, j + jj, k - kk, flag);
                    this.treeLeafSpot(i - ii, j + jj, k + kk, flag);
                    this.treeLeafSpot(i - ii, j + jj, k - kk, flag);
                }
            }
        }
        for (int n = -r; n <= r; ++n) {
            if (this.getBlock(i + r, j, k) == this.hatLeafBlock) {
                this.plotVine(i + r, j, k, 1);
                this.plotVine(i + r, j, k, 4);
            }
            if (this.getBlock(i, j, k + r) == this.hatLeafBlock) {
                this.plotVine(i, j, k + r, 2);
                this.plotVine(i, j, k + r, 8);
            }
        }
    }

    private void treeLeafSpot(int i, int j, int k, boolean flag) {
        BlockPos pos = new BlockPos(i, j, k);
        if (BTBlockHelper.isReplaceableByTree(this.wo, pos)) {
            this.setBlockAndMetadata(i, j, k, this.hatLeafBlock, this.leafMeta);
        }
    }

    private void plotVine(int i, int j, int k, int l) {
        int ii, kk;
        if (l == 2) { ii = 1; kk = 0; }
        else if (l == 1) { ii = 0; kk = -1; }
        else if (l == 4) { ii = 0; kk = 1; }
        else { ii = -1; kk = 0; l = 8; }

        Block id = this.getBlock(i + ii, j, k + kk);
        for (int n = 0; n < 4 && id == this.hatLeafBlock; ++n) {
            i += ii;
            k += kk;
            id = this.getBlock(i + ii, j, k + kk);
        }
        if (this.getBlock(i, j, k) != Blocks.AIR) return;
        this.setVine(i, j, k, l);
        for (int i1 = 20; this.getBlock(i, --j, k) == Blocks.AIR && i1 > 0; --i1) {
            this.setVine(i, j, k, l);
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
        int length = 14 + this.rand.nextInt(2);
        double x = l > 0 ? (double) l + 0.5 : (double) l - 0.5;
        double y = (double) m + 0.5;
        double z = n > 0 ? (double) n + 0.5 : (double) n - 0.5;
        int i = (int) x, j = (int) y, k = (int) z;
        int med = this.getMedium(i, j, k);
        int cnt = 0;
        while (length > 0) {
            --length;
            curl += (double) (this.rand.nextFloat() * 0.06f) - 0.03;
            pitch = med == 1 ? (pitch + 1.5707963267948966) * 0.7 - 1.5707963267948966 : (pitch + 1.5707963267948966) * 0.9 - 1.5707963267948966;
            double hoz = Math.cos(pitch);
            double x2 = x + Math.cos(direction) * hoz;
            double y2 = y + Math.sin(pitch);
            double z2 = z + Math.sin(direction) * hoz;
            int i2 = (int) x2, j2 = (int) y2, k2 = (int) z2;
            if (i2 == i && j2 == j && k2 == k) continue;
            this.setBlockAndMetadata(i, j, k, this.hatWoodBlock, this.woodMeta);
            if (++cnt < 4 && (j2 != j - 1 || i2 != i || k2 != k)) {
                this.setBlockAndMetadata(i, j - 1, k, this.hatWoodBlock, this.woodMeta);
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
