package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.blocks.ModBlocks;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

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
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CARD_INFUSER.get()), CardInfuserRecipeCategory.UID);
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(rm.getAllRecipesFor(CardInfuserRecipe.Type.INSTANCE)
                        .stream()
                        .map(r -> (CardInfuserRecipe) r)
                        .collect(Collectors.toList()),CardInfuserRecipeCategory.UID);
        registration.addIngredientInfo(new ItemStack(ModBlocks.BASIC_GENERATOR.get()),VanillaTypes.ITEM,new StringTextComponent("Right click with a gessence to put in the generator. \n\nSneak + Right Click to remove the gessence in the generator \n\nRight click with an empty hand to decrease the delay."));
        List<Block> generator = Arrays.asList(ModBlocks.IRON_GENERATOR.get(),ModBlocks.GOLD_GENERATOR.get(),ModBlocks.DIAMOND_GENERATOR.get(),ModBlocks.NETHERITE_GENERATOR.get(),
                ModBlocks.MODIUM_GENERATOR.get(),ModBlocks.DERIUM_GENERATOR.get(),ModBlocks.BLAZUM_GENERATOR.get(),ModBlocks.LAVIUM_GENERATOR.get());
        List<ItemStack> stacks = new ArrayList<>();
        generator.forEach(g->stacks.add(new ItemStack(g)));
        registration.addIngredientInfo(stacks,VanillaTypes.ITEM,new StringTextComponent("Right click to open the gui"));

        registration.addIngredientInfo(new ItemStack(ModBlocks.CARD_INFUSER.get()),VanillaTypes.ITEM,new StringTextComponent("Right Click with an item on the top to give him. \n \n Right click with an item on the front to give. \n\n You can retrive items with a sneak + right click. You can get the result of a craft with a sneak right click on the bottom. \n \n You can also place a sign in his back to get informations."));
    }
}