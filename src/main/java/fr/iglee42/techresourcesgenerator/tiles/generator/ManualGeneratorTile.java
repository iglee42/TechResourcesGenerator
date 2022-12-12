package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ManualGeneratorTile extends GeneratorTile {

    public ManualGeneratorTile( BlockState state, BlockPos pos) {
        super(ModBlockEntities.MANUAL_GENERATOR.get(), state, pos, GeneratorType.BASIC);
    }
    public ManualGeneratorTile(BlockPos pos,BlockState state) {
        this(state, pos);
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, GeneratorTile tile) {
    }

    @Override
    public boolean generateItem() {
        Block.popResource(level,this.worldPosition.offset(0,1,0),new ItemStack(this.getGessence().getItem(),this.getItemsDropped()));
        setGessence(null);
        return true;
    }
}
