package fr.iglee42.techresourcesgenerator.blocks;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.generator.automatic.ElectricGenerator;
import fr.iglee42.techresourcesgenerator.blocks.generator.manual.MagmaticGenerator;
import fr.iglee42.techresourcesgenerator.blocks.generator.manual.ManualGenerator;
import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TechResourcesGenerator.MODID);

    public static final RegistryObject<Block> BASIC_GENERATOR = createManualGenerator("basic");
    public static final RegistryObject<Block> IRON_GENERATOR = createMagmaticGenerator("iron", GeneratorType.IRON);
    public static final RegistryObject<Block> GOLD_GENERATOR = createMagmaticGenerator("gold", GeneratorType.GOLD);
    public static final RegistryObject<Block> DIAMOND_GENERATOR = createMagmaticGenerator("diamond", GeneratorType.DIAMOND);
    public static final RegistryObject<Block> NETHERITE_GENERATOR = createMagmaticGenerator("netherite", GeneratorType.NETHERITE);
    public static final RegistryObject<Block> MODIUM_GENERATOR = createElectricGenerator("modium", GeneratorType.MODIUM);
    public static final RegistryObject<Block> DERIUM_GENERATOR = createElectricGenerator("derium", GeneratorType.DERIUM);
    public static final RegistryObject<Block> BLAZUM_GENERATOR = createElectricGenerator("blazum", GeneratorType.BLAZUM);
    public static final RegistryObject<Block> LAVIUM_GENERATOR = createElectricGenerator("lavium", GeneratorType.LAVIUM);

    public static RegistryObject<Block> createManualGenerator(String name){
        return createBlock(name + "_generator",()-> new ManualGenerator(GeneratorType.BASIC));
    }

    public static RegistryObject<Block> createMagmaticGenerator(String name, GeneratorType type){
        return createBlock(name + "_generator",()-> new MagmaticGenerator(type));
    }

    public static RegistryObject<Block> createElectricGenerator(String name, GeneratorType type){
        return createBlock(name + "_generator",()-> new ElectricGenerator(type));
    }

    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(TechResourcesGenerator.GENERATOR_GROUP)));
        return block;
    }
    public static RegistryObject<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        return block;
    }
}
