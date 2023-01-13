package fr.iglee42.techresourcesgenerator.jei;

import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiGessenceOutputRecipe {

    @Unmodifiable
    Ingredient getInput();

    @Unmodifiable
    Ingredient getOutput();



}
