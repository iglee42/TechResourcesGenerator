package fr.iglee42.techresourcesgenerator.customize.generation;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.customize.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesgenerator.TechResourcesGenerator.MODID;

public class RecipesGenerator {
    public static void generate() {
        //Types.GESSENCES.stream().filter(g->!g.isInModBase()).forEach(g -> cardInfuser(g.name().toLowerCase()));
    }
    private static void cardInfuser(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.GESSENCE_CARDS_RECIPES_FROM_GESSENCE_PATH.toFile(), name+"_card.json"));
            writer.write("{\n" +
                    "  \"type\": \"techresourcesgenerator:card_infuser\",\n" +
                    "  \"base\": \"techresourcesgenerator:gessence_card\",\n" +
                    "  \"infuser\": \"techresourcesgenerator:"+name+"_gessence\",\n" +
                    "  \"result\": \"techresourcesgenerator:"+name+"_gessence_card\"\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            TechResourcesGenerator.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }
}
