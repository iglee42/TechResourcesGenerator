package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.items.Gessence;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Objects;

public enum GessenceType {
    WOOD(Items.OAK_LOG,"wood", false,GeneratorType.BASIC),
    COBBLESTONE(Items.COBBLESTONE,"cobblestone", false,GeneratorType.BASIC),
    COAL(Items.COAL,"coal", false,GeneratorType.BASIC),
    IRON(Items.IRON_INGOT,"iron", false,GeneratorType.BASIC),
    GOLD(Items.GOLD_INGOT,"gold", false,GeneratorType.IRON),
    REDSTONE(Items.REDSTONE,"redstone", true,GeneratorType.GOLD),
    LAPIS(Items.LAPIS_LAZULI,"lapis", true,GeneratorType.GOLD),
    QUARTZ(Items.QUARTZ,"quartz", true,GeneratorType.IRON),
    DIAMOND(Items.DIAMOND,"diamond", false,GeneratorType.GOLD),
    EMERALD(Items.EMERALD,"emerald", true,GeneratorType.DIAMOND),
    NETHERITE(Items.NETHERITE_SCRAP,"netherite", false,GeneratorType.DIAMOND),
    MODIUM(fr.iglee42.techresourcesbase.init.ModItem.MODIUM_INGOT.get(),"modium",true,GeneratorType.NETHERITE),
    DERIUM(fr.iglee42.techresourcesbase.init.ModItem.DERIUM_INGOT.get(), "derium",true,GeneratorType.MODIUM),
    BLAZUM(fr.iglee42.techresourcesbase.init.ModItem.BLAZUM_INGOT.get(), "blazum",true,GeneratorType.DERIUM),
    LAVIUM(fr.iglee42.techresourcesbase.init.ModItem.LAVIUM_INGOT.get(), "lavium",true,GeneratorType.BLAZUM)
    ;

    private Item item;
    private boolean isOnlyElectronic;
    private String resourceName;

    private GeneratorType minimumGenerator;
    GessenceType(Item item, String resourceName, boolean isOnlyElectronic, GeneratorType minimumGenerator) {
        this.item = item;
        this.resourceName = resourceName;
        this.isOnlyElectronic = isOnlyElectronic;
        this.minimumGenerator = minimumGenerator;
    }

    public static GessenceType getByResourceName(String name){
        for (GessenceType type : values()){
            if (Objects.equals(type.getRessourceName(), name)) return type;
        }
        return null;
    }

    public static GessenceType getByItem(Item item) {
        return Gessence.isGessence(item) ? ((Gessence)item).getType() : GessenceType.WOOD;
    }

    public static boolean isGeneratorValidForGessence(ItemStack stack,GeneratorType type){
        return getByItem(stack.getItem()).getMinimumGenerator().getOrder() <= type.getOrder();
    }

    public Item getItem() {return item;
    }

    public GeneratorType getMinimumGenerator() {
        return minimumGenerator;
    }

    public String getRessourceName() {
        return resourceName;
    }

    public boolean isOnlyElectronic() {
        return isOnlyElectronic;
    }
}
