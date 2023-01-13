package fr.iglee42.techresourcesgenerator.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class GessenceOutputRecipeCategory implements IRecipeCategory<IJeiGessenceOutputRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TechResourcesGenerator.MODID, "gessence_output");

    public static final RecipeType<IJeiGessenceOutputRecipe> RECIPE_TYPE = RecipeType.create(TechResourcesGenerator.MODID, "gessence_output",
            GessenceOutputRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawable slotBackground;

    private final IDrawable arrow;

    public GessenceOutputRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(GessenceOutputRecipe.recipeWidth,GessenceOutputRecipe.recipeHeight);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItem.BASE_GESSENCE.get()));
        this.slotBackground = helper.getSlotDrawable();
        this.arrow = helper.createDrawable(new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/jei_arrow.png"),0,0,64,8);
    }


    @Override
    public RecipeType<IJeiGessenceOutputRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Gessence Output");
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
    public void draw(IJeiGessenceOutputRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        //this.resultArrow.draw(stack,117,18);
        int xPos = (GessenceOutputRecipe.recipeWidth - 64) / 2;
        this.arrow.draw(stack , 0, 0);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IJeiGessenceOutputRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        int xPos = (GessenceOutputRecipe.recipeWidth - 64) / 2;

        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, xPos - 16, 1)
                .setBackground(slotBackground, -1, -1);
        IRecipeSlotBuilder outputSlotBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, xPos + 64, 1)
                .setBackground(slotBackground, -1, -1);
        inputSlotBuilder.addIngredients(recipe.getInput());
        outputSlotBuilder.addIngredients(recipe.getOutput());
    }

}