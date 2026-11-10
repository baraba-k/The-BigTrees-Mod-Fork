package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBTSapling extends BushBlock implements BonemealableBlock {
    public static final com.mojang.serialization.MapCodec<BlockBTSapling> CODEC = simpleCodec(props -> new BlockBTSapling(TreeType.BIG_OAK));
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

    @Override
    protected com.mojang.serialization.MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public enum TreeType {
        BIG_OAK, BIG_BIRCH, BIG_PINE, DEAD_TREE, CYPRUS, HAT_TREE
    }

    private final TreeType treeType;
    private int x_offset;
    private int z_offset;

    public BlockBTSapling(TreeType treeType) {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .noOcclusion()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS));
        this.treeType = treeType;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    public TreeType getTreeType() {
        return treeType;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(7) == 0) {
            this.advanceTree(level, pos, state, random);
        }
    }

    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.setValue(STAGE, 1), 4);
        } else {
            this.growTree(level, pos, state, new Random(random.nextLong()));
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Direct bonemeal growth attempt
        this.growTree(level, pos, state, new Random(random.nextLong()));
    }

    public void growTree(ServerLevel level, BlockPos pos, BlockState state, Random random) {
        if (!KTreeCfg.passRecipeRealm(level.dimension())) {
            return;
        }

        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        Block id = this;

        switch (this.treeType) {
            case BIG_OAK -> {
                if (this.checkTreeCoin4x4(level, i, j, k, id)) {
                    this.clearTreeCoin4x4(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 28, 32, 7);
                    if (!tree.greatGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeCoin4x4(level, i, j, k, id);
                    }
                } else if (this.checkTreeRing4x4(level, i, j, k, id)) {
                    this.clearTreeRing4x4(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 28, 32, 7);
                    if (!tree.swampGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeRing4x4(level, i, j, k, id);
                    }
                } else if (this.checkTreeBlock2x2(level, i, j, k, id)) {
                    this.clearTreeBlock2x2(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 12, 18, 7);
                    if (!tree.blockOakGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeBlock2x2(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenTallTree tree = new KWorldGenTallTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK,
                            28, 32, 7, 0.23, 0.32, 0.6, 0.618034, 0.5, 0.6, 0.0, 0.08, 3, 0.12, 0.6, 4, 30.0, 0.75);
                    if (!tree.func_76484_a(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
            case BIG_BIRCH -> {
                if (this.checkTreeBlock2x2(level, i, j, k, id)) {
                    this.clearTreeBlock2x2(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 2, 2, Blocks.DIRT, Blocks.GRASS_BLOCK, 19, 24, 7);
                    if (!tree.birchGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeBlock2x2(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 2, 2, Blocks.DIRT, Blocks.GRASS_BLOCK, 12, 16, 7);
                    if (!tree.birchGenerate(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
            case BIG_PINE -> {
                if (this.checkTreeCross3x3(level, i, j, k, id)) {
                    this.clearTreeCross3x3(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, 1, 1, Blocks.DIRT, Blocks.GRASS_BLOCK, 18, 22, 7);
                    if (!tree.pineGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeCross3x3(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, 1, 1, Blocks.DIRT, Blocks.GRASS_BLOCK, 14, 18, 7);
                    if (!tree.pineGenerate(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
            case DEAD_TREE -> {
                if (this.checkTreeBlock2x2(level, i, j, k, id)) {
                    this.clearTreeBlock2x2(level, i, j, k);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.AIR, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 13, 17, 6);
                    if (!tree.desertGenerate(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeBlock2x2(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenBigTree tree = new KWorldGenBigTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.AIR, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 9, 13, 6);
                    if (!tree.desertGenerate(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
            case CYPRUS -> {
                if (this.checkTreeBlock2x2(level, i, j, k, id)) {
                    this.clearTreeBlock2x2(level, i, j, k);
                    KWorldGenCyprusTree tree = new KWorldGenCyprusTree(true);
                    tree.setConfigOptions(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, 1, 1, Blocks.DIRT, Blocks.GRASS_BLOCK, 28, 32, 7);
                    if (!tree.func_76484_a(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeBlock2x2(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenCyprusTree tree = new KWorldGenCyprusTree(true);
                    tree.setConfigOptions(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, 1, 1, Blocks.DIRT, Blocks.GRASS_BLOCK, 18, 24, 7);
                    if (!tree.func_76484_a(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
            case HAT_TREE -> {
                if (this.checkTreeRing4x4(level, i, j, k, id)) {
                    this.clearTreeRing4x4(level, i, j, k);
                    KWorldGenHatTree tree = new KWorldGenHatTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 28, 32, 7);
                    if (!tree.func_76484_a(level, random, this.x_offset, j, this.z_offset)) {
                        this.resetTreeRing4x4(level, i, j, k, id);
                    }
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    KWorldGenHatTree tree = new KWorldGenHatTree(true);
                    tree.setConfigOptions(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 0, 0, Blocks.DIRT, Blocks.GRASS_BLOCK, 16, 22, 7);
                    if (!tree.func_76484_a(level, random, i, j, k)) {
                        level.setBlock(pos, state, 3);
                    }
                }
            }
        }
    }

    public boolean isBlock(Level level, int x, int y, int z, Block id) {
        Block b = level.getBlockState(new BlockPos(x, y, z)).getBlock();
        if (b == id) return true;
        if (b instanceof BlockBTSapling other && other.getTreeType() == this.treeType) return true;
        return false;
    }

    public boolean checkTreeBlock2x2(Level wo, int x, int y, int z, Block id) {
        if (isBlock(wo, x, y, z - 1, id)) {
            if (isBlock(wo, x - 1, y, z, id) && isBlock(wo, x - 1, y, z - 1, id)) {
                this.x_offset = x - 1;
                this.z_offset = z - 1;
                return true;
            }
            if (isBlock(wo, x + 1, y, z, id) && isBlock(wo, x + 1, y, z - 1, id)) {
                this.x_offset = x;
                this.z_offset = z - 1;
                return true;
            }
        }
        if (isBlock(wo, x, y, z + 1, id)) {
            if (isBlock(wo, x - 1, y, z, id) && isBlock(wo, x - 1, y, z + 1, id)) {
                this.x_offset = x - 1;
                this.z_offset = z;
                return true;
            }
            if (isBlock(wo, x + 1, y, z, id) && isBlock(wo, x + 1, y, z + 1, id)) {
                this.x_offset = x;
                this.z_offset = z;
                return true;
            }
        }
        return false;
    }

    public void clearTreeBlock2x2(Level wo, int x, int y, int z) {
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset + 1), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset + 1), Blocks.AIR.defaultBlockState(), 3);
    }

    public void resetTreeBlock2x2(Level wo, int x, int y, int z, Block id) {
        BlockState def = id.defaultBlockState();
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset), def, 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset), def, 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset + 1), def, 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset + 1), def, 3);
    }

    public boolean checkTreeCross3x3(Level wo, int x, int y, int z, Block id) {
        int summation = 0;
        if (isBlock(wo, x, y, z - 1, id)) {
            summation++;
            if (isBlock(wo, x - 1, y, z - 1, id) && isBlock(wo, x + 1, y, z - 1, id) && isBlock(wo, x, y, z - 2, id)) {
                this.x_offset = x;
                this.z_offset = z - 1;
                return true;
            }
        }
        if (isBlock(wo, x, y, z + 1, id)) {
            summation++;
            if (isBlock(wo, x - 1, y, z + 1, id) && isBlock(wo, x + 1, y, z + 1, id) && isBlock(wo, x, y, z + 2, id)) {
                this.x_offset = x;
                this.z_offset = z + 1;
                return true;
            }
        }
        if (isBlock(wo, x - 1, y, z, id)) {
            summation++;
            if (isBlock(wo, x - 1, y, z - 1, id) && isBlock(wo, x - 1, y, z + 1, id) && isBlock(wo, x - 2, y, z, id)) {
                this.x_offset = x - 1;
                this.z_offset = z;
                return true;
            }
        }
        if (isBlock(wo, x + 1, y, z, id)) {
            summation++;
            if (isBlock(wo, x + 1, y, z - 1, id) && isBlock(wo, x + 1, y, z + 1, id) && isBlock(wo, x + 2, y, z, id)) {
                this.x_offset = x + 1;
                this.z_offset = z;
                return true;
            }
        }
        if (summation == 4) {
            this.x_offset = x;
            this.z_offset = z;
            return true;
        }
        return false;
    }

    public void clearTreeCross3x3(Level wo, int x, int y, int z) {
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset - 1, y, this.z_offset), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset + 1), Blocks.AIR.defaultBlockState(), 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset - 1), Blocks.AIR.defaultBlockState(), 3);
    }

    public void resetTreeCross3x3(Level wo, int x, int y, int z, Block id) {
        BlockState def = id.defaultBlockState();
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset), def, 3);
        wo.setBlock(new BlockPos(this.x_offset + 1, y, this.z_offset), def, 3);
        wo.setBlock(new BlockPos(this.x_offset - 1, y, this.z_offset), def, 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset + 1), def, 3);
        wo.setBlock(new BlockPos(this.x_offset, y, this.z_offset - 1), def, 3);
    }

    public boolean checkTreeCoin4x4(Level wo, int x, int y, int z, Block id) {
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, 0, 1)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, -1, 1)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, 1, 0)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, -2, 0)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, 1, -1)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, -2, -1)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, 0, -2)) return true;
        if (this.checkTreeCoin4x4_sub(wo, x, y, z, id, -1, -2)) return true;
        return false;
    }

    private boolean checkTreeCoin4x4_sub(Level wo, int x, int y, int z, Block id, int xo, int zo) {
        int nx = x + xo;
        int nz = z + zo;
        int[] dx = {0, 1, -1, 0, 1, 2, -1, 0, 1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            if (!isBlock(wo, nx + dx[idx], y, nz + dz[idx], id)) return false;
        }
        this.x_offset = nx;
        this.z_offset = nz;
        return true;
    }

    public void clearTreeCoin4x4(Level wo, int x, int y, int z) {
        int[] dx = {0, 1, -1, 0, 1, 2, -1, 0, 1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            wo.setBlock(new BlockPos(this.x_offset + dx[idx], y, this.z_offset + dz[idx]), Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public void resetTreeCoin4x4(Level wo, int x, int y, int z, Block id) {
        BlockState def = id.defaultBlockState();
        int[] dx = {0, 1, -1, 0, 1, 2, -1, 0, 1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            wo.setBlock(new BlockPos(this.x_offset + dx[idx], y, this.z_offset + dz[idx]), def, 3);
        }
    }

    public boolean checkTreeRing4x4(Level wo, int x, int y, int z, Block id) {
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, 0, 1)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, -1, 1)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, 1, 0)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, -2, 0)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, 1, -1)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, -2, -1)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, 0, -2)) return true;
        if (this.checkTreeRing4x4_sub(wo, x, y, z, id, -1, -2)) return true;
        return false;
    }

    private boolean checkTreeRing4x4_sub(Level wo, int x, int y, int z, Block id, int xo, int zo) {
        int nx = x + xo;
        int nz = z + zo;
        int[] dx = {0, 1, -1, 2, -1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            if (!isBlock(wo, nx + dx[idx], y, nz + dz[idx], id)) return false;
        }
        this.x_offset = nx;
        this.z_offset = nz;
        return true;
    }

    public void clearTreeRing4x4(Level wo, int x, int y, int z) {
        int[] dx = {0, 1, -1, 2, -1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            wo.setBlock(new BlockPos(this.x_offset + dx[idx], y, this.z_offset + dz[idx]), Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public void resetTreeRing4x4(Level wo, int x, int y, int z, Block id) {
        BlockState def = id.defaultBlockState();
        int[] dx = {0, 1, -1, 2, -1, 2, 0, 1};
        int[] dz = {2, 2, 1, 1, 0, 0, -1, -1};
        for (int idx = 0; idx < dx.length; idx++) {
            wo.setBlock(new BlockPos(this.x_offset + dx[idx], y, this.z_offset + dz[idx]), def, 3);
        }
    }
}
