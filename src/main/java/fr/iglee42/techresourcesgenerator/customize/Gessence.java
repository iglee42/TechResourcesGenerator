package fr.iglee42.techresourcesgenerator.customize;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Gessence{

    private final String name,item;
    private final boolean hasNormalGessence,hasElectronicGessence;
    private final String minimumGeneratorType,itemTexture;
    private final boolean isInModBase;

    public Gessence(String name, String item, boolean hasNormalGessence, boolean hasElectronicGessence, String minimumGeneratorType, String itemTexture, boolean isInModBase) {
        this.name = name;
        this.item = item;
        this.hasNormalGessence = hasNormalGessence;
        this.hasElectronicGessence = hasElectronicGessence;
        this.minimumGeneratorType = minimumGeneratorType;
        this.itemTexture = itemTexture;
        this.isInModBase = isInModBase;
    }

    public String name(){return name;}
    public String item(){return item;}
    public String minimumGeneratorType(){return minimumGeneratorType;}
    public String itemTexture(){return itemTexture;}

    public boolean hasNormalGessence() {return hasNormalGessence;}
    public boolean hasElectronicGessence() {return hasElectronicGessence;}
    public boolean isInModBase() {return isInModBase;}
    public static Gessence getByName(String name){
        return Types.getGessenceType(name);
    }
    public static Gessence getByItem(Item item) {
        return ItemGessence.isGessence(item) ? ((ItemGessence)item).getType() : Types.getGessenceType("wood");
    }
    public static Gessence getByItemCanBeNull(Item item) {
        return ItemGessence.isGessence(item) ? ((ItemGessence)item).getType() : null;
    }
    public static boolean isGeneratorValidForGessence(ItemStack stack, Generator type){
        return getByItem(stack.getItem()).getMinimumGenerator().getOrder() <= type.getOrder();
    }
    public Generator getMinimumGenerator(){
        return Generator.getByName(minimumGeneratorType());
    }

    public Item getItem(){
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item()));
    }

}
