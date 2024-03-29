package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("techresourcesmods","generator");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                CardInfuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                GessenceOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlock.CARD_INFUSER.get()), new RecipeType<>(CardInfuserRecipeCategory.UID, CardInfuserRecipe.class));
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(CardInfuserRecipeCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(CardInfuserRecipe.Type.INSTANCE)
                        .stream()
                        .map(r -> (CardInfuserRecipe) r)
                        .collect(Collectors.toList()));
        List<IJeiGessenceOutputRecipe> gessenceOutputRecipes = new ArrayList<>();
        Types.GESSENCES.forEach(g->{
            if (g.hasNormalGessence())gessenceOutputRecipes.add(new GessenceOutputRecipe(Ingredient.of(ModItem.getGessence(g)),Ingredient.of(g.getItem())));
            if (g.hasElectronicGessence())gessenceOutputRecipes.add(new GessenceOutputRecipe(Ingredient.of(ModItem.getGessenceCard(g)),Ingredient.of(g.getItem())));
        });
        registration.addRecipes(GessenceOutputRecipeCategory.RECIPE_TYPE,gessenceOutputRecipes);
        registration.addIngredientInfo(new ItemStack(ModBlock.getGenerator(Generator.getByName("basic"))),VanillaTypes.ITEM_STACK,new TextComponent("Right click with a gessence to put in the generator. \n\nSneak + Right Click to remove the gessence in the generator \n\nRight click with an empty hand to decrease the delay."));
        List<Block> generator = new ArrayList<>();
        generator.addAll(Arrays.asList(ModBlock.getAllGeneratorForType("magmatic")));
        generator.addAll(Arrays.asList(ModBlock.getAllGeneratorForType("electric")));
        List<ItemStack> stacks = new ArrayList<>();
        generator.forEach(g->stacks.add(new ItemStack(g)));
        registration.addIngredientInfo(stacks,VanillaTypes.ITEM_STACK,new TextComponent("Right click to open the gui"));

        registration.addIngredientInfo(new ItemStack(ModBlock.CARD_INFUSER.get()),VanillaTypes.ITEM_STACK,new TextComponent("Right Click with an item on the top to give him. \n \n Right click with an item on the front to give. \n\n You can retrive items with a sneak + right click. You can get the result of a craft with a sneak right click on the bottom. \n \n You can also place a sign in his back to get informations."));
    }
}