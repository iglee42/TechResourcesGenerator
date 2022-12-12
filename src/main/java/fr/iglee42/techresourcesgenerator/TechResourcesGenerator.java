package fr.iglee42.techresourcesgenerator;

import fr.iglee42.techresourcesbase.config.TechResourcesBaseCommonConfig;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.client.screen.MagmaticGeneratorScreen;
import fr.iglee42.techresourcesgenerator.menu.ModMenuTypes;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TechResourcesGenerator.MODID)
public class TechResourcesGenerator {

    public static final String MODID = "techresourcesgenerator";
    private static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab GENERATOR_GROUP = new CreativeModeTab(TechResourcesGenerator.MODID + ".generator") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlock.BASIC_GENERATOR.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> stacks) {
            //stacks.clear();
            stacks.add(new ItemStack(ModBlock.BASIC_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.IRON_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.GOLD_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.DIAMOND_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.NETHERITE_GENERATOR.get()));
            for (RegistryObject<Item> it : ModItem.ITEMS.getEntries()){
                if (it.getId().getPath().endsWith("generator")) continue;
                stacks.add(new ItemStack(it.get()));
            }

            //super.fillItemList(stacks);
        }
    };

    public TechResourcesGenerator() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC,"techresourcesgenerator-common.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlock.BLOCKS.register(bus);
        ModItem.ITEMS.register(bus);
        ModBlockEntities.TILE_ENTITIES.register(bus);
        ModMenuTypes.MENUS.register(bus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.MAGMATIC_GENERATOR_MENU.get(), MagmaticGeneratorScreen::new);
        }
    }

}
