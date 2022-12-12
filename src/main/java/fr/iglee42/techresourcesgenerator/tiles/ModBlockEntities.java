package fr.iglee42.techresourcesgenerator.tiles;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TechResourcesGenerator.MODID);

    public static final RegistryObject<BlockEntityType<?>> MANUAL_GENERATOR =TILE_ENTITIES.register("manual_generator_tile",() -> BlockEntityType.Builder.of(ManualGeneratorTile::new, ModBlock.BASIC_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<?>> MAGMATIC_GENERATOR =TILE_ENTITIES.register("magmatic_generator_tile",() -> BlockEntityType.Builder.of(MagmaticGeneratorTile::new,ModBlock.IRON_GENERATOR.get(),ModBlock.GOLD_GENERATOR.get(),ModBlock.DIAMOND_GENERATOR.get(),ModBlock.NETHERITE_GENERATOR.get()).build(null));

}
