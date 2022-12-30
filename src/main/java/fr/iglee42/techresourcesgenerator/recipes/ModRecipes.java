package fr.iglee42.techresourcesgenerator.recipes;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TechResourcesGenerator.MODID);


    public static RegistryObject<RecipeSerializer<CardInfuserRecipe>> CARD_INFUSER_SERIALIZER = SERIALIZER.register("card_infuser", CardInfuserRecipe.Serializer::new);


}
