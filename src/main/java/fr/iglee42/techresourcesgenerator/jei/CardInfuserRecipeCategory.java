package fr.iglee42.techresourcesgenerator.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CardInfuserRecipeCategory implements IRecipeCategory<CardInfuserRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TechResourcesGenerator.MODID, "card_infuser");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID, "textures/gui/card_infuser_jei.png");

    public static final RecipeType<CardInfuserRecipe> RECIPE_TYPE = RecipeType.create(TechResourcesGenerator.MODID, "card_infuser",
            CardInfuserRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public CardInfuserRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlock.CARD_INFUSER.get()));
    }


    @Override
    public @NotNull RecipeType<CardInfuserRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return new TextComponent("Card Infuser");
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
    public void draw(CardInfuserRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        //this.resultArrow.draw(stack,117,18);
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CardInfuserRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 9).addIngredients(recipe.getInfuser());

        builder.addSlot(RecipeIngredientRole.INPUT, 12, 70).addIngredients(recipe.getBase());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 70).addItemStack(recipe.getResultItem());
        

    }


}