package fr.iglee42.techresourcesgenerator.tiles;

import fr.iglee42.techresourcesbase.api.blockentities.SecondBlockEntity;
import fr.iglee42.techresourcesgenerator.blocks.BlockCardInfuser;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.CardInfuserProgressSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorDelaySyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorTypeSyncS2C;
import fr.iglee42.techresourcesgenerator.network.packets.ItemStackSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CardInfuserTile extends SecondBlockEntity {

    private boolean active;

    private int progress;

    private CardInfuserRecipe recipe;
    public CardInfuserTile(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CARD_INFUSER.get(), pos, state);
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity secondBlockEntity) {
        if (!level.isClientSide()){
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(itemHandler, worldPosition));
        }
        //level.setBlockAndUpdate(blockPos,blockState.setValue(BlockCardInfuser.HAS_ITEM,!itemHandler.getStackInSlot(0).isEmpty()));
        recipeProgress();
        signUpdate(level,blockPos,blockState);
    }

    private void signUpdate(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.getBlockState(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(),1)).getBlock() instanceof WallSignBlock &&
            level.getBlockEntity(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(),1)) instanceof SignBlockEntity sign &&
            level.getBlockState(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(),1)).getValue(WallSignBlock.FACING).getOpposite() == blockState.getValue(BlockCardInfuser.FACING)){
            sign.setMessage(0,Component.empty());
            sign.setMessage(1,Component.empty());
            sign.setMessage(2,Component.empty());
            sign.setMessage(3,Component.empty());
            if (!itemHandler.getStackInSlot(2).isEmpty()){
                sign.setMessage(1,Component.literal("The output slot"));
                sign.setMessage(2,Component.literal("is full"));
                sign.setColor(DyeColor.RED);
                sign.setHasGlowingText(true);
            }
            else if (active){
                sign.setMessage(0,Component.literal("Active"));
                sign.setMessage(1,Component.literal("Progress : " + progress));
                sign.setMessage(2, Component.literal("Recipe :"));
                sign.setMessage(3, Component.translatable(recipe.getResult().getItems()[0].getDescriptionId()));
                sign.setColor(DyeColor.LIME);
                sign.setHasGlowingText(true);
            } else {
                if (recipe == null && !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty()){
                    sign.setMessage(1,Component.literal("Invalid Recipe"));
                    sign.setColor(DyeColor.RED);
                    sign.setHasGlowingText(true);
                } else {
                    sign.setMessage(1,Component.literal("Inactive"));
                    sign.setColor(DyeColor.ORANGE);
                    sign.setHasGlowingText(true);
                }
            }
        }
    }

    private void recipeProgress() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        if (this.recipe == null || !this.recipe.matches(inventory,this.level)) {
            this.recipe = this.level.getRecipeManager()
                    .getRecipeFor(CardInfuserRecipe.Type.INSTANCE, inventory, this.level)
                    .orElse(null);
        }

        active = !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty() && itemHandler.getStackInSlot(2).isEmpty() && this.recipe != null;
        if (progress < 12 && active) {
            progress++;
            ModMessages.sendToClients(new CardInfuserProgressSyncS2CPacket(progress,this.getBlockPos()));
        }
        if (progress == 11){
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
            lightning.setDamage(1);
            lightning.setPos(Vec3.atCenterOf(this.getBlockPos()).add(0,1,0));
            level.addFreshEntity(lightning);
        }
        if (progress == 12 && this.recipe != null){
            this.active = false;
            this.progress = 0;
            ModMessages.sendToClients(new CardInfuserProgressSyncS2CPacket(progress,this.getBlockPos()));
            extractItemForce(0);
            extractItemForce(1);
            insertItemForce(2,new ItemStack(recipe.getResult().getItems()[0].getItem(),1));
            this.recipe = null;

        }
    }

    public int getProgress() {
        return progress;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CardInfuserTile entity) {
        entity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        super.tick(level,pos,state,this);
        if (active){
            Vec3 bpos = Vec3.atCenterOf(this.getBlockPos()).add(0,-0.5,0);

            //TO BRANCHS
            if (progress <= 5) {
                switch (state.getValue(BlockCardInfuser.FACING)){
                    case SOUTH -> {
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                    }
                    case NORTH ->{
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                    }
                    case WEST ->{
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                    }
                    case EAST ->{
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                    }
                }                //spawnItemParticles(bpos.add(0,0.8,0),bpos.add(0,1.9,0.8), itemHandler.getStackInSlot(0));
            }
            //TO CORE
            if (progress > 5 && progress <= 10) {
                switch(state.getValue(BlockCardInfuser.FACING)){
                    case NORTH -> {
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                    }
                    case SOUTH -> {
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                    }
                    case EAST -> {
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                    }
                    case WEST -> {
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                    }
                }            }
        }
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == 2) return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 || slot == 1) return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }


    };
    public ItemStack extractItemForce(int slot){
        ItemStack stack = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot,ItemStack.EMPTY);
        setChanged();
        return stack;
    }
    public void insertItemForce(int slot,ItemStack stack){
        itemHandler.setStackInSlot(slot,stack);
        setChanged();
    }
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? lazyItemHandler.cast() : super.getCapability(cap,side);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!level.isClientSide()){
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(itemHandler, worldPosition));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->itemHandler);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        active = tag.getBoolean("active");
        progress = tag.getInt("progress");
        if (tag.contains("recipeId"))recipe = this.level.getRecipeManager().getAllRecipesFor(CardInfuserRecipe.Type.INSTANCE).stream().filter(r-> Objects.equals(r.getId(), new ResourceLocation(tag.getString("recipeId")))).findFirst().orElse(null);

    }



    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putBoolean("active",active);
        tag.putInt("progress",progress);
        if (recipe != null) tag.putString("recipeId",recipe.getId().toString());
    }
    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
    private void spawnItemParticles(Vec3 fromPos, Vec3 goPos, ItemStack stack) {
        if (this.getLevel() == null || this.getLevel().isClientSide() || stack.isEmpty())
            return;

        var level = (ServerLevel) this.getLevel();

        double x = fromPos.x();
        double y = fromPos.y();
        double z = fromPos.z();

        double velX = goPos.x() - fromPos.x();
        double velY = goPos.y() - fromPos.y();
        double velZ = goPos.z() - fromPos.z();

        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void dropContent() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
