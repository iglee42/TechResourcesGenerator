package fr.iglee42.techresourcesgenerator.customize;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public record Gessence(String name, String item,boolean hasNormalGessence,boolean hasElectronicGessence,String minimumGeneratorType,String itemTexture,boolean isInModBase) {

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
