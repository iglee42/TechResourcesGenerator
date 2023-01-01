package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManualGeneratorTile extends GeneratorTile {

    public ManualGeneratorTile() {
        super(ModBlockEntities.MANUAL_GENERATOR.get(), GeneratorType.BASIC);
    }


    @Override
    protected void second(World level, BlockPos pos, BlockState state, GeneratorTile tile) {
    }

    @Override
    public boolean generateItem() {
        Block.popResource(level,this.worldPosition.offset(0,1,0),new ItemStack(this.getGessence().getItem(),this.getItemsDropped()));
        setGessence(null);
        return true;
    }
}
