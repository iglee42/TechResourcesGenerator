package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.igleelib.IgleeLibrary;
import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

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
    MODIUM(new ResourceLocation(IgleeLibrary.MODID,"modium_ingot"),"modium",false,GeneratorType.NETHERITE),
    DERIUM(new ResourceLocation(IgleeLibrary.MODID,"derium_ingot"), "derium",true,GeneratorType.MODIUM),
    BLAZUM(new ResourceLocation(IgleeLibrary.MODID,"blazum_ingot"), "blazum",true,GeneratorType.DERIUM),
    LAVIUM(new ResourceLocation(IgleeLibrary.MODID,"lavium_ingot"), "lavium",true,GeneratorType.BLAZUM)
    ;

    private ResourceLocation item;
    private String resourceName;
    private boolean isOnlyElectronic;
    private GeneratorType minimumGenerator;
    GessenceType(ResourceLocation item,  String resourceName,boolean isOnlyElectronic, GeneratorType minimumGenerator) {
        this.item = item;
        this.isOnlyElectronic = isOnlyElectronic;
        this.resourceName = resourceName;
        this.minimumGenerator = minimumGenerator;
    }
    GessenceType(Item item, String resourceName, boolean isOnlyElectronic, GeneratorType minimumGenerator) {
        this(ForgeRegistries.ITEMS.getKey(item),resourceName,isOnlyElectronic,minimumGenerator);
    }

    public ResourceLocation getItemLocation(){
        return item;
    }

    public Item getItem() {return ForgeRegistries.ITEMS.getValue(item);}

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
