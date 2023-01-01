package fr.iglee42.techresourcesgenerator.tiles;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TechResourcesGenerator.MODID);

    public static final RegistryObject<BlockEntityType<CardInfuserTile>> CARD_INFUSER = TILE_ENTITIES.register("card_infuser_tile",()-> BlockEntityType.Builder.of(CardInfuserTile::new,ModBlock.CARD_INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ManualGeneratorTile>> MANUAL_GENERATOR =TILE_ENTITIES.register("manual_generator_tile",() -> BlockEntityType.Builder.of(ManualGeneratorTile::new, ModBlock.BASIC_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<MagmaticGeneratorTile>> MAGMATIC_GENERATOR =TILE_ENTITIES.register("magmatic_generator_tile",() -> BlockEntityType.Builder.of(MagmaticGeneratorTile::new,ModBlock.IRON_GENERATOR.get(),ModBlock.GOLD_GENERATOR.get(),ModBlock.DIAMOND_GENERATOR.get(),ModBlock.NETHERITE_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricGeneratorTile>> ELECTRIC_GENERATOR =TILE_ENTITIES.register("electric_generator_tile",() -> BlockEntityType.Builder.of(ElectricGeneratorTile::new,ModBlock.MODIUM_GENERATOR.get(),ModBlock.DERIUM_GENERATOR.get(),ModBlock.BLAZUM_GENERATOR.get(),ModBlock.LAVIUM_GENERATOR.get()).build(null));

}
