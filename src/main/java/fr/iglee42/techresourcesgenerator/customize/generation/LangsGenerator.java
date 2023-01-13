package fr.iglee42.techresourcesgenerator.customize.generation;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.customize.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.iglee42.techresourcesbase.utils.ModsUtils.getUpperName;

public class LangsGenerator {

    private static Map<String,String> langs = new HashMap<>();
    public static void generate() {
        Types.GESSENCES.stream().filter(g->!g.isInModBase()).forEach(g->{
            if (g.hasNormalGessence()) langs.put("item.techresourcesgenerator."+g.name() + "_gessence",getUpperName(g.name()," ") +" Gessence");
            if (g.hasElectronicGessence()) langs.put("item.techresourcesgenerator."+g.name() + "_gessence_card",getUpperName(g.name()," ") +" Gessence Card");
        });
        Types.GENERATORS.stream().filter(g->!g.isInModBase()).forEach(g->langs.put("block.techresourcesgenerator."+g.name()+"_generator",getUpperName(g.name()," ") +" Generator"));



        try {
            FileWriter writer = new FileWriter(new File(PathConstant.LANGS_PATH.toFile(), "en_us.json"));
            writer.write("{\n");
            AtomicInteger index = new AtomicInteger(-1);
            langs.forEach((key,translation) -> {
                try {
                    index.getAndIncrement();
                    writer.write("  \"" + key + "\": \"" + translation + "\"" + (index.get() != langs.size() - 1? ",":"") + "\n");

                } catch (IOException e) {
                    TechResourcesGenerator.LOGGER.error("An error was detected when langs generating",e);
                }
            });
            writer.write("}");
            writer.close();
        } catch (Exception exception){
            TechResourcesGenerator.LOGGER.error("An error was detected when langs generating",exception);
        }
    }
}
