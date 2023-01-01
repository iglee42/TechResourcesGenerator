package fr.iglee42.techresourcesgenerator.blocks.generator.manual;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class ManualGenerator extends GeneratorBlock {
    public ManualGenerator(GeneratorType type) {
        super(Properties.of(Material.METAL).strength(2.0F, 6.0F).sound(SoundType.METAL).noOcclusion().harvestTool(ToolType.AXE).harvestTool(ToolType.PICKAXE),type);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ManualGeneratorTile();
    }
}
