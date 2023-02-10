package fr.iglee42.techresourcesgenerator.customize;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlocks;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Types {
    public static List<Gessence> GESSENCES = new ArrayList<>();

    public static List<Generator> GENERATORS = new ArrayList<>();
    private static List<Generator> loadingGenerators = new ArrayList<>();

    public static Map<String,Integer> GENERATORS_ORDERS = new HashMap<>();

    public static void init(){
        readGessenceFiles();
        registerAllGessence();
        readGeneratorFiles();
        registerAllGenerator();
    }


    private static void registerAllGessence() {
        if (GESSENCES.isEmpty()) return;
        for (Gessence g : GESSENCES) {
            String type = g.name();
            ModItem.createGessence(g);
            if (!g.isInModBase())TechResourcesGenerator.LOGGER.info("Registering gessence : " + type);
        }

    }

    private static void readGessenceFiles() {
        for (GessenceType t : GessenceType.values()){
            GESSENCES.add(new Gessence(t.getRessourceName(),t.getItemLocation().toString(),!t.isOnlyElectronic(),true,t.getMinimumGenerator().name().toLowerCase(),"",true));
        }

        File dir = FMLPaths.CONFIGDIR.get().resolve("techresourcesgenerator/gessence/").toFile();
        dir.mkdirs();
        if (!dir.mkdirs() && dir.isDirectory()) {
            File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
            if (files == null)
                return;

            for (File file : files) {
                JsonObject json;
                InputStreamReader reader = null;

                try {

                    JsonParser parser = new JsonParser();
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    json = parser.parse(reader).getAsJsonObject();
                    if (json.get("name").getAsString().isEmpty()) throw new NullPointerException("The name can't be empty ! (" + file.getName() + ")" );

                    GESSENCES.add(new Gessence(json.get("name").getAsString(),
                             JsonHelper.getStringOrDefault(json,"item","minecraft:bedrock"),
                            JsonHelper.getBooleanOrDefault(json,"hasNormalGessence",true),
                            JsonHelper.getBooleanOrDefault(json,"hasElectronicGessence",true),
                            JsonHelper.getStringOrDefault(json,"minimumGeneratorTier","basic"),
                            JsonHelper.getStringOrDefault(json,"itemTexture","minecraft:block/bedrock"),
                            false));
                    reader.close();
                } catch (Exception e) {
                    TechResourcesGenerator.LOGGER.error("An error occurred while loading Gessences", e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }

    }

    private static void readGeneratorFiles() {
        for (GeneratorType value : GeneratorType.values()) {
            loadingGenerators.add(new Generator(true,value.name().toLowerCase(),
                    "",true,
                    value.getType(),
                    0,0,0));
        }
        File dir = FMLPaths.CONFIGDIR.get().resolve("techresourcesgenerator/generator/").toFile();
        dir.mkdirs();
        if (!dir.mkdirs() && dir.isDirectory()) {
            File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
            if (files == null)
                return;
            for (File file : files) {
                JsonObject json;
                InputStreamReader reader = null;
                try {
                    JsonParser parser = new JsonParser();
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    json = parser.parse(reader).getAsJsonObject();
                    if (json.get("name").getAsString().isEmpty()) throw new NullPointerException("The name can't be empty ! (" + file.getName() + ")" );
                    boolean isElectric = JsonHelper.getStringOrDefault(json,"generatorType","basic").equals("electric");
                    loadingGenerators.add(new Generator(false,json.get("name").getAsString(),
                            JsonHelper.getStringOrDefault(json,"centerTexture","minecraft:block/bedrock"),
                            json.has("centerTexture"),
                            JsonHelper.getStringOrDefault(json,"generatorType","basic"),
                            JsonHelper.getIntOrDefault(json,"delay",60),
                            JsonHelper.getIntOrDefault(json,"itemCount",1),
                            isElectric ? JsonHelper.getIntOrDefault(json,"consumed", 4096) : 0));
                    reader.close();
                } catch (Exception e) {
                    TechResourcesGenerator.LOGGER.error("An error occurred while loading generators", e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }
        File orderingFile = new File(dir,"orders.txt");
        if (!orderingFile.exists()) {
            StringBuilder builder = new StringBuilder();
            for (GeneratorType value : GeneratorType.values())
                builder.append((value != GeneratorType.BASIC ? "\n" : "") + value.name().toLowerCase());
            loadingGenerators.forEach(g -> builder.append("\n" + g.name().toLowerCase()));
            try {
                FileWriter writer = new FileWriter(orderingFile);
                writer.write(builder.toString());
                writer.close();
            } catch (IOException exception) {
                TechResourcesGenerator.LOGGER.error("An error occurred while loading generators", exception);
            }
        }
            try{
                Scanner scanner = new Scanner(new FileReader(orderingFile));
                int index = -1;
                while (scanner.hasNextLine()){
                    String name = scanner.nextLine();
                    index++;
                    GENERATORS_ORDERS.put(name,index);
                    TechResourcesGenerator.LOGGER.info("Generator : " + name + ", order : "+ index);
                }
            } catch (IOException exception){
                TechResourcesGenerator.LOGGER.error("An error occurred while loading generators",exception);
            }
    }

    private static void registerAllGenerator() {
        loadingGenerators.forEach(g->{
            switch (g.generatorType()) {
                case "basic":
                    ModBlocks.createManualGenerator(g.name(),g);
                    break;
                case "electric" :
                    ModBlocks.createElectricGenerator(g.name(), g);
                    break;
                case "magmatic" :
                    ModBlocks.createMagmaticGenerator(g.name(), g);
                    break;
            }
            GENERATORS.add(g);
            TechResourcesGenerator.LOGGER.info("Registering generator : " + g.name());
        });
        loadingGenerators.clear();
    }

    public static boolean isValidGessenceType(String type){
        if (GESSENCES.stream().noneMatch(t-> t.name().equals(type))) throw new IllegalArgumentException("Unknow Gessence type : " + type);
        return true;
    }

    public static Gessence getGessenceType(String type) {
        if (!isValidGessenceType(type)) return null;
        return GESSENCES.stream().filter(g->g.name().equals(type)).findFirst().get();
    }
}
