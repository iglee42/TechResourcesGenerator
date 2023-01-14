package fr.iglee42.techresourcesgenerator.customize.generation;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.customize.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesgenerator.TechResourcesGenerator.MODID;

public class ModelsGenerator {
    public static void generate() {
        Types.GESSENCES.stream().filter(g->!g.isInModBase()).forEach(gessence -> {
            if (gessence.hasNormalGessence())itemFromParent(gessence.name() + "_gessence","minecraft:item/generated",new TextureKey("layer0",MODID + ":gessence/"+gessence.name()));
            if (gessence.hasElectronicGessence())itemFromParent(gessence.name() + "_gessence_card",MODID+":item/gessence_card",new TextureKey("1",gessence.itemTexture()));
        });

        Types.GENERATORS.stream().filter(g->!g.isInModBase()).forEach(generator -> {
            switch (generator.generatorType()){
                case "basic" :
                    blockFromParent(generator.name() + "_generator",MODID + ":block/basic_generator",  new TextureKey("1", generator.hasCustomTexture() ? generator.centerTexture() : "techresourcesgenerator:generator/"+generator.name()));
                    break;
                case "magmatic" :
                    blockFromParent(generator.name() + "_generator",MODID + ":block/magmatic_generator",  new TextureKey("1", generator.hasCustomTexture() ? generator.centerTexture() : "techresourcesgenerator:generator/"+generator.name()));
                    break;
                case "electric" :
                    blockFromParent(generator.name() + "_generator",MODID + ":block/electric_generator",  new TextureKey("1", generator.hasCustomTexture() ? generator.centerTexture() : "techresourcesgenerator:generator/"+generator.name()));
                    break;
                default:break;
            }
            itemFromBlock(generator.name() + "_generator");
        });
    }

    private static void itemFromBlock(String name){
        itemFromParent(name,MODID + ":block/" + name);
    }
    private static void itemFromParent(String name,String parent,TextureKey... textureKeys){
        String jsonBase =   "{\n"+
                            "   \"parent\": \""+ parent +"\""+(textureKeys.length > 0 ? ",":"")+"\n";
        StringBuilder builder = new StringBuilder(jsonBase);
        if (textureKeys.length > 0){
            builder.append("   \"textures\": {\n");
            for (int i = 0; i < textureKeys.length; i++){
                builder.append(textureKeys[i].toJson());
                if (i != textureKeys.length - 1) builder.append(",");
                builder.append("\n");
            }
            builder.append("    }\n");
        }
        builder.append("}");
        generateItem(name,builder.toString());
    }
    private static void generateItem(String name, String fileText) {
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.ITEM_MODELS_PATH.toFile(), name+".json"));
            writer.write(fileText);
            writer.close();
        } catch (Exception exception){
            TechResourcesGenerator.LOGGER.error("An error was detected when models generating",exception);
        }

    }
    private static void blockFromParent(String name,String parent,TextureKey... textureKeys){
        String jsonBase =   "{\n"+
                            "   \"parent\": \""+ parent +"\",\n";
        StringBuilder builder = new StringBuilder(jsonBase);
        if (textureKeys.length > 0){
            builder.append("   \"textures\": {\n");
            builder.append("        \"particle\": \"").append(textureKeys[0].getObject()).append("\",\n");
            for (int i = 0; i < textureKeys.length; i++){
                builder.append(textureKeys[i].toJson());
                if (i != textureKeys.length - 1) builder.append(",");
                builder.append("\n");
            }
            builder.append("    }\n");
        }
        builder.append("}");
        generateBlock(name,builder.toString());
    }
    private static void generateBlock(String name, String fileText) {
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_MODELS_PATH.toFile(), name+".json"));
            writer.write(fileText);
            writer.close();
        } catch (Exception exception){
            TechResourcesGenerator.LOGGER.error("An error was detected when models generating",exception);
        }

    }
}
