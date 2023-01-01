package fr.iglee42.techresourcesgenerator;

import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class CommonEvents {

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID)
    public class Forge{
        @SubscribeEvent
        public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE,CardInfuserRecipe.Type.ID,CardInfuserRecipe.Type.INSTANCE);
        }
    }

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID,bus = EventBusSubscriber.Bus.MOD)
    public class Mod{

    }

}
