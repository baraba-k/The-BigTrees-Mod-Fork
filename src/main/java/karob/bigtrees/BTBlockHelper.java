package karob.bigtrees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BTBlockHelper {

    public static BlockState getWoodState(Block block, int meta) {
        if (block == null || block == Blocks.AIR) {
            block = Blocks.OAK_LOG;
        }
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (name.contains("log") || name.contains("wood")) {
            switch (meta & 3) {
                case 1:
                    return Blocks.SPRUCE_LOG.defaultBlockState();
                case 2:
                    return Blocks.BIRCH_LOG.defaultBlockState();
                case 3:
                    return Blocks.JUNGLE_LOG.defaultBlockState();
                case 0:
                default:
                    if (name.contains("log2") || name.contains("dark") || name.contains("acacia")) {
                        return (meta == 1) ? Blocks.DARK_OAK_LOG.defaultBlockState() : Blocks.ACACIA_LOG.defaultBlockState();
                    }
                    return Blocks.OAK_LOG.defaultBlockState();
            }
        }
        return block.defaultBlockState();
    }

    public static BlockState getWoodStateWithAxis(Block block, int meta, Direction.Axis axis) {
        BlockState state = getWoodState(block, meta);
        if (state.hasProperty(RotatedPillarBlock.AXIS)) {
            return state.setValue(RotatedPillarBlock.AXIS, axis);
        }
        return state;
    }

    public static BlockState getLeafState(Block block, int meta, boolean persistent) {
        if (block == null || block == Blocks.AIR) {
            block = Blocks.OAK_LEAVES;
        }
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        BlockState state;
        switch (meta & 3) {
            case 1:
                state = Blocks.SPRUCE_LEAVES.defaultBlockState();
                break;
            case 2:
                state = Blocks.BIRCH_LEAVES.defaultBlockState();
                break;
            case 3:
                state = Blocks.JUNGLE_LEAVES.defaultBlockState();
                break;
            case 0:
            default:
                if (name.contains("leaves2") || name.contains("dark") || name.contains("acacia")) {
                    state = (meta == 1) ? Blocks.DARK_OAK_LEAVES.defaultBlockState() : Blocks.ACACIA_LEAVES.defaultBlockState();
                } else {
                    state = Blocks.OAK_LEAVES.defaultBlockState();
                }
                break;
        }
        if (state.hasProperty(LeavesBlock.PERSISTENT)) {
            state = state.setValue(LeavesBlock.PERSISTENT, persistent);
        }
        return state;
    }

    public static Block resolveBlock(String name, Block fallback) {
        if (name == null || name.isEmpty()) return fallback;
        if (name.equals("log")) return Blocks.OAK_LOG;
        if (name.equals("log2")) return Blocks.ACACIA_LOG;
        if (name.equals("leaves")) return Blocks.OAK_LEAVES;
        if (name.equals("leaves2")) return Blocks.ACACIA_LEAVES;
        if (name.equals("dirt")) return Blocks.DIRT;
        if (name.equals("grass")) return Blocks.GRASS_BLOCK;

        ResourceLocation loc;
        if (name.contains(":")) {
            loc = ResourceLocation.parse(name);
        } else {
            loc = ResourceLocation.withDefaultNamespace(name);
        }
        if (BuiltInRegistries.BLOCK.containsKey(loc)) {
            return BuiltInRegistries.BLOCK.get(loc);
        }
        return fallback;
    }

    public static boolean isReplaceableByTree(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return true;
        if (state.is(BlockTags.LEAVES)) return true;
        if (state.is(BlockTags.FLOWERS) || state.is(BlockTags.CROPS) || state.is(BlockTags.SAPLINGS)) return true;
        if (state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN)) return true;
        if (state.is(Blocks.DEAD_BUSH) || state.is(Blocks.VINE) || state.is(Blocks.SNOW)) return true;
        if (state.is(Blocks.WATER)) return true;
        return state.canBeReplaced();
    }

    public static boolean isSoil(Block block) {
        if (block == null) return false;
        return block == Blocks.DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.COARSE_DIRT ||
               block == Blocks.PODZOL || block == Blocks.ROOTED_DIRT || block == Blocks.FARMLAND ||
               block == Blocks.MUD || block == Blocks.MUDDY_MANGROVE_ROOTS || block == Blocks.MYCELIUM ||
               block == Blocks.MOSS_BLOCK || block.defaultBlockState().is(BlockTags.DIRT);
    }
}
