package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.items.ModItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Objects;

public enum GessenceType {
    WOOD(Items.OAK_LOG,"wood", ModItem.WOOD_GESSENCE,GeneratorType.BASIC),
    COBBLESTONE(Items.COBBLESTONE,"cobblestone", ModItem.COBBLESTONE_GESSENCE,GeneratorType.BASIC),
    COAL(Items.COAL,"coal", ModItem.COAL_GESSENCE,GeneratorType.BASIC),
    IRON(Items.IRON_INGOT,"iron", ModItem.IRON_GESSENCE,GeneratorType.BASIC),
    GOLD(Items.GOLD_INGOT,"gold", ModItem.GOLD_GESSENCE,GeneratorType.IRON),
    REDSTONE(Items.REDSTONE,"redstone", ModItem.REDSTONE_GESSENCE,GeneratorType.GOLD),
    LAPIS(Items.LAPIS_LAZULI,"lapis", ModItem.LAPIS_GESSENCE,GeneratorType.GOLD),
    QUARTZ(Items.QUARTZ,"quartz", ModItem.QUARTZ_GESSENCE,GeneratorType.IRON),
    DIAMOND(Items.DIAMOND,"diamond", ModItem.DIAMOND_GESSENCE,GeneratorType.GOLD),
    EMERALD(Items.EMERALD,"emerald", ModItem.EMERALD_GESSENCE,GeneratorType.DIAMOND),
    NETHERITE(Items.NETHERITE_SCRAP,"netherite", ModItem.NETHERITE_GESSENCE,GeneratorType.DIAMOND),
    MODIUM(fr.iglee42.techresourcesbase.init.ModItem.MODIUM_INGOT.get(),"modium",ModItem.MODIUM_GESSENCE,GeneratorType.NETHERITE),
    DERIUM(fr.iglee42.techresourcesbase.init.ModItem.DERIUM_INGOT.get(), "derium",ModItem.DERIUM_GESSENCE,GeneratorType.NETHERITE),
    BLAZUM(fr.iglee42.techresourcesbase.init.ModItem.BLAZUM_INGOT.get(), "blazum",ModItem.BLAZUM_GESSENCE,GeneratorType.NETHERITE),
    LAVIUM(fr.iglee42.techresourcesbase.init.ModItem.LAVIUM_INGOT.get(), "lavium",ModItem.LAVIUM_GESSENCE,GeneratorType.NETHERITE)
    ;

    private Item item;
    private RegistryObject<Item> gessence;
    private String resourceName;

    private GeneratorType minimumGenerator;
    GessenceType(Item item, String resourceName, RegistryObject<Item> gessence, GeneratorType minimumGenerator) {
        this.item = item;
        this.resourceName = resourceName;
        this.gessence = gessence;
        this.minimumGenerator = minimumGenerator;
    }

    public static GessenceType getByResourceName(String name){
        for (GessenceType type : values()){
            if (Objects.equals(type.getRessourceName(), name)) return type;
        }
        return null;
    }

    public static GessenceType getByItem(Item item) {
        return Arrays.stream(values()).filter(g -> g.getGessence().get() == item).findFirst().orElse(null);
    }

    public Item getItem() {return item;
    }

    public GeneratorType getMinimumGenerator() {
        return minimumGenerator;
    }

    public String getRessourceName() {
        return resourceName;
    }

    public RegistryObject<Item> getGessence() {
        return gessence;
    }
}
