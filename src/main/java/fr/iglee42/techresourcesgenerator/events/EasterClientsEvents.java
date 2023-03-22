package fr.iglee42.techresourcesgenerator.events;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TechResourcesGenerator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EasterClientsEvents {


    @SubscribeEvent
    public static void clientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItem.getGessenceCard(Types.getGessenceType("blazum")),
                    new ResourceLocation(TechResourcesGenerator.MODID, "easter"), (stack, level, living,id) ->{
               return (stack.getDisplayName().getString().equals("[Code Lyoko]") ? 1.0F : 0.0F);
            });
        });

    }

}
