package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TechResourcesGenerator.MODID);

    public static RegistryObject<SoundEvent> EASTER = registerSoundEvent("easter_egg");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(TechResourcesGenerator.MODID, name)));
    }
}