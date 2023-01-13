package fr.iglee42.techresourcesgenerator.mixin;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(ResourcePackLoader.class)
public class ResourceLoaderMixin {
	@Inject(method = "loadResourcePacks(Lnet/minecraft/server/packs/repository/PackRepository;Ljava/util/function/Function;)V", at = @At("RETURN"), remap = false)
	private static void loadResourcePacks(PackRepository resourcePacks, Function<Map<IModFile, ? extends PathPackResources>, ? extends RepositorySource> packFinder, CallbackInfo ci) {

		TechResourcesGenerator.injectDatapackFinder(resourcePacks);
	}
}