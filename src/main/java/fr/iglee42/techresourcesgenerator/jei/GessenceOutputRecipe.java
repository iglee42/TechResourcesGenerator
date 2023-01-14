package fr.iglee42.techresourcesgenerator.jei;

import net.minecraft.item.crafting.Ingredient;

public class GessenceOutputRecipe implements IJeiGessenceOutputRecipe{
    public static final int recipeWidth = 160;
    public static final int recipeHeight = 20;
    private final Ingredient input;
    private final Ingredient output;
    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
    public Ingredient getOutput() {
        return output;
    }

    public GessenceOutputRecipe(Ingredient input, Ingredient output) {
        this.input = input;
        this.output = output;
    }
}