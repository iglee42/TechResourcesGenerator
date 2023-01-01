package fr.iglee42.techresourcesgenerator.recipes;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface ICardInfuserRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(TechResourcesGenerator.MODID, "card_infuser");

    @Override
    default IRecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }


}