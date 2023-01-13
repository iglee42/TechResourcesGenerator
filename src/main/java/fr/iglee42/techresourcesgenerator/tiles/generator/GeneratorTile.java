package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GeneratorTile extends BlockEntity {

    private Generator generatorType;
    private Gessence type;
    protected boolean enabled;
    private int tick = 0;

    private float delay;
    public GeneratorTile(BlockEntityType<?> p_i48289_1_, BlockState state, BlockPos pos, Generator generatorType) {
        super(p_i48289_1_,pos,state);
        this.generatorType = generatorType;
        this.delay = getDelayBetweenItem();
    }


    public static float getDelay(ServerLevel level, BlockPos blockPos) {
        return level.getBlockEntity(blockPos) instanceof GeneratorTile ? ((GeneratorTile)level.getBlockEntity(blockPos)).getDelay() : 0;
    }


    public void tick(Level level, BlockPos pos, BlockState state,GeneratorTile tile) {

        tick++;
        if (tick == 20){ this.second(level, pos, state, tile); tick = 0;}
    }

    public boolean isEnabled() {
        return enabled;
    }



    protected void second(Level level, BlockPos pos, BlockState state, GeneratorTile tile) {
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
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("tick",tick);
        if (type != null)tag.putString("gessence",type.name());
        tag.putFloat("delay",delay);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
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
