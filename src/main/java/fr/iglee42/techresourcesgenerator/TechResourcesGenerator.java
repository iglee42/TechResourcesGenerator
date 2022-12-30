package fr.iglee42.techresourcesgenerator;

import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.client.renderer.blockentities.CardInfuserRenderer;
import fr.iglee42.techresourcesgenerator.client.renderer.blockentities.ElectricGeneratorRenderer;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.ElectricGeneratorScreen;
import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.MagmaticGeneratorScreen;
import fr.iglee42.techresourcesgenerator.menu.ModMenuTypes;
import fr.iglee42.techresourcesgenerator.recipes.ModRecipes;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
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

import java.util.Arrays;

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
            stacks.add(new ItemStack(ModBlock.BASIC_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.IRON_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.GOLD_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.DIAMOND_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.NETHERITE_GENERATOR.get()));
            for (RegistryObject<Item> it : ModItem.ITEMS.getEntries()){
                if (it.getId().getPath().endsWith("generator")) continue;
                if (it.getId().getPath().endsWith("card")) continue;
                stacks.add(new ItemStack(it.get()));
            }
        }
    };
    public static final CreativeModeTab CARDS_GROUP = new CreativeModeTab(TechResourcesGenerator.MODID + ".cards") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlock.MODIUM_GENERATOR.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> stacks) {
            stacks.add(new ItemStack(ModBlock.MODIUM_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.DERIUM_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.BLAZUM_GENERATOR.get()));
            stacks.add(new ItemStack(ModBlock.LAVIUM_GENERATOR.get()));
            for (RegistryObject<Item> it : ModItem.ITEMS.getEntries()) {
                if (it.getId().getPath().endsWith("generator")) continue;
                if (it.getId().getPath().endsWith("gessence")) continue;
                stacks.add(new ItemStack(it.get()));
            }
        }
    };
    public TechResourcesGenerator() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC,"techresourcesgenerator-common.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlock.BLOCKS.register(bus);
        ModItem.ITEMS.register(bus);
        ModBlockEntities.TILE_ENTITIES.register(bus);
        ModMenuTypes.MENUS.register(bus);
        ModRecipes.SERIALIZER.register(bus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
        Arrays.stream(GessenceType.values()).toList().forEach(ModItem::createGessence);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.MAGMATIC_GENERATOR_MENU.get(), MagmaticGeneratorScreen::new);
            MenuScreens.register(ModMenuTypes.ELECTRIC_GENERATOR_MENU.get(), ElectricGeneratorScreen::new);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.ELECTRIC_GENERATOR.get(),
                    ElectricGeneratorRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.CARD_INFUSER.get(),
                    CardInfuserRenderer::new);
        }
    }

}
