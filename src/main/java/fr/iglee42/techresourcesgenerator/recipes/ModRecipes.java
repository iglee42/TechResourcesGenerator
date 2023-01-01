package fr.iglee42.techresourcesgenerator.recipes;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TechResourcesGenerator.MODID);


    public static RegistryObject<IRecipeSerializer<CardInfuserRecipe>> CARD_INFUSER_SERIALIZER = SERIALIZER.register("card_infuser", CardInfuserRecipe.Serializer::new);

    public static void register(IEventBus bus){
        SERIALIZER.register(bus);

        Registry.register(Registry.RECIPE_TYPE,CardInfuserRecipe.TYPE_ID,CardInfuserRecipe.Type.INSTANCE);
    }

}
