package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlocks;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CardInfuserRecipeCategory implements IRecipeCategory<CardInfuserRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TechResourcesGenerator.MODID, "card_infuser");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID, "textures/gui/card_infuser_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CardInfuserRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 92);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.CARD_INFUSER.get()));
    }




    @Override
    public @Nonnull String getTitle() {
        return "Card Infuser";
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(CardInfuserRecipe recipe, IIngredients ingredients) {
        List<Ingredient> ingredient = new ArrayList<>();
        ingredient.add(recipe.getBase());
        ingredient.add(recipe.getInfuser());
        ingredients.setInputIngredients(ingredient);
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getResultItem());
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CardInfuserRecipe> getRecipeClass() {
        return CardInfuserRecipe.class;
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayout builder, @Nonnull CardInfuserRecipe recipe, @Nonnull IIngredients ingredients) {

        builder.getItemStacks().init(0,true,11,69);
        builder.getItemStacks().init(1,true,12,8);
        builder.getItemStacks().init(2,false,115,69);
        builder.getItemStacks().set(ingredients);
    }


}