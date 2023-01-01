package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechResourcesGenerator.MODID);


    public static final RegistryObject<Item> GESSENCE_CARD = ITEMS.register("gessence_card", () -> new Item(new Item.Properties().tab(TechResourcesGenerator.GENERATOR_GROUP)));

    public static final RegistryObject<Item> BASE_GESSENCE = ITEMS.register("base_gessence", () -> new Item(new Item.Properties().tab(TechResourcesGenerator.GENERATOR_GROUP)));
    /*public static final RegistryObject<Item> WOOD_GESSENCE = ITEMS.register("wood_gessence", () -> new Gessence(GessenceType.WOOD));
    public static final RegistryObject<Item> COBBLESTONE_GESSENCE = ITEMS.register("cobblestone_gessence", () -> new Gessence(GessenceType.COBBLESTONE));
    public static final RegistryObject<Item> COAL_GESSENCE = ITEMS.register("coal_gessence", () -> new Gessence(GessenceType.COAL));
    public static final RegistryObject<Item> IRON_GESSENCE = ITEMS.register("iron_gessence", () -> new Gessence(GessenceType.IRON));
    public static final RegistryObject<Item> GOLD_GESSENCE = ITEMS.register("gold_gessence", () -> new Gessence(GessenceType.GOLD));
    public static final RegistryObject<Item> REDSTONE_GESSENCE = ITEMS.register("redstone_gessence", () -> new Gessence(GessenceType.REDSTONE));
    public static final RegistryObject<Item> LAPIS_GESSENCE = ITEMS.register("lapis_gessence", () -> new Gessence(GessenceType.LAPIS));
    public static final RegistryObject<Item> DIAMOND_GESSENCE = ITEMS.register("diamond_gessence", () -> new Gessence(GessenceType.DIAMOND));
    public static final RegistryObject<Item> EMERALD_GESSENCE = ITEMS.register("emerald_gessence", () -> new Gessence(GessenceType.EMERALD));
    public static final RegistryObject<Item> QUARTZ_GESSENCE = ITEMS.register("quartz_gessence", () -> new Gessence(GessenceType.QUARTZ));
    public static final RegistryObject<Item> NETHERITE_GESSENCE = ITEMS.register("netherite_gessence", () -> new Gessence(GessenceType.NETHERITE));
    public static final RegistryObject<Item> MODIUM_GESSENCE = ITEMS.register("modium_gessence", () -> new Gessence(GessenceType.MODIUM));
    public static final RegistryObject<Item> DERIUM_GESSENCE = ITEMS.register("derium_gessence", () -> new Gessence(GessenceType.DERIUM));
    public static final RegistryObject<Item> BLAZUM_GESSENCE = ITEMS.register("blazum_gessence", () -> new Gessence(GessenceType.BLAZUM));
    public static final RegistryObject<Item> LAVIUM_GESSENCE = ITEMS.register("lavium_gessence", () -> new Gessence(GessenceType.LAVIUM)); */

    public static void createGessence(GessenceType type){
        if (!type.isOnlyElectronic()) ITEMS.register(type.getRessourceName() + "_gessence",()-> new ItemGessence(type,TechResourcesGenerator.GENERATOR_GROUP));
        ITEMS.register(type.getRessourceName() + "_gessence_card",()-> new ItemGessence(type,TechResourcesGenerator.CARDS_GROUP));
    }
    
    public static Item getGessence(GessenceType type){
        if (type.isOnlyElectronic()) return getGessenceCard(type);
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(TechResourcesGenerator.MODID , type.getRessourceName() + "_gessence"));
    }

    public static Item getGessenceCard(GessenceType type) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(TechResourcesGenerator.MODID , type.getRessourceName() + "_gessence_card"));
    }

}
