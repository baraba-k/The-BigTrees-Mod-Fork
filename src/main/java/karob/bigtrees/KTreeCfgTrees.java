package karob.bigtrees;

import java.io.File;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class KTreeCfgTrees {
    public static BTConfigFile config;
    public static int count = 0;
    public static String[] treeName;
    public static int[] heightmin;
    public static int[] heightmax;
    public static int[] frequencymultiplyer;
    public static String[] woodName;
    public static Block[] woodBlock;
    public static int[] woodMeta;
    public static String[] leafName;
    public static Block[] leafBlock;
    public static int[] leafMeta;
    public static int[] stuntmin;
    public static String[] basetype1;
    public static String[] basetype2;
    public static Block[] baseBlock1;
    public static Block[] baseBlock2;
    public static int[] locality;
    public static int[] localitymin;
    public static int[] localitymax;
    public static int[] algore;
    public static int tallTrees = 0;
    public static int hatTrees = 0;
    public static int greatoakTrees = 0;
    public static int swampoakTrees = 0;
    public static int blockoakTrees = 0;
    public static int greatpineTrees = 0;
    public static int greatbirchTrees = 0;
    public static int deadTrees = 0;
    public static int cyprusTrees = 0;
    public static double[] branchlessmin;
    public static double[] branchlessmax;
    public static double[] longestbranchp;
    public static double[] branchrot;
    public static double[] taplength;
    public static double[] branchspace;
    public static double[] pitch;
    public static double[] curl;
    public static int[] leafrad;
    public static double[] subbranchdensity;
    public static double[] subbranchinglength;
    public static int[] subbranchingsize;
    public static double[] subbranchangle;
    public static double[] subbranchsize;

    private static void allocArrays(int size) {
        treeName = new String[size];
        heightmin = new int[size];
        heightmax = new int[size];
        frequencymultiplyer = new int[size];
        woodName = new String[size];
        woodBlock = new Block[size];
        woodMeta = new int[size];
        leafName = new String[size];
        leafBlock = new Block[size];
        leafMeta = new int[size];
        stuntmin = new int[size];
        basetype1 = new String[size];
        basetype2 = new String[size];
        baseBlock1 = new Block[size];
        baseBlock2 = new Block[size];
        locality = new int[size + 1];
        localitymin = new int[size + 1];
        localitymax = new int[size + 1];
        algore = new int[size];
        branchlessmin = new double[size];
        branchlessmax = new double[size];
        longestbranchp = new double[size];
        branchrot = new double[size];
        taplength = new double[size];
        branchspace = new double[size];
        pitch = new double[size];
        curl = new double[size];
        leafrad = new int[size];
        subbranchdensity = new double[size];
        subbranchinglength = new double[size];
        subbranchingsize = new int[size];
        subbranchangle = new double[size];
        subbranchsize = new double[size];
    }

    public static void init(File configDir) {
        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".cfg"));
        if (files == null) files = new File[0];

        int size = Math.max(9, files.length);
        allocArrays(size);

        if (files.length == 0) {
            configureDefault(configDir, "TallOak.cfg", "TallOak");
            configureDefault(configDir, "BlockOak.cfg", "BlockOak");
            configureDefault(configDir, "GreatOak.cfg", "GreatOak");
            configureDefault(configDir, "SwampOak.cfg", "SwampOak");
            configureDefault(configDir, "Cyprus.cfg", "Cyprus");
            configureDefault(configDir, "Hat.cfg", "Hat");
            configureDefault(configDir, "BigPine.cfg", "BigPine");
            configureDefault(configDir, "BigBirch.cfg", "BigBirch");
            configureDefault(configDir, "Dead.cfg", "Dead");
            count = 9;
        } else {
            for (File file : files) {
                try {
                    config = new BTConfigFile(file);
                    config.load();
                    String alg = config.get("Tree Configuration Settings", "Algorithm", "").getString();
                    dispatchAlgorithm(alg, config);
                } catch (Exception e) {
                    System.out.println("Problem reading Bigtrees tree config file: " + file.getName());
                }
            }
        }
    }

    private static void configureDefault(File configDir, String fileName, String alg) {
        File file = new File(configDir, fileName);
        config = new BTConfigFile(file);
        config.load();
        config.get("Tree Configuration Settings", "Algorithm", alg, "TallOak, BlockOak, GreatOak, SwampOak, Cyprus, Hat, BigPine, BigBirch, or Dead");
        dispatchAlgorithm(alg, config);
    }

    private static void dispatchAlgorithm(String alg, BTConfigFile cfg) {
        switch (alg) {
            case "TallOak" -> configureTallOak(cfg);
            case "BlockOak" -> configureBlockOak(cfg);
            case "GreatOak" -> configureGreatOak(cfg);
            case "SwampOak" -> configureSwampOak(cfg);
            case "Cyprus" -> configureCyprus(cfg);
            case "Hat" -> configureHat(cfg);
            case "BigPine" -> configureBigPine(cfg);
            case "BigBirch" -> configureBigBirch(cfg);
            case "Dead" -> configureDead(cfg);
        }
    }

    public static void configureTallOak(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Tall Oak " + tallTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 13, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 28, "Maximum height in blocks").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt minimum").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Block tree can grow on").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Other block tree can grow on").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 1, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 0, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 40, "Locality max").getInt();
        branchlessmin[idx] = config.get("Tree Configuration Settings", "Branchless Min", 0.23, "Branchless min").getDouble();
        branchlessmax[idx] = config.get("Tree Configuration Settings", "Branchless Max", 0.32, "Branchless max").getDouble();
        longestbranchp[idx] = config.get("Tree Configuration Settings", "Longest Branch Percentage", 0.6, "Longest branch").getDouble();
        branchrot[idx] = config.get("Tree Configuration Settings", "Branch Rotation", 0.618034, "Branch rotation").getDouble();
        taplength[idx] = config.get("Tree Configuration Settings", "Tap Root Length", 0.5, "Tap root length").getDouble();
        branchspace[idx] = config.get("Tree Configuration Settings", "Branch Spacing", 0.6, "Branch spacing").getDouble();
        pitch[idx] = config.get("Tree Configuration Settings", "Branch Pitch", 0.0, "Branch pitch").getDouble();
        curl[idx] = config.get("Tree Configuration Settings", "Branch Curl", 0.08, "Branch curl").getDouble();
        leafrad[idx] = config.get("Tree Configuration Settings", "Leaf Radius", 3, "Leaf radius").getInt();
        subbranchdensity[idx] = config.get("Tree Configuration Settings", "SubBranch Density", 0.12, "Sub branch density").getDouble();
        subbranchinglength[idx] = config.get("Tree Configuration Settings", "Sub Branching Length", 0.6, "Sub branching length").getDouble();
        subbranchingsize[idx] = config.get("Tree Configuration Settings", "Sub Branching Size", 4, "Sub branching size").getInt();
        subbranchangle[idx] = config.get("Tree Configuration Settings", "Sub Branch Angle", 30.0, "Sub branch angle").getDouble();
        subbranchsize[idx] = config.get("Tree Configuration Settings", "Sub Branch Size", 0.75, "Sub branch size").getDouble();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.OAK_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 1;
        count++;
        tallTrees++;
        config.save();
    }

    public static void configureBlockOak(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Block Oak " + blockoakTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 12, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 18, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 1, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 30, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 50, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.OAK_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 2;
        count++;
        blockoakTrees++;
        config.save();
    }

    public static void configureGreatOak(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Great Oak " + greatoakTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 28, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 32, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 1, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 45, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 65, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.OAK_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 3;
        count++;
        greatoakTrees++;
        config.save();
    }

    public static void configureSwampOak(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Swamp Oak " + swampoakTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 28, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 32, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 1, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 55, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 75, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.OAK_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 4;
        count++;
        swampoakTrees++;
        config.save();
    }

    public static void configureCyprus(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Cyprus Oak " + cyprusTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 28, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 32, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 1, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 1, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 2, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 0, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 90, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.SPRUCE_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.SPRUCE_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 5;
        count++;
        cyprusTrees++;
        config.save();
    }

    public static void configureHat(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Hat Tree " + hatTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 28, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 32, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 3, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 0, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 90, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.OAK_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 6;
        count++;
        hatTrees++;
        config.save();
    }

    public static void configureBigPine(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Big Pine " + greatpineTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 18, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 22, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 40, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 1, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 1, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "gravel", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 4, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 80, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 60, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.SPRUCE_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.SPRUCE_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.GRAVEL);

        algore[idx] = 7;
        count++;
        greatpineTrees++;
        config.save();
    }

    public static void configureBigBirch(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Big Birch " + greatbirchTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 19, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 24, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 2, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "leaves", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 2, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "dirt", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 5, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 0, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 60, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.BIRCH_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.BIRCH_LEAVES);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.DIRT);

        algore[idx] = 8;
        count++;
        greatbirchTrees++;
        config.save();
    }

    public static void configureDead(BTConfigFile config) {
        int idx = count;
        treeName[idx] = config.get("Tree Configuration Settings", "Unique Tree Name", "Dead Tree " + deadTrees, "Unique tree name").getString();
        heightmin[idx] = config.get("Tree Configuration Settings", "Height Min", 13, "Minimum height in blocks").getInt();
        heightmax[idx] = config.get("Tree Configuration Settings", "Height Max", 28, "Maximum height in blocks").getInt();
        frequencymultiplyer[idx] = config.get("Tree Configuration Settings", "Frequency", 10, "Frequency").getInt();
        woodName[idx] = config.get("Tree Configuration Settings", "Wood Name", "log", "Wood name").getString();
        woodMeta[idx] = config.get("Tree Configuration Settings", "Wood Meta", 0, "Wood meta").getInt();
        leafName[idx] = config.get("Tree Configuration Settings", "Leaf Name", "air", "Leaf name").getString();
        leafMeta[idx] = config.get("Tree Configuration Settings", "Leaf Meta", 0, "Leaf meta").getInt();
        stuntmin[idx] = config.get("Tree Configuration Settings", "Stunt Minimum", 7, "Stunt min").getInt();
        basetype1[idx] = config.get("Tree Configuration Settings", "Base Type 1", "grass", "Base 1").getString();
        basetype2[idx] = config.get("Tree Configuration Settings", "Base Type 2", "sand", "Base 2").getString();
        locality[idx] = config.get("Tree Configuration Settings", "Locality Distribution Type", 1, "Locality").getInt();
        localitymin[idx] = config.get("Tree Configuration Settings", "Locality Min", 0, "Locality min").getInt();
        localitymax[idx] = config.get("Tree Configuration Settings", "Locality Max", 60, "Locality max").getInt();

        woodBlock[idx] = BTBlockHelper.resolveBlock(woodName[idx], Blocks.OAK_LOG);
        leafBlock[idx] = BTBlockHelper.resolveBlock(leafName[idx], Blocks.AIR);
        baseBlock1[idx] = BTBlockHelper.resolveBlock(basetype1[idx], Blocks.GRASS_BLOCK);
        baseBlock2[idx] = BTBlockHelper.resolveBlock(basetype2[idx], Blocks.SAND);

        algore[idx] = 9;
        count++;
        deadTrees++;
        config.save();
    }
}
