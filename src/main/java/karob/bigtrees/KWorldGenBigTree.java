package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class KWorldGenBigTree {
    static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
    Random rand = new Random();
    int rootRand = 0;
    int rootAlt = 0;
    int tapRootRand = 0;
    LevelAccessor worldObj;
    int[] basePos = new int[]{0, 0, 0};
    int heightLimit = 0;
    int height;
    double heightAttenuation = 0.318;
    double field_875_h = 1.0;
    double field_874_i = 0.681;
    double field_873_j = 1.0;
    double field_872_k = 1.0;
    int trunkSize = 1;
    int heightLimitLimit = 12;
    int leafDistanceLimit = 4;
    int[][] leafNodes;
    int type = 0;
    Block trunkBlock = Blocks.OAK_LOG;
    private int trunkMeta = 0;
    Block leafBlock = Blocks.OAK_LEAVES;
    private int leafMeta = 0;
    private int stuntmin = 7;
    private int heightmin = 12;
    private int heightmax = 18;
    private boolean doNotify = false;

    public KWorldGenBigTree(boolean doNotify) {
        this.doNotify = doNotify;
    }

    public void setConfigOptions(Block wood, Block leaf, int woodmeta, int leafmeta, Block Base1, Block Base2, int height1, int height2, int stunt) {
        this.trunkBlock = wood != null ? wood : Blocks.OAK_LOG;
        this.leafBlock = leaf != null ? leaf : Blocks.OAK_LEAVES;
        this.trunkMeta = woodmeta;
        this.leafMeta = leafmeta;
        this.heightmin = height1;
        this.heightmax = height2;
        this.stuntmin = stunt;
    }

    void setBlockAndMetadata(int x, int y, int z, Block block, int meta) {
        try {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state;
            if (block == this.leafBlock) {
                state = BTBlockHelper.getLeafState(block, meta, this.doNotify);
            } else {
                state = BTBlockHelper.getWoodState(block, meta);
            }
            this.worldObj.setBlock(pos, state, 3);
        } catch (Exception ignored) {
        }
    }

    Block getBlock(int x, int y, int z) {
        try {
            return this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();
        } catch (Exception e) {
            return Blocks.AIR;
        }
    }

    private boolean generateLeafNodeList() {
        this.height = (int) ((double) this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        if (this.basePos[1] + this.heightLimit > 310) {
            this.height /= 2;
            this.heightLimit /= 2;
            if (this.height >= this.heightLimit) {
                this.height = this.heightLimit - 1;
            }
            if (this.height < 1) return false;
            if (this.basePos[1] + this.heightLimit > 310) return false;
        }
        int i = (int) (1.382 + Math.pow(this.field_872_k * (double) this.heightLimit / 13.0, 2.0));
        if (i < 1) i = 1;

        int[][] ai = new int[i * this.heightLimit][4];
        int j = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
        int k = 1;
        int l = this.basePos[1] + this.height;
        int i1 = j - this.basePos[1];
        ai[0][0] = this.basePos[0];
        ai[0][1] = j--;
        ai[0][2] = this.basePos[2];
        ai[0][3] = l;
        while (i1 >= 0) {
            int j1 = 0;
            float f = this.func_528_a(i1);
            if (k >= i * this.heightLimit) {
                f = -1.0f;
            }
            if (f < 0.0f) {
                --j;
                --i1;
                continue;
            }
            double d = 0.5;
            while (j1 < i) {
                double d1 = this.field_873_j * ((double) f * ((double) this.rand.nextFloat() + 0.328));
                double d2 = (double) this.rand.nextFloat() * 2.0 * Math.PI;
                int k1 = Mth.floor(d1 * Math.sin(d2) + (double) this.basePos[0] + d);
                int l1 = Mth.floor(d1 * Math.cos(d2) + (double) this.basePos[2] + d);
                int[] ai1 = new int[]{k1, j, l1};
                int[] ai2 = new int[]{k1, j + this.leafDistanceLimit, l1};
                if (this.checkBlockLine(ai1, ai2) == -1) {
                    int[] ai3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                    ai3[1] = l;
                    if (this.checkBlockLine(ai3, ai1) == -1) {
                        ai[k][0] = k1;
                        ai[k][1] = j;
                        ai[k][2] = l1;
                        ai[k][3] = ai3[1];
                        ++k;
                    }
                }
                ++j1;
            }
            --j;
            --i1;
        }
        this.leafNodes = new int[k][4];
        System.arraycopy(ai, 0, this.leafNodes, 0, k);
        return true;
    }

    void func_523_a(int i, int j, int k, float f, byte byte0) {
        int i1 = (int) ((double) f + 0.618);
        byte byte1 = otherCoordPairs[byte0];
        byte byte2 = otherCoordPairs[byte0 + 3];
        int[] ai = new int[]{i, j, k};
        int[] ai1 = new int[]{0, 0, 0};
        ai1[byte0] = ai[byte0];
        for (int j1 = -i1; j1 <= i1; ++j1) {
            ai1[byte1] = ai[byte1] + j1;
            int l1 = -i1;
            while (l1 <= i1) {
                double d = Math.sqrt(Math.pow((double) Math.abs(j1) + 0.5, 2.0) + Math.pow((double) Math.abs(l1) + 0.5, 2.0));
                if (d <= (double) f) {
                    ai1[byte2] = ai[byte2] + l1;
                    BlockPos pos = new BlockPos(ai1[0], ai1[1], ai1[2]);
                    if (BTBlockHelper.isReplaceableByTree(this.worldObj, pos)) {
                        this.setBlockAndMetadata(ai1[0], ai1[1], ai1[2], this.leafBlock, this.leafMeta);
                    }
                }
                ++l1;
            }
        }
    }

    float func_528_a(int i) {
        if (this.trunkSize == 0) {
            return (float) this.heightLimit - this.rand.nextFloat();
        }
        if (this.trunkSize != 3 && this.trunkSize > 1 && this.rand.nextFloat() > 0.7f) {
            return -1.618f;
        }
        if (this.trunkSize == 3 && (double) i < (double) this.heightLimit * 0.2) {
            return -1.618f;
        }
        if (this.trunkSize < 3 && (double) i < (double) this.heightLimit * 0.3) {
            return -1.618f;
        }
        if (this.trunkSize == 4 && (double) i < (double) this.heightLimit * 0.16) {
            return -1.618f;
        }
        float f = (float) this.heightLimit / 2.0f;
        float f1 = (float) this.heightLimit / 2.0f - (float) i;
        float f2 = f1 == 0.0f ? f : (Math.abs(f1) >= f ? 0.0f : (float) Math.sqrt(Math.pow(Math.abs(f), 2.0) - Math.pow(Math.abs(f1), 2.0)));
        return f2 * 0.5f;
    }

    float func_526_b(int i) {
        if (i < 0 || i >= this.leafDistanceLimit) {
            return -1.0f;
        }
        return (i != 0 && i != this.leafDistanceLimit - 1) ? 3.0f : 2.0f;
    }

    void generateLeafNode(int i, int j, int k) {
        int i1 = j + this.leafDistanceLimit;
        for (int l = j; l < i1; ++l) {
            float f = this.func_526_b(l - j);
            this.func_523_a(i, l, k, f, (byte) 1);
        }
    }

    void placeBlockLine(int[] ai, int[] ai1) {
        int[] ai2 = new int[]{0, 0, 0};
        int j = 0;
        for (int byte0 = 0; byte0 < 3; byte0++) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
        }
        if (ai2[j] == 0) return;

        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        int byte3 = ai2[j] > 0 ? 1 : -1;
        double d = (double) ai2[byte1] / (double) ai2[j];
        double d1 = (double) ai2[byte2] / (double) ai2[j];
        int[] ai3 = new int[]{0, 0, 0};
        int l = ai2[j] + byte3;
        for (int k = 0; k != l; k += byte3) {
            ai3[j] = Mth.floor((double) (ai[j] + k) + 0.5);
            ai3[byte1] = Mth.floor((double) ai[byte1] + (double) k * d + 0.5);
            ai3[byte2] = Mth.floor((double) ai[byte2] + (double) k * d1 + 0.5);
            this.setBlockAndMetadata(ai3[0], ai3[1], ai3[2], this.trunkBlock, this.trunkMeta);
        }
    }

    void generateLeaves() {
        for (int[] node : this.leafNodes) {
            this.generateLeafNode(node[0], node[1], node[2]);
        }
    }

    boolean leafNodeNeedsBase(int i) {
        if (this.trunkSize != 2) return true;
        return (double) i >= (double) this.heightLimit * 0.2;
    }

    void generateTrunk() {
        int i = this.basePos[0];
        int j = this.basePos[1];
        int k = this.basePos[1] + this.height + 2;
        int l = this.basePos[2];
        int[] ai = new int[]{i, j, l};
        int[] ai1 = new int[]{i, k, l};

        if (this.trunkSize == 1) {
            this.placeBlockLine(ai, ai1);
        } else if (this.trunkSize == 2) {
            this.rootAlt = 0;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1] - 2, ai[2], 0.625, -0.0625);
            this.rootAlt = 1;
            this.growRoot(ai[0], ai[1], ai[2], 0.7125, -0.0625);
            this.placeBlockLine(ai, ai1);

            ai[0] += 1; ai1[0] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1], ai[2], 0.7875, -0.0625);
            this.growRoot(ai[0], ai[1] - 2, ai[2], 0.875, -0.0625);
            this.rootAlt = 1;
            this.growRoot(ai[0], ai[1], ai[2], 0.9625, -0.0625);
            this.placeBlockLine(ai, ai1);

            ai[2] += 1; ai1[2] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1], ai[2], 0.0375, -0.0625);
            this.growRoot(ai[0], ai[1] - 2, ai[2], 0.125, -0.0625);
            this.rootAlt = 1;
            this.growRoot(ai[0], ai[1], ai[2], 0.2125, -0.0625);
            this.placeBlockLine(ai, ai1);

            ai[0] -= 1; ai1[0] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1], ai[2], 0.2875, -0.0625);
            this.growRoot(ai[0], ai[1] - 2, ai[2], 0.375, -0.0625);
            this.rootAlt = 1;
            this.growRoot(ai[0], ai[1], ai[2], 0.4625, -0.0625);
            this.placeBlockLine(ai, ai1);
            this.growRoot(ai[0], ai[1], ai[2] - 1, 0.5375, -0.0625);
        } else if (this.trunkSize == 3) {
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.placeBlockLine(ai, ai1);
            ai[0] += 1; ai1[0] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.placeBlockLine(ai, ai1);
            ai[2] += 1; ai1[2] += 1; ai[0] -= 1; ai1[0] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.placeBlockLine(ai, ai1);
            ai[2] -= 1; ai1[2] -= 1; ai[0] -= 1; ai1[0] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.placeBlockLine(ai, ai1);
            ai[2] -= 1; ai1[2] -= 1; ai[0] += 1; ai1[0] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.placeBlockLine(ai, ai1);
        } else if (this.trunkSize == 4) {
            this.rootAlt = 10;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.625, -0.0625);
            ai[0] += 1; ai1[0] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.875, -0.0625);
            ai[2] += 1; ai1[2] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.125, -0.0625);
            ai[0] -= 1; ai1[0] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 1.0);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.375, -0.0625);
            ai[0] -= 1; ai1[0] -= 1; ai[2] -= 1; ai1[2] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.55, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] += 1; ai1[0] += 1; ai[2] -= 1; ai1[2] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.7, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] += 1; ai1[0] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.8, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] += 1; ai1[0] += 1; ai[2] += 1; ai1[2] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.95, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[2] += 1; ai1[2] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.05, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] -= 1; ai1[0] -= 1; ai[2] += 1; ai1[2] += 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.2, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] -= 1; ai1[0] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.3, -0.0625);
            this.placeBlockLine(ai, ai1);
            ai[0] -= 1; ai1[0] -= 1; ai[2] -= 1; ai1[2] -= 1;
            this.growTapRoot(ai[0], ai[1], ai[2], 0.5);
            this.growRoot(ai[0], ai[1] + 1, ai[2], 0.45, -0.0625);
            this.placeBlockLine(ai, ai1);
        }
    }

    private int getMedium(int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = this.worldObj.getBlockState(pos);
        if (state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || BTBlockHelper.isReplaceableByTree(this.worldObj, pos)) {
            return 1;
        }
        Block qq = state.getBlock();
        if (BTBlockHelper.isSoil(qq) || qq == Blocks.SAND || qq == Blocks.GRAVEL) {
            return 2;
        }
        return 0;
    }

    void growTapRoot(int i, int j, int k, double flen) {
        if (!KTreeCfg.rootsEnable) return;
        int len = (int) ((6.0 + (double) this.rand.nextFloat() * 6.0) * flen);
        if (len == this.tapRootRand || len == this.tapRootRand + 1 || len == this.tapRootRand - 1) {
            len = (int) ((6.0 + (double) this.rand.nextFloat() * 6.0) * flen);
        }
        for (int jj = 1; jj <= len; ++jj) {
            int med = this.getMedium(i, j - jj, k);
            if (med == 1) {
                --len;
                continue;
            }
            if (med != 0) continue;
            len = Math.min(len, jj - 1);
            break;
        }
        this.tapRootRand = len;
        for (int jj = 1; jj <= len; ++jj) {
            this.setBlockAndMetadata(i, j - jj, k, this.trunkBlock, this.trunkMeta);
        }
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
        int length = 2 + 3 * this.trunkSize + this.rand.nextInt(2);
        double x = l > 0 ? (double) l + 0.5 : (double) l - 0.5;
        double y = (double) m + 0.5;
        double z = n > 0 ? (double) n + 0.5 : (double) n - 0.5;
        int i = (int) x;
        int j = (int) y;
        int k = (int) z;
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
            int i2 = (int) x2;
            int j2 = (int) y2;
            int k2 = (int) z2;
            if (i2 == i && j2 == j && k2 == k) continue;
            this.setBlockAndMetadata(i, j, k, this.trunkBlock, this.trunkMeta);
            if (++cnt < 4 && (j2 != j - 1 || i2 != i || k2 != k)) {
                this.setBlockAndMetadata(i, j - 1, k, this.trunkBlock, this.trunkMeta);
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

    void generateLeafNodeBases() {
        int[] ai = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
        for (int[] ai1 : this.leafNodes) {
            int[] ai2 = new int[]{ai1[0], ai1[1], ai1[2]};
            ai[1] = ai1[3];
            int k = ai[1] - this.basePos[1];
            if (this.leafNodeNeedsBase(k)) {
                this.placeBlockLine(ai, ai2);
            }
        }
    }

    int checkBlockLine(int[] ai, int[] ai1) {
        int[] ai2 = new int[]{0, 0, 0};
        int i = 0;
        for (int byte0 = 0; byte0 < 3; byte0++) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[i])) {
                i = byte0;
            }
        }
        if (ai2[i] == 0) return -1;

        byte byte1 = otherCoordPairs[i];
        byte byte2 = otherCoordPairs[i + 3];
        int byte3 = ai2[i] > 0 ? 1 : -1;
        double d = (double) ai2[byte1] / (double) ai2[i];
        double d1 = (double) ai2[byte2] / (double) ai2[i];
        int[] ai3 = new int[]{0, 0, 0};
        int k = ai2[i] + byte3;
        int j;
        for (j = 0; j != k; j += byte3) {
            ai3[i] = ai[i] + j;
            ai3[byte1] = Mth.floor((double) ai[byte1] + (double) j * d);
            ai3[byte2] = Mth.floor((double) ai[byte2] + (double) j * d1);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            if (!BTBlockHelper.isReplaceableByTree(this.worldObj, pos) && this.worldObj.getBlockState(pos).getBlock() != this.trunkBlock) {
                break;
            }
        }
        if (j == k) return -1;
        return Math.abs(j);
    }

    boolean validTreeLocation() {
        BlockPos below = new BlockPos(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
        Block i = this.worldObj.getBlockState(below).getBlock();
        return BTBlockHelper.isSoil(i) || i == Blocks.SAND || i == Blocks.GRAVEL;
    }

    public boolean func_76484_a(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 0;
        return this.generator(world, random, i, j, k);
    }

    public boolean desertGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 1;
        return this.generator(world, random, i, j, k);
    }

    public boolean desertGenerate2(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 11;
        return this.generator(world, random, i, j, k);
    }

    public boolean swampGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 2;
        return this.generator(world, random, i, j, k);
    }

    public boolean greatGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 3;
        return this.generator(world, random, i, j, k);
    }

    public boolean pineGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 4;
        return this.generator(world, random, i, j, k);
    }

    public boolean blockOakGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 5;
        return this.generator(world, random, i, j, k);
    }

    public boolean birchGenerate(LevelAccessor world, Random random, int i, int j, int k) {
        this.type = 6;
        return this.generator(world, random, i, j, k);
    }

    private boolean generator(LevelAccessor world, Random random, int i, int j, int k) {
        this.worldObj = world;
        long l = random.nextLong();
        this.rand.setSeed(l);
        this.basePos[0] = i;
        this.basePos[1] = j;
        this.basePos[2] = k;
        boolean qbirch = false;
        int qq = this.type == 1 ? this.rand.nextInt(8) + 86 : (this.type == 2 ? 0 : (this.type == 3 ? 0 : (this.type == 4 ? 30 : (this.type == 5 || this.type == 6 ? 80 : this.rand.nextInt(94)))));
        if (this.type == 6) {
            qbirch = true;
        }
        if (this.type == 11) {
            this.type = 1;
            qq = 87;
        }
        int[] heightvector = new int[]{this.heightmin, Math.max(1, this.heightmax - this.heightmin)};
        this.heightLimit = KTreeCfg.vary(this.rand, heightvector);
        if (qq < 8) {
            this.heightAttenuation = 0.1;
            this.field_873_j = 1.4;
            this.trunkSize = 4;
            this.heightLimitLimit = 4;
            this.leafDistanceLimit = 4;
        } else if (qq < 40) {
            this.heightAttenuation = 0.3;
            this.field_873_j = 1.2;
            this.trunkSize = 3;
            this.heightLimitLimit = 3;
            this.leafDistanceLimit = 4;
        } else if (qq < 90) {
            this.heightAttenuation = 0.3;
            this.field_873_j = 1.0;
            this.trunkSize = 2;
            this.heightLimitLimit = 3;
            this.leafDistanceLimit = 4;
            if (qbirch) {
                this.heightLimit = KTreeCfg.vary(this.rand, KTreeCfg.birchHeight);
            }
        } else if (qq < 95) {
            this.heightAttenuation = 0.3;
            this.field_873_j = 1.0;
            this.trunkSize = 2;
            this.heightLimitLimit = 3;
            this.leafDistanceLimit = 0;
        } else {
            this.heightLimit = 5;
            this.heightAttenuation = 0.1;
            this.field_873_j = 2.0;
            this.trunkSize = 0;
            this.heightLimitLimit = 0;
            this.leafDistanceLimit = 0;
            this.trunkBlock = Blocks.COBBLESTONE;
            this.leafBlock = Blocks.COBBLESTONE;
        }
        if (this.type == 1) {
            if (this.trunkSize != 1) {
                this.field_873_j *= 1.0;
            }
            this.leafDistanceLimit = 0;
        } else if (this.type == 2) {
            this.heightAttenuation = 0.0;
        }
        if (this.heightLimitLimit > 0) {
            this.heightLimit = this.heightLimit + this.rand.nextInt(this.heightLimitLimit * 2) - this.heightLimitLimit;
        }
        if (!this.validTreeLocation()) {
            return false;
        }
        this.rootRand = this.rand.nextInt(4);
        if (this.generateLeafNodeList()) {
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            return true;
        }
        return false;
    }
}
