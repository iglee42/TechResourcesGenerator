package fr.iglee42.techresourcesgenerator;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class CommonEvents {

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID)
    public class Forge{

    }

    @EventBusSubscriber(modid = TechResourcesGenerator.MODID,bus = EventBusSubscriber.Bus.MOD)
    public class Mod{

    }

}
