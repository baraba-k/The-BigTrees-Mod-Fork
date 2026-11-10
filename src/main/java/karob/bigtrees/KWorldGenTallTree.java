package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class KWorldGenTallTree {
    LevelAccessor wo;
    Random rand;
    private int[][][] check;
    private boolean planted;
    private Block tallWoodBlock = Blocks.OAK_LOG;
    private Block tallLeafBlock = Blocks.OAK_LEAVES;
    private Block tallBaseBlock1 = Blocks.GRASS_BLOCK;
    private Block tallBaseBlock2 = Blocks.DIRT;
    private int woodMeta = 0;
    private int leafMeta = 0;
    private int heightmin = 13;
    private int heightmax = 28;
    private int stuntmin = 7;
    private double branchlessmin = 0.23;
    private double branchlessmax = 0.32;
    private double longestbranchp = 0.6;
    private double branchrot = 0.618034;
    private double taplength = 0.5;
    private double branchspace = 0.6;
    private double pitch = 0.0;
    private double curl = 0.08;
    private int leafrad = 3;
    private double subbranchdensity = 0.12;
    private double subbranchinglength = 0.6;
    private int subbranchingsize = 4;
    private double subbranchangle = 30.0;
    private double subbranchsize = 0.75;

    public KWorldGenTallTree(boolean planted) {
        this.planted = planted;
        this.check = new int[9][9][9];
    }

    public void setConfigOptions(Block wood, Block leaf, int woodmeta, int leafmeta, Block base1, Block base2,
                                 int height1, int height2, int stunt, double branchlessmin1, double branchlessmax1,
                                 double longestbranchp1, double branchrot1, double taplength1, double branchspace1,
                                 double pitch1, double curl1, int leafrad1, double subbranchdensity1,
                                 double subbranchinglength1, int subbranchingsize1, double subbranchangle1, double subbranchsize1) {
        this.tallWoodBlock = wood != null ? wood : Blocks.OAK_LOG;
        this.tallLeafBlock = leaf != null ? leaf : Blocks.OAK_LEAVES;
        this.tallBaseBlock1 = base1 != null ? base1 : Blocks.GRASS_BLOCK;
        this.tallBaseBlock2 = base2 != null ? base2 : Blocks.DIRT;
        this.woodMeta = woodmeta;
        this.leafMeta = leafmeta;
        this.heightmin = height1;
        this.heightmax = height2;
        this.stuntmin = stunt;
        this.branchlessmin = branchlessmin1;
        this.branchlessmax = branchlessmax1;
        this.longestbranchp = longestbranchp1;
        this.branchrot = branchrot1;
        this.taplength = taplength1;
        this.branchspace = branchspace1;
        this.pitch = pitch1;
        this.curl = curl1;
        this.leafrad = leafrad1;
        this.subbranchdensity = subbranchdensity1;
        this.subbranchinglength = subbranchinglength1;
        this.subbranchingsize = subbranchingsize1;
        this.subbranchangle = subbranchangle1;
        this.subbranchsize = subbranchsize1;
    }

    void setBlock(int x, int y, int z, Block block) {
        setBlockAndMetadata(x, y, z, block, 0);
    }

    void setBlockAndMetadata(int x, int y, int z, Block block, int meta) {
        try {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state;
            if (block == this.tallLeafBlock) {
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
        int l = random.nextInt(Math.max(1, this.heightmax - this.heightmin)) + this.heightmin;
        if (j < 1) return false;
        if (j + l + 1 > 310 && (l = 310 - j - 2) < this.stuntmin) {
            return false;
        }

        if (!this.planted) {
            if (this.tallBaseBlock1 != Blocks.AIR || this.tallBaseBlock2 != Blocks.AIR) {
                boolean flag = false;
                Block id = this.getBlock(i, j - 1, k);
                if (this.tallBaseBlock1 != Blocks.AIR && (id == this.tallBaseBlock1 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (this.tallBaseBlock2 != Blocks.AIR && (id == this.tallBaseBlock2 || BTBlockHelper.isSoil(id))) {
                    flag = true;
                }
                if (!flag) return false;
            }
            Block id = this.getBlock(i, j, k);
            if (!BTBlockHelper.isReplaceableByTree(this.wo, new BlockPos(i, j, k)) && id != this.tallLeafBlock) {
                return false;
            }
        }

        int ll;
        for (ll = 0; ll <= l; ++ll) {
            BlockPos pos = new BlockPos(i, j + ll, k);
            Block zz = this.getBlock(i, j + ll, k);
            if (!BTBlockHelper.isReplaceableByTree(this.wo, pos) && zz != this.tallWoodBlock && zz != this.tallLeafBlock) {
                break;
            }
        }
        if (ll < this.stuntmin) return false;

        int zzz = random.nextInt(Math.max(1, (int) (100.0 * (this.branchlessmax - this.branchlessmin) + this.branchlessmin)));
        int m = l * zzz / 100 + j;
        Block id = this.getBlock(i, j - 1, k);
        if (!this.planted && this.tallBaseBlock1 != Blocks.AIR && id == this.tallBaseBlock2) {
            this.setBlock(i, j - 1, k, this.tallBaseBlock1);
        }
        this.setBlockAndMetadata(i, j, k, this.tallWoodBlock, this.woodMeta);

        if (KTreeCfg.rootsEnable) {
            int jj = 1;
            while ((double) jj < (double) ll * this.taplength / 100.0) {
                BlockPos pos = new BlockPos(i, j - jj, k);
                Block zz = this.getBlock(i, j - jj, k);
                if (BTBlockHelper.isReplaceableByTree(this.wo, pos) || zz == this.tallWoodBlock || zz == this.tallLeafBlock || BTBlockHelper.isSoil(zz) || zz == Blocks.SAND || zz == Blocks.GRAVEL) {
                    this.setBlockAndMetadata(i, j - jj, k, this.tallWoodBlock, this.woodMeta);
                    ++jj;
                } else {
                    break;
                }
            }
        }

        double bd = (double) random.nextFloat() * 2.0 * Math.PI;
        boolean growflag = true;
        int jj = j;
        for (int j1 = j; j1 <= j + l; ++j1) {
            if (growflag) {
                BlockPos nextPos = new BlockPos(i, jj + 1, k);
                Block nextId = this.getBlock(i, jj + 1, k);
                if (BTBlockHelper.isReplaceableByTree(this.wo, nextPos) || nextId == this.tallWoodBlock || nextId == this.tallLeafBlock) {
                    this.setBlockAndMetadata(i, ++jj, k, this.tallWoodBlock, this.woodMeta);
                } else {
                    growflag = false;
                }
            }
            int branchs = Math.max(0, (int) (100.0 * this.branchspace));
            if (branchs > 0 && j1 >= m && random.nextInt(branchs) < 100 * (j1 - m) / (j + l - m + 1)) {
                double bl = (double) l * this.longestbranchp;
                bl = bl * (double) (j + l - jj + 1) / (double) (j + l - m + 1);
                this.treeBranch(i, jj, k, bd, bl, this.pitch, this.curl);
            }
            bd += this.branchrot;
            if (j1 == j + l && this.leafrad > 0) {
                this.treeLeaf(i, jj, k, this.leafrad);
            }
        }
        return true;
    }

    void treeBranch(int i, int j, int k, double dir, double len, double up, double crv) {
        if (len < 1.0) return;
        double xx = Math.cos(dir);
        double yy = up;
        double zz = Math.sin(dir);
        double dd = Math.sqrt(xx * xx + yy * yy + zz * zz);
        int xpol = xx > 0.0 ? 1 : -1;
        int ypol = yy > 0.0 ? 1 : -1;
        int zpol = zz > 0.0 ? 1 : -1;
        xx = Math.abs(xx) / dd;
        yy = Math.abs(yy) / dd;
        zz = Math.abs(zz) / dd;
        double dx = 0.0, dy = 0.0, dz = 0.0;
        int ii = i, jj = j, kk = k;

        double ll;
        for (ll = len; ll > 0.0; ll -= 1.0) {
            boolean chg = false;
            if ((dx += xx) >= 1.0) { ii += xpol; dx -= 1.0; chg = true; }
            if ((dy += yy) >= 1.0) { jj += ypol; dy -= 1.0; chg = true; }
            if ((dz += zz) >= 1.0) { kk += zpol; dz -= 1.0; chg = true; }
            if (chg) {
                BlockPos pos = new BlockPos(ii, jj, kk);
                Block id = this.getBlock(ii, jj, kk);
                if (!BTBlockHelper.isReplaceableByTree(this.wo, pos) && id != this.tallWoodBlock && id != this.tallLeafBlock) break;
                this.setBlockAndMetadata(ii, jj, kk, this.tallWoodBlock, this.woodMeta);
            }
            if ((double) this.rand.nextInt(100) < this.subbranchdensity && len >= (double) this.subbranchingsize && ll < len * this.subbranchinglength) {
                if (this.rand.nextInt(10) < 5) {
                    this.treeBranch(ii, jj, kk, dir + this.subbranchangle, ll * this.subbranchsize, yy, crv);
                } else {
                    this.treeBranch(ii, jj, kk, dir - this.subbranchangle, ll * this.subbranchsize, yy, crv);
                }
            }
            xx = Math.cos(dir);
            up += crv;
            yy = up;
            zz = Math.sin(dir);
            dd = Math.sqrt(xx * xx + yy * yy + zz * zz);
            xpol = xx > 0.0 ? 1 : -1;
            ypol = yy > 0.0 ? 1 : -1;
            zpol = zz > 0.0 ? 1 : -1;
            xx = Math.abs(xx) / dd;
            yy = Math.abs(yy) / dd;
            zz = Math.abs(zz) / dd;
        }
        if (ll <= len / 2.0 && this.leafrad > 0) {
            int klen = (int) (len / 2.0);
            if (klen >= this.leafrad) {
                this.treeLeaf(ii, jj, kk, this.leafrad);
            } else if (klen >= 1) {
                this.treeLeaf(ii, jj, kk, klen);
            } else {
                this.treeLeaf(ii, jj, kk, 1);
            }
        }
    }

    void treeLeaf(int i, int j, int k, int r) {
        if (r <= 0) return;
        if (r <= 3) {
            for (int ii = -r - 1; ii <= r + 1; ++ii) {
                for (int jj = -2; jj <= r + 1; ++jj) {
                    for (int kk = -r - 1; kk <= r + 1; ++kk) {
                        BlockPos pos = new BlockPos(i + ii, j + jj, k + kk);
                        Block zz = this.getBlock(i + ii, j + jj, k + kk);
                        this.check[ii + 4][jj + 4][kk + 4] = BTBlockHelper.isReplaceableByTree(this.wo, pos) ? 2 : (zz == this.tallLeafBlock ? 1 : 0);
                    }
                }
            }
            this.leafShell(i, j, k, 1);
            if (r >= 2) this.leafShell(i, j, k, 2);
            if (r == 3) this.leafShell(i, j, k, 3);
        } else {
            int rr = r * r;
            for (int ii = -r; ii <= r; ++ii) {
                for (int jj = -r / 2; jj <= r; ++jj) {
                    for (int kk = -r; kk <= r; ++kk) {
                        if (ii * ii + jj * jj + kk * kk <= rr) {
                            BlockPos pos = new BlockPos(i + ii, j + jj, k + kk);
                            if (BTBlockHelper.isReplaceableByTree(this.wo, pos) && this.rand.nextInt(3) == 0) {
                                this.setBlockAndMetadata(i + ii, j + jj, k + kk, this.tallLeafBlock, this.leafMeta);
                            }
                        }
                    }
                }
            }
        }
    }

    void leafShell(int i, int j, int k, int r) {
        int rr = r * r + 1;
        int rrr = (r - 1) * (r - 1);
        for (int ii = 0; ii <= r; ++ii) {
            for (int jj = 0; jj <= r; ++jj) {
                for (int kk = 0; kk <= r; ++kk) {
                    int zz = ii * ii + jj * jj + kk * kk;
                    if (zz > rr || zz <= rrr) continue;
                    this.plotLeaf(i, j, k, ii, jj, kk, r);
                    this.plotLeaf(i, j, k, ii, jj, -kk, r);
                    this.plotLeaf(i, j, k, -ii, jj, kk, r);
                    this.plotLeaf(i, j, k, -ii, jj, -kk, r);
                    if (jj < 1) {
                        this.plotLeaf(i, j, k, ii, -jj, kk, r);
                        this.plotLeaf(i, j, k, ii, -jj, -kk, r);
                        this.plotLeaf(i, j, k, -ii, -jj, kk, r);
                        this.plotLeaf(i, j, k, -ii, -jj, -kk, r);
                    } else if (jj == 1) {
                        if (this.rand.nextInt(3) > 0) this.plotLeaf(i, j, k, ii, -jj, kk, r);
                        if (this.rand.nextInt(3) > 0) this.plotLeaf(i, j, k, ii, -jj, -kk, r);
                        if (this.rand.nextInt(3) > 0) this.plotLeaf(i, j, k, -ii, -jj, kk, r);
                        if (this.rand.nextInt(3) > 0) this.plotLeaf(i, j, k, -ii, -jj, -kk, r);
                    }
                }
            }
        }
    }

    void plotLeaf(int i, int j, int k, int ii, int jj, int kk, int r) {
        if (this.check[ii + 4][jj + 4][kk + 4] != 2) return;
        boolean flag = false;
        if (r == 1) {
            flag = true;
        } else {
            if (this.check[ii + 3][jj + 4][kk + 4] == 1 || this.check[ii + 5][jj + 4][kk + 4] == 1 ||
                this.check[ii + 4][jj + 3][kk + 4] == 1 || this.check[ii + 4][jj + 5][kk + 4] == 1 ||
                this.check[ii + 4][jj + 4][kk + 3] == 1 || this.check[ii + 4][jj + 4][kk + 5] == 1 ||
                this.check[ii + 3][jj + 3][kk + 4] == 1 || this.check[ii + 3][jj + 5][kk + 4] == 1 ||
                this.check[ii + 3][jj + 4][kk + 3] == 1 || this.check[ii + 3][jj + 4][kk + 5] == 1 ||
                this.check[ii + 5][jj + 3][kk + 4] == 1 || this.check[ii + 5][jj + 5][kk + 4] == 1 ||
                this.check[ii + 5][jj + 4][kk + 3] == 1 || this.check[ii + 5][jj + 4][kk + 5] == 1 ||
                this.check[ii + 4][jj + 3][kk + 3] == 1 || this.check[ii + 4][jj + 3][kk + 5] == 1 ||
                this.check[ii + 4][jj + 5][kk + 3] == 1 || this.check[ii + 4][jj + 5][kk + 5] == 1) {
                flag = true;
            }
        }
        if (flag) {
            this.setBlockAndMetadata(i + ii, j + jj, k + kk, this.tallLeafBlock, this.leafMeta);
            this.check[ii + 4][jj + 4][kk + 4] = 1;
        }
    }
}
