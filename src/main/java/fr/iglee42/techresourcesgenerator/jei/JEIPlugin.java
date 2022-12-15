package fr.iglee42.techresourcesgenerator.jei;

import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
    public void registerRecipes(IRecipeRegistration registration){
        registration.addIngredientInfo(new ItemStack(ModBlock.BASIC_GENERATOR.get()),VanillaTypes.ITEM_STACK,Component.literal("Right click with a gessence to put in the generator. \n\nSneak + Right Click to remove the gessence in the generator \n\nRight click with an empty hand to decrease the delay."));
        List<Block> generator = Arrays.asList(ModBlock.IRON_GENERATOR.get(),ModBlock.GOLD_GENERATOR.get(),ModBlock.DIAMOND_GENERATOR.get(),ModBlock.NETHERITE_GENERATOR.get());
        List<ItemStack> stacks = new ArrayList<>();
        generator.forEach(g->stacks.add(new ItemStack(g)));
        registration.addIngredientInfo(stacks,VanillaTypes.ITEM_STACK,Component.literal("Right click to open the gui"));
    }
}