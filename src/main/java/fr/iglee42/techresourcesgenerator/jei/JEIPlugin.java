package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
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
        registration.addIngredientInfo(new ItemStack(ModBlock.BASIC_GENERATOR.get()),VanillaTypes.ITEM_STACK,new TextComponent("Right click with a gessence to put in the generator. \n\nSneak + Right Click to remove the gessence in the generator \n\nRight click with an empty hand to decrease the delay."));
        List<Block> generator = Arrays.asList(ModBlock.IRON_GENERATOR.get(),ModBlock.GOLD_GENERATOR.get(),ModBlock.DIAMOND_GENERATOR.get(),ModBlock.NETHERITE_GENERATOR.get(),
                ModBlock.MODIUM_GENERATOR.get(),ModBlock.DERIUM_GENERATOR.get(),ModBlock.BLAZUM_GENERATOR.get(),ModBlock.LAVIUM_GENERATOR.get());
        List<ItemStack> stacks = new ArrayList<>();
        generator.forEach(g->stacks.add(new ItemStack(g)));
        registration.addIngredientInfo(stacks,VanillaTypes.ITEM_STACK,new TextComponent("Right click to open the gui"));

        registration.addIngredientInfo(new ItemStack(ModBlock.CARD_INFUSER.get()),VanillaTypes.ITEM_STACK,new TextComponent("Right Click with an item on the top to give him. \n \n Right click with an item on the front to give. \n\n You can retrive items with a sneak + right click. You can get the result of a craft with a sneak right click on the bottom. \n \n You can also place a sign in his back to get informations."));
    }
}