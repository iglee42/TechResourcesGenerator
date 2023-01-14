package fr.iglee42.techresourcesgenerator.menu;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuTypes {
    public static final DeferredRegister<ContainerType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, TechResourcesGenerator.MODID);

    public static final RegistryObject<ContainerType<MagmaticGeneratorMenu>> MAGMATIC_GENERATOR_MENU = MENUS.register("magmatic_generator_menu",
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World world = inv.player.level;
                        return new MagmaticGeneratorMenu(windowId,  inv,world.getBlockEntity(pos), Generator.getByName("iron"));
                    })));
    public static final RegistryObject<ContainerType<ElectricGeneratorMenu>> ELECTRIC_GENERATOR_MENU = MENUS.register("electric_generator_menu",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new ElectricGeneratorMenu(windowId,  inv,world.getBlockEntity(pos), Generator.getByName("modium"));
            })));




}