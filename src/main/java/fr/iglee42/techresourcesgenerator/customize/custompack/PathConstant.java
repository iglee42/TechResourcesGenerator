package fr.iglee42.techresourcesgenerator.customize.custompack;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Path;

public class PathConstant {

    public static Path ROOT_PATH;
    public static Path ASSETS_PATH;
    public static Path DATAS_PATH;

    public static Path LANGS_PATH;
    public static Path TEXTURES_PATH;
    public static Path RECIPES_PATH;

    public static Path BLOCK_STATES_PATH;
    public static Path MODELS_PATH;
    public static Path ITEM_MODELS_PATH;
    public static Path BLOCK_MODELS_PATH;
    public static Path GESSENCE_CARDS_RECIPES_PATH;
    public static Path GESSENCE_CARDS_RECIPES_FROM_GESSENCE_PATH;

    public static void init() {
        try {
            TechResourcesGenerator.deleteDirectory(FMLPaths.CONFIGDIR.get().resolve("techresourcesgenerator/pack"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ROOT_PATH = FMLPaths.CONFIGDIR.get().resolve("techresourcesgenerator/pack");
        ASSETS_PATH = ROOT_PATH.resolve("assets/techresourcesgenerator");
        DATAS_PATH = ROOT_PATH.resolve("data/techresourcesgenerator");

        BLOCK_STATES_PATH = ASSETS_PATH.resolve("blockstates");
        LANGS_PATH = ASSETS_PATH.resolve("lang");
        TEXTURES_PATH = ASSETS_PATH.resolve("textures");
        MODELS_PATH = ASSETS_PATH.resolve("models");

        RECIPES_PATH = DATAS_PATH.resolve("recipes");

        ITEM_MODELS_PATH = MODELS_PATH.resolve("item");
        BLOCK_MODELS_PATH = MODELS_PATH.resolve("block");
        GESSENCE_CARDS_RECIPES_PATH = RECIPES_PATH.resolve("gessence_cards");
        GESSENCE_CARDS_RECIPES_FROM_GESSENCE_PATH = GESSENCE_CARDS_RECIPES_PATH.resolve("from_gessence");

        GESSENCE_CARDS_RECIPES_FROM_GESSENCE_PATH.toFile().mkdirs();
        BLOCK_STATES_PATH.toFile().mkdirs();
        LANGS_PATH.toFile().mkdirs();
        TEXTURES_PATH.toFile().mkdirs();
        ITEM_MODELS_PATH.toFile().mkdirs();
        BLOCK_MODELS_PATH.toFile().mkdirs();
    }

}