package karob.bigtrees;

import java.io.File;
import java.util.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class KTreeCfgBiomes {
    public static BTConfigFile config;
    public static final String[] PRIMARY_TYPES = {
        "FOREST", "SWAMP", "PLAINS", "MOUNTAIN", "HILLS", "WATER", "DESERT", "FROZEN", "JUNGLE"
    };
    public static int[][] defaultdensity;
    public static int[][] TreeDensity;

    public static void init(File configFile) {
        config = new BTConfigFile(configFile);
        config.load();
        int numTrees = Math.max(9, KTreeCfgTrees.count);
        defaultdensity = new int[PRIMARY_TYPES.length][numTrees];
        TreeDensity = new int[PRIMARY_TYPES.length][numTrees];

        for (int i = 0; i < numTrees; i++) {
            int alg = (i < KTreeCfgTrees.algore.length) ? KTreeCfgTrees.algore[i] : 0;
            int freq = (i < KTreeCfgTrees.frequencymultiplyer.length) ? KTreeCfgTrees.frequencymultiplyer[i] : 10;
            for (int k = 0; k < PRIMARY_TYPES.length; k++) {
                int base = switch (PRIMARY_TYPES[k]) {
                    case "FOREST" -> switch (alg) {
                        case 1 -> 50; case 2 -> 20; case 3 -> 5; case 4 -> 4; case 7 -> 9; case 8 -> 14; case 9 -> 1; default -> 0;
                    };
                    case "SWAMP" -> switch (alg) {
                        case 3, 4, 5, 6 -> 20; case 8 -> 1; default -> 0;
                    };
                    case "PLAINS" -> switch (alg) {
                        case 1, 7, 8 -> 1; case 2 -> 2; case 3 -> 3; default -> 0;
                    };
                    case "MOUNTAIN" -> (alg == 1) ? 10 : 0;
                    case "HILLS" -> switch (alg) {
                        case 1 -> 15; case 2 -> 6; case 3 -> 4; case 7 -> 7; case 8 -> 10; default -> 0;
                    };
                    case "WATER" -> switch (alg) {
                        case 1, 3, 8 -> 5; case 4 -> 2; case 7 -> 3; default -> 0;
                    };
                    case "DESERT" -> (alg == 9) ? 3 : 0;
                    case "FROZEN" -> (alg == 1) ? 2 : ((alg == 7) ? 10 : 0);
                    case "JUNGLE" -> (alg == 3) ? 10 : ((alg == 4) ? 5 : 0);
                    default -> 0;
                };
                defaultdensity[k][i] = base * freq;
                String treeName = (i < KTreeCfgTrees.treeName.length && KTreeCfgTrees.treeName[i] != null) ? KTreeCfgTrees.treeName[i] : ("Tree " + i);
                TreeDensity[k][i] = config.get("Tree populations for groups", PRIMARY_TYPES[k] + " " + treeName, defaultdensity[k][i]).getInt();
            }
        }
        config.save();
    }

    public static int getPrimaryGroup(Holder<Biome> biome) {
        if (biome.is(BiomeTags.IS_JUNGLE)) return 8; // JUNGLE
        if (biome.is(BiomeTags.IS_TAIGA)) return 7; // FROZEN / TAIGA
        if (biome.is(BiomeTags.HAS_DESERT_PYRAMID) || biome.is(Biomes.DESERT) || biome.is(Biomes.BADLANDS)) return 6; // DESERT
        if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_RIVER)) return 5; // WATER
        if (biome.is(BiomeTags.IS_MOUNTAIN)) return 3; // MOUNTAIN
        if (biome.is(BiomeTags.IS_HILL)) return 4; // HILLS
        if (biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)) return 1; // SWAMP
        if (biome.is(BiomeTags.IS_FOREST)) return 0; // FOREST
        return 2; // PLAINS default
    }
}
