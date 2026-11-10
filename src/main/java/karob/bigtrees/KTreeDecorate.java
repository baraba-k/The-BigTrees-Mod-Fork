package karob.bigtrees;

import java.util.Random;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class KTreeDecorate {

    public static boolean decorate(LevelAccessor world, Random rand, int posX, int posZ, Holder<Biome> biome) {
        int numberoftrees = Math.max(9, KTreeCfgTrees.count);
        int seed = 1337;
        int[] bigTreeDensity = new int[numberoftrees];
        int[] bigTreeType = new int[numberoftrees];

        for (int i = 0; i < numberoftrees; ++i) {
            bigTreeDensity[i] = KGetLocality.locality(posX, posZ, seed + 128 * i, 128);
            bigTreeType[i] = KGetLocality.locality(posX, posZ, seed + 128 * i + 64, 168);
        }

        boolean generatedAny = false;
        int minBuildY = world.getMinBuildHeight();
        int maxBuildY = world.getMaxBuildHeight();

        for (int current = 0; current < numberoftrees; ++current) {
            // Check locality constraint once per tree type before looping populations
            int loc = (current < KTreeCfgTrees.locality.length) ? KTreeCfgTrees.locality[current] : 0;
            int locMin = (current < KTreeCfgTrees.localitymin.length) ? KTreeCfgTrees.localitymin[current] : 0;
            int locMax = (current < KTreeCfgTrees.localitymax.length) ? KTreeCfgTrees.localitymax[current] : 144;

            boolean generatetree = false;
            if (loc > numberoftrees || loc < 1) {
                generatetree = true;
            } else if (bigTreeType[loc - 1] > locMin && bigTreeType[loc - 1] < locMax) {
                generatetree = true;
            }

            if (!generatetree) {
                continue;
            }

            int pop = KTreeCfgBiomes2.getPopulation(biome, current);
            if (pop <= 0) {
                continue;
            }

            int alg = (current < KTreeCfgTrees.algore.length) ? KTreeCfgTrees.algore[current] : 0;
            int rarityChance = Math.max(1, numberoftrees * 50);

            for (int j = 0; j < pop; ++j) {
                // Optimization: Random test BEFORE expensive world.getHeight calls!
                if (rand.nextInt(rarityChance) != 0) {
                    continue;
                }

                int x = posX + rand.nextInt(16);
                int z = posZ + rand.nextInt(16);
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                if (y <= minBuildY + 2 || y >= maxBuildY - 20) {
                    continue;
                }

                if (alg == 1) {
                    KWorldGenTallTree tallTree = new KWorldGenTallTree(false);
                    tallTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current], KTreeCfgTrees.branchlessmin[current],
                            KTreeCfgTrees.branchlessmax[current], KTreeCfgTrees.longestbranchp[current],
                            KTreeCfgTrees.branchrot[current], KTreeCfgTrees.taplength[current],
                            KTreeCfgTrees.branchspace[current], KTreeCfgTrees.pitch[current],
                            KTreeCfgTrees.curl[current], KTreeCfgTrees.leafrad[current],
                            KTreeCfgTrees.subbranchdensity[current], KTreeCfgTrees.subbranchinglength[current],
                            KTreeCfgTrees.subbranchingsize[current], KTreeCfgTrees.subbranchangle[current],
                            KTreeCfgTrees.subbranchsize[current]
                    );
                    if (tallTree.func_76484_a(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 2) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.blockOakGenerate(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 3) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.greatGenerate(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 4) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.swampGenerate(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 5) {
                    KWorldGenCyprusTree bigTree = new KWorldGenCyprusTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.func_76484_a(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 6) {
                    KWorldGenHatTree bigTree = new KWorldGenHatTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.func_76484_a(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 7) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.pineGenerate(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 8) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.birchGenerate(world, rand, x, y, z)) generatedAny = true;
                } else if (alg == 9) {
                    KWorldGenBigTree bigTree = new KWorldGenBigTree(false);
                    bigTree.setConfigOptions(
                            KTreeCfgTrees.woodBlock[current], KTreeCfgTrees.leafBlock[current],
                            KTreeCfgTrees.woodMeta[current], KTreeCfgTrees.leafMeta[current],
                            KTreeCfgTrees.baseBlock1[current], KTreeCfgTrees.baseBlock2[current],
                            KTreeCfgTrees.heightmin[current], KTreeCfgTrees.heightmax[current],
                            KTreeCfgTrees.stuntmin[current]
                    );
                    if (bigTree.desertGenerate(world, rand, x, y, z)) generatedAny = true;
                }
            }
        }
        return generatedAny;
    }
}
