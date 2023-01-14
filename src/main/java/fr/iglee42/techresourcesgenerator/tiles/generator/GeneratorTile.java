package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class GeneratorTile extends TileEntity implements ITickableTileEntity {

    private Generator generatorType;
    private Gessence type;
    protected boolean enabled;
    private int tick = 0;

    private float delay;
    public GeneratorTile(TileEntityType<?> p_i48289_1_,Generator generatorType) {
        super(p_i48289_1_);
        this.generatorType = generatorType;
        this.delay = getDelayBetweenItem();
    }


    public static float getDelay(ServerWorld level, BlockPos blockPos) {
        return level.getBlockEntity(blockPos) instanceof GeneratorTile ? ((GeneratorTile)level.getBlockEntity(blockPos)).getDelay() : 0;
    }

    @Override
    public void tick() {
        tick(this.level,worldPosition,this.getBlockState(),this);
    }

    public void tick(World level, BlockPos pos, BlockState state, GeneratorTile tile) {

        tick++;
        if (tick == 20){ this.second(level, pos, state, tile); tick = 0;}
    }

    public boolean isEnabled() {
        return enabled;
    }



    protected void second(World level, BlockPos pos, BlockState state, GeneratorTile tile) {
        enabled = hasGessence();
        if (enabled)delay = delay - 1;
       /*if (delay == 0) {
           if (type == null) return;
           if (output_stack == null || output_stack.isEmpty()){
               output_stack = new ItemStack(type.getItem(),itemPerSecond);
           } else {
               output_stack.setCount(output_stack.getCount() + itemPerSecond);
           }
           BlockPos outpos = pos.offset(0,1,0);
           Block.popResource(level,outpos,output_stack);
           output_stack = null;
           delay = delayBetweenItem;
       }*/
    }
    public void resetDelay(){
        this.setDelay(this.getDelayBetweenItem());
    }


    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putInt("tick",tick);
        if (type != null)tag.putString("gessence",type.name());
        tag.putFloat("delay",delay);
        return super.save(tag);
    }

    @Override
    public void load(BlockState state,CompoundNBT tag) {
        super.load(state,tag);
        tick = tag.getInt("tick");
        delay = tag.getFloat("delay");
        if (tag.contains("gessence")) type = Gessence.getByName(tag.getString("gessence"));
    }

    public boolean hasGessence(){
        return type != null;
    }

    public Gessence getGessence() {
        return type;
    }

    public void setGessence(Gessence type){
        this.type = type;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public int getDelayBetweenItem() {
        return generatorType.isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(generatorType.name())).getDelay() : generatorType.delay();
    }

    public int getItemsDropped() {
        return generatorType.isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(generatorType.name())).getItemCount() : generatorType.itemCount();
    }

    public Generator getGeneratorType() {
        return generatorType;
    }

    public abstract boolean generateItem();

    public void setGeneratorType(Generator generatorType) {
        this.generatorType = generatorType;
    }
}
