package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.items.ModItem;
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

public class GessenceOutputRecipeCategory implements IRecipeCategory<IJeiGessenceOutputRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TechResourcesGenerator.MODID, "gessence_output");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawable slotBackground;

    private final IDrawable arrow;

    public GessenceOutputRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(GessenceOutputRecipe.recipeWidth,GessenceOutputRecipe.recipeHeight);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItem.BASE_GESSENCE.get()));
        this.slotBackground = helper.getSlotDrawable();
        this.arrow = helper.createDrawable(new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/jei_arrow.png"),0,0,64,8);
    }

    @Override
    public String getTitle() {
        return "Gessence Output";
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
    public void setIngredients(IJeiGessenceOutputRecipe recipe, IIngredients ingredients) {
        List<Ingredient> ingredient = new ArrayList<>();
        ingredient.add(recipe.getInput());
        ingredients.setInputIngredients(ingredient);
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getOutput().getItems()[0]);
    }

    @Override
    public Class<? extends IJeiGessenceOutputRecipe> getRecipeClass() {
        return IJeiGessenceOutputRecipe.class;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }



    @Override
    public void setRecipe(@Nonnull IRecipeLayout builder, @Nonnull IJeiGessenceOutputRecipe recipe, @Nonnull IIngredients ingredients) {
        int xPos = (GessenceOutputRecipe.recipeWidth - 64) / 2;
        builder.getItemStacks().init(0,true,xPos-16,1);
        builder.getItemStacks().init(1,false,xPos+64,1);
        builder.getItemStacks().setBackground(0,slotBackground);
        builder.getItemStacks().setBackground(1,slotBackground);
        builder.getItemStacks().set(ingredients);
    }

}