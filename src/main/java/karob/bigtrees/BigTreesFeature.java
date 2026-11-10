package karob.bigtrees;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BigTreesFeature extends Feature<NoneFeatureConfiguration> {

    public BigTreesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        if (!KTreeCfg.passRealm(level.getLevel().dimension())) {
            return false;
        }
        BlockPos origin = context.origin();
        int chunkOriginX = (origin.getX() >> 4) << 4;
        int chunkOriginZ = (origin.getZ() >> 4) << 4;
        Holder<Biome> biome = level.getBiome(new BlockPos(chunkOriginX + 8, origin.getY(), chunkOriginZ + 8));
        Random rand = new Random(context.random().nextLong());
        return KTreeDecorate.decorate(level, rand, chunkOriginX, chunkOriginZ, biome);
    }
}
