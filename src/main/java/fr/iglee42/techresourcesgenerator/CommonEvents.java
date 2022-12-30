package fr.iglee42.techresourcesgenerator;

import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class CommonEvents {

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID)
    public class Forge{
        @SubscribeEvent
        public static void registerRecipeTypes(final RegisterEvent event) {
            event.register(ForgeRegistries.RECIPE_TYPES.getRegistryKey(), h->{
                h.register(new ResourceLocation(TechResourcesGenerator.MODID, CardInfuserRecipe.Type.ID),CardInfuserRecipe.Type.INSTANCE);
            });
        }
    }

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID,bus = EventBusSubscriber.Bus.MOD)
    public class Mod{

    }

}
