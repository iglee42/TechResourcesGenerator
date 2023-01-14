package fr.iglee42.techresourcesgenerator.customize.custompack;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;

import java.nio.file.Path;
import java.util.function.Consumer;

public class TRGPackFinder implements IPackFinder {


	private final PackType type;

	public TRGPackFinder(PackType type) {

		this.type = type;
	}


	@Override
	public void loadPacks(Consumer<ResourcePackInfo> infoConsumer, ResourcePackInfo.IFactory infoFactory) {
		Path rootPath = PathConstant.ROOT_PATH;

		ResourcePackInfo pack = ResourcePackInfo.create("trg_" + type.getSuffix(), true,
				() -> new InMemoryPack(rootPath), infoFactory, ResourcePackInfo.Priority.BOTTOM,  IPackNameDecorator.BUILT_IN);
		if (pack != null) {
			infoConsumer.accept(pack);
		}
	}
}