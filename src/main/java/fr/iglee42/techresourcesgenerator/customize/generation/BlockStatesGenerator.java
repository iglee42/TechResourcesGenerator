package fr.iglee42.techresourcesgenerator.customize.generation;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.customize.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesgenerator.TechResourcesGenerator.MODID;

public class BlockStatesGenerator {
    public static void generate() {
        Types.GENERATORS.stream().filter(g->!g.isInModBase()).forEach(generator -> blockState(generator.name() + "_generator"));
    }
    private static void blockState(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_STATES_PATH.toFile(), name+".json"));
            writer.write("{\n" +
                    "  \"variants\": {\n" +
                    "    \"\": {\n" +
                    "      \"model\": \""+MODID+":block/"+name+"\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            TechResourcesGenerator.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
