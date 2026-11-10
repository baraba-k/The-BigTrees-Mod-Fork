package karob.bigtrees;

import java.io.File;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(BigTreesMod.MOD_ID)
public class BigTreesMod {
    public static final String MOD_ID = "bigtrees";
    public static final String MOD_NAME = "The BigTrees Mod";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MOD_ID);

    // Sapling blocks
    public static final DeferredBlock<BlockBTSapling> BIG_OAK_SAPLING = BLOCKS.register("bt_big_oak_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.BIG_OAK));
    public static final DeferredBlock<BlockBTSapling> BIG_BIRCH_SAPLING = BLOCKS.register("bt_big_birch_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.BIG_BIRCH));
    public static final DeferredBlock<BlockBTSapling> BIG_PINE_SAPLING = BLOCKS.register("bt_big_pine_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.BIG_PINE));
    public static final DeferredBlock<BlockBTSapling> DEAD_TREE_SAPLING = BLOCKS.register("bt_dead_tree_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.DEAD_TREE));
    public static final DeferredBlock<BlockBTSapling> CYPRUS_SAPLING = BLOCKS.register("bt_cyprus_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.CYPRUS));
    public static final DeferredBlock<BlockBTSapling> HAT_TREE_SAPLING = BLOCKS.register("bt_hat_tree_sapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.HAT_TREE));

    // Compatibility alias for the old 1.7.10 id "bt_bigsapling"
    public static final DeferredBlock<BlockBTSapling> BT_BIG_SAPLING = BLOCKS.register("bt_bigsapling", () -> new BlockBTSapling(BlockBTSapling.TreeType.BIG_OAK));

    // Items
    public static final DeferredItem<BlockItem> BIG_OAK_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_big_oak_sapling", BIG_OAK_SAPLING);
    public static final DeferredItem<BlockItem> BIG_BIRCH_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_big_birch_sapling", BIG_BIRCH_SAPLING);
    public static final DeferredItem<BlockItem> BIG_PINE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_big_pine_sapling", BIG_PINE_SAPLING);
    public static final DeferredItem<BlockItem> DEAD_TREE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_dead_tree_sapling", DEAD_TREE_SAPLING);
    public static final DeferredItem<BlockItem> CYPRUS_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_cyprus_sapling", CYPRUS_SAPLING);
    public static final DeferredItem<BlockItem> HAT_TREE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_hat_tree_sapling", HAT_TREE_SAPLING);
    public static final DeferredItem<BlockItem> BT_BIG_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("bt_bigsapling", BT_BIG_SAPLING);

    // Feature for WorldGen
    public static final DeferredHolder<Feature<?>, BigTreesFeature> BIG_TREES_FEATURE = FEATURES.register("big_trees", () -> new BigTreesFeature(NoneFeatureConfiguration.CODEC));

    // Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BIG_TREES_TAB = CREATIVE_MODE_TABS.register("bigtrees_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bigtrees"))
            .icon(() -> new ItemStack(BIG_OAK_SAPLING_ITEM.get()))
            .displayItems((params, output) -> {
                output.accept(BIG_OAK_SAPLING_ITEM.get());
                output.accept(BIG_BIRCH_SAPLING_ITEM.get());
                output.accept(BIG_PINE_SAPLING_ITEM.get());
                output.accept(DEAD_TREE_SAPLING_ITEM.get());
                output.accept(CYPRUS_SAPLING_ITEM.get());
                output.accept(HAT_TREE_SAPLING_ITEM.get());
            })
            .build());

    public BigTreesMod(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FEATURES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Initialize global references
        BTGlobal.bigOakSapling = BIG_OAK_SAPLING.get();
        BTGlobal.bigBirchSapling = BIG_BIRCH_SAPLING.get();
        BTGlobal.bigPineSapling = BIG_PINE_SAPLING.get();
        BTGlobal.deadTreeSapling = DEAD_TREE_SAPLING.get();
        BTGlobal.cyprusSapling = CYPRUS_SAPLING.get();
        BTGlobal.hatTreeSapling = HAT_TREE_SAPLING.get();

        event.enqueueWork(() -> {
            File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "bigtrees");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            KTreeCfg.init(configDir.getAbsolutePath());

            File treeConfigsDir = new File(configDir, "treeconfigs");
            if (!treeConfigsDir.exists()) {
                treeConfigsDir.mkdirs();
            }
            KTreeCfgTrees.init(treeConfigsDir);
            KTreeCfgBiomes.init(new File(configDir, "biomegroups.cfg"));
            KTreeCfgBiomes2.init(new File(configDir, "individualbiomes.cfg"));
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().getPath().equals("natural_blocks")) {
            event.accept(BIG_OAK_SAPLING_ITEM.get());
            event.accept(BIG_BIRCH_SAPLING_ITEM.get());
            event.accept(BIG_PINE_SAPLING_ITEM.get());
            event.accept(DEAD_TREE_SAPLING_ITEM.get());
            event.accept(CYPRUS_SAPLING_ITEM.get());
            event.accept(HAT_TREE_SAPLING_ITEM.get());
        }
    }
}
