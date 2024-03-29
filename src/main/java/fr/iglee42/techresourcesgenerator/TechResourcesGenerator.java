package fr.iglee42.techresourcesgenerator;

import com.google.common.collect.Lists;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.client.renderer.blockentities.CardInfuserRenderer;
import fr.iglee42.techresourcesgenerator.client.renderer.blockentities.ElectricGeneratorRenderer;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.ElectricGeneratorScreen;
import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import fr.iglee42.techresourcesgenerator.customize.*;
import fr.iglee42.techresourcesgenerator.customize.generation.*;
import fr.iglee42.techresourcesgenerator.customize.custompack.*;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.MagmaticGeneratorScreen;
import fr.iglee42.techresourcesgenerator.menu.ModMenuTypes;
import fr.iglee42.techresourcesgenerator.recipes.ModRecipes;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.utils.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TechResourcesGenerator.MODID)
public class TechResourcesGenerator {

    public static final String MODID = "techresourcesgenerator";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab GENERATOR_GROUP = new CreativeModeTab(TechResourcesGenerator.MODID + ".generator") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItem.BASE_GESSENCE.get());
        }
        @Override
        public void fillItemList(NonNullList<ItemStack> stacks) {
            Types.GENERATORS.stream().filter(g->!g.generatorType().equals("electric")).forEach(g->stacks.add(new ItemStack(ModBlock.getGenerator(g))));
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
            return new ItemStack(ModItem.GESSENCE_CARD.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> stacks) {
            Types.GENERATORS.stream().filter(g->g.generatorType().equals("electric")).forEach(g->stacks.add(new ItemStack(ModBlock.getGenerator(g))));
            for (RegistryObject<Item> it : ModItem.ITEMS.getEntries()) {
                if (it.getId().getPath().endsWith("generator")) continue;
                if (it.getId().getPath().endsWith("gessence")) continue;
                stacks.add(new ItemStack(it.get()));
            }
        }
    };

    private static boolean hasGenerated;

    private static TechResourcesGenerator instance;


    public TechResourcesGenerator() {
        hasGenerated = false;
        instance = this;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC,"techresourcesgenerator-common.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlock.BLOCKS.register(bus);
        ModItem.ITEMS.register(bus);
        ModBlockEntities.TILE_ENTITIES.register(bus);
        ModMenuTypes.MENUS.register(bus);
        ModRecipes.SERIALIZER.register(bus);
        ModSounds.SOUND_EVENTS.register(bus);
        PathConstant.init();
        Types.init();

        transferTextures();

        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
        try {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new TRGPackFinder(PackType.RESOURCE));
            }
        } catch (Exception ignored) {
        }    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }


    public static void transferTextures() {
        Path texturesPath = FMLPaths.GAMEDIR.get().resolve("techresourcesTextures");
        File texturesDir = texturesPath.toFile();
        texturesDir.mkdirs();
        Path gessenceTexturesPath = FMLPaths.GAMEDIR.get().resolve("techresourcesTextures/gessences");
        File gessenceTexturesDir = gessenceTexturesPath.toFile();
        gessenceTexturesDir.mkdirs();
        Path generatorTexturesPath = FMLPaths.GAMEDIR.get().resolve("techresourcesTextures/generator");
        File generatorTexturesDir = generatorTexturesPath.toFile();
        generatorTexturesDir.mkdirs();
        File outGessenceDir = PathConstant.TEXTURES_PATH.resolve("gessence").toFile();
        File outGeneratorDir = PathConstant.TEXTURES_PATH.resolve("generator").toFile();
        for (Gessence c : Types.GESSENCES) {
            if (c.isInModBase()) continue;
            String name = c.name();
            LOGGER.info("Copy Texture for gessence : " + name);
            try {
                copyFile(new File(gessenceTexturesDir,name+".png"),new File(outGessenceDir,name+".png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Types.GENERATORS.stream().filter(g->!g.hasCustomTexture()).forEach(g->{
            String name = g.name();
            LOGGER.info("Copy Texture for generator : " + name);
            try {
                copyFile(new File(generatorTexturesDir,name+".png"),new File(outGeneratorDir,name+".png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    public static void deleteDirectory(Path sourceDirectory) throws IOException {
        if (!sourceDirectory.toFile().exists()) return;

        for (String f : sourceDirectory.toFile().list()) {
            deleteDirectoryCompatibilityMode(new File(sourceDirectory.toFile(), f).toPath());
        }
        sourceDirectory.toFile().delete();
    }

    public static void deleteDirectoryCompatibilityMode(Path source) throws IOException {
        if (source.toFile().isDirectory()) {
            deleteDirectory(source);
        } else {
            source.toFile().delete();
        }
    }
    private static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : sourceDirectory.list()) {
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }

    private static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.getParentFile().exists())destinationFile.getParentFile().mkdirs();
        if (!sourceFile.exists()) return;
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public void onServerStarted(final ServerStartedEvent event) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PackRepository repo = event.getServer().getPackRepository();
                List<Pack> packs = Lists.newArrayList(repo.getSelectedPacks());
                event.getServer().reloadResources(packs.stream().map(Pack::getId).collect(Collectors.toList()));
                this.cancel();
            }
        }, 5000L);
    }

    public void onServerStart(final ServerAboutToStartEvent event) {
        event.getServer().getPackRepository().addPackFinder(new TRGPackFinder(PackType.DATA));

    }


    public static void generateData() {
        if (!hasGenerated) {
            if (!ModLoader.isLoadingStateValid()) {
                return;
            }
            ModelsGenerator.generate();
            BlockStatesGenerator.generate();
            LangsGenerator.generate();
            RecipesGenerator.generate();
            hasGenerated = true;
        }
    }

    public static void injectDatapackFinder(PackRepository resourcePacks) {
        if (DistExecutor.unsafeRunForDist(() -> () -> resourcePacks != Minecraft.getInstance().getResourcePackRepository(), () -> () -> true)) {
            resourcePacks.addPackFinder(new TRGPackFinder(PackType.RESOURCE));


        }
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
