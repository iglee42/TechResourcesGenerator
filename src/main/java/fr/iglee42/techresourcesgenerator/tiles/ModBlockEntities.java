package fr.iglee42.techresourcesgenerator.tiles;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.blocks.ModBlocks;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TechResourcesGenerator.MODID);

    public static final RegistryObject<TileEntityType<CardInfuserTile>> CARD_INFUSER = TILE_ENTITIES.register("card_infuser_tile",()-> TileEntityType.Builder.of(CardInfuserTile::new,ModBlocks.CARD_INFUSER.get()).build(null));
    public static final RegistryObject<TileEntityType<ManualGeneratorTile>> MANUAL_GENERATOR =TILE_ENTITIES.register("manual_generator_tile",() -> TileEntityType.Builder.of(ManualGeneratorTile::new, ModBlocks.getAllGeneratorForType("basic")).build(null));
    public static final RegistryObject<TileEntityType<MagmaticGeneratorTile>> MAGMATIC_GENERATOR =TILE_ENTITIES.register("magmatic_generator_tile",() -> TileEntityType.Builder.of(MagmaticGeneratorTile::new,ModBlocks.getAllGeneratorForType("magmatic")).build(null));
    public static final RegistryObject<TileEntityType<ElectricGeneratorTile>> ELECTRIC_GENERATOR =TILE_ENTITIES.register("electric_generator_tile",() -> TileEntityType.Builder.of(ElectricGeneratorTile::new,ModBlocks.getAllGeneratorForType("electric")).build(null));

}
