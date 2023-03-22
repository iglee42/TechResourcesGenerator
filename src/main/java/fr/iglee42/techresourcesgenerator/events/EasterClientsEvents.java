package fr.iglee42.techresourcesgenerator.events;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TechResourcesGenerator.MODID,bus = Bus.MOD,value = Dist.CLIENT)
public class EasterClientsEvents {
    public EasterClientsEvents() {
    }

    @SubscribeEvent
    public static void clientStuff(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ModItem.getGessenceCard(Types.getGessenceType("blazum")), new ResourceLocation("techresourcesgenerator", "easter"), (stack, level, living) -> {
                return stack.getDisplayName().getString().equals("[Code Lyoko]") ? 1.0F : 0.0F;
            });
        });
    }
}