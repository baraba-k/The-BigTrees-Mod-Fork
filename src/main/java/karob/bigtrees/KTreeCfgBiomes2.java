package karob.bigtrees;

import java.io.File;
import java.util.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class KTreeCfgBiomes2 {
    public static BTConfigFile config;
    private static final Map<String, int[]> customBiomePopulations = new java.util.concurrent.ConcurrentHashMap<>();

    public static void init(File configFile) {
        config = new BTConfigFile(configFile);
        config.load();
        config.save();
    }

    public static int getPopulation(Holder<Biome> biome, int currentTree) {
        int numTrees = Math.max(9, KTreeCfgTrees.count);
        String biomeKey = biome.unwrapKey().map(k -> k.location().toString()).orElse("unknown");
        int[] cached = customBiomePopulations.get(biomeKey);
        if (cached == null) {
            cached = new int[numTrees];
            int group = KTreeCfgBiomes.getPrimaryGroup(biome);
            for (int i = 0; i < numTrees; i++) {
                int defPop = (KTreeCfgBiomes.TreeDensity != null && group < KTreeCfgBiomes.TreeDensity.length && i < KTreeCfgBiomes.TreeDensity[group].length)
                        ? KTreeCfgBiomes.TreeDensity[group][i] : 0;
                String treeName = (i < KTreeCfgTrees.treeName.length && KTreeCfgTrees.treeName[i] != null) ? KTreeCfgTrees.treeName[i] : ("Tree " + i);
                cached[i] = config.get("Tree populations for biomes", biomeKey + " " + treeName, defPop).getInt();
            }
            customBiomePopulations.put(biomeKey, cached);
        }
        return (currentTree >= 0 && currentTree < cached.length) ? cached[currentTree] : 0;
    }
}
