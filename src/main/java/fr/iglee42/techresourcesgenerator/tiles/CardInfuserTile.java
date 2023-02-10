package fr.iglee42.techresourcesgenerator.tiles;

import fr.iglee42.igleelib.api.blockentities.SecondTileEntity;
import fr.iglee42.techresourcesgenerator.blocks.BlockCardInfuser;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.CardInfuserProgressSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.ItemStackSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.recipes.CardInfuserRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CardInfuserTile extends TileEntity implements ITickableTileEntity {

    private int tick;
    private boolean active;

    private int progress;

    private CardInfuserRecipe recipe;

    public CardInfuserTile() {
        super(ModBlockEntities.CARD_INFUSER.get());
    }
    public void tick() {
        tick(this.level,worldPosition,getBlockState());
    }

    protected void second(World level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide()){
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(itemHandler,worldPosition));
        }
        //level.setBlockAndUpdate(blockPos,blockState.setValue(BlockCardInfuser.HAS_ITEM,!itemHandler.getStackInSlot(0).isEmpty()));
        recipeProgress();
        signUpdate(level, blockPos, blockState);
    }

    private void signUpdate(World level, BlockPos blockPos, BlockState blockState) {
        if (level.getBlockState(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(), 1)).getBlock() instanceof WallSignBlock &&
                level.getBlockEntity(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(), 1)) instanceof SignTileEntity  &&
                level.getBlockState(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(), 1)).getValue(WallSignBlock.FACING).getOpposite() == blockState.getValue(BlockCardInfuser.FACING)) {
            SignTileEntity sign = (SignTileEntity) level.getBlockEntity(blockPos.relative(blockState.getValue(BlockCardInfuser.FACING).getOpposite(), 1));
            sign.setMessage(0, new StringTextComponent(""));
            sign.setMessage(1, new StringTextComponent(""));
            sign.setMessage(2, new StringTextComponent(""));
            sign.setMessage(3, new StringTextComponent(""));
            if (!itemHandler.getStackInSlot(2).isEmpty()) {
                sign.setMessage(1, new StringTextComponent("The output slot"));
                sign.setMessage(2, new StringTextComponent("is full"));
                sign.setColor(DyeColor.RED);
            } else if (active) {
                sign.setMessage(0, new StringTextComponent("Active"));
                sign.setMessage(1, new StringTextComponent("Progress : " + progress));
                sign.setMessage(2, new StringTextComponent("Recipe :"));
                sign.setMessage(3, new TranslationTextComponent(recipe.getResult().getItems()[0].getDescriptionId()));
                sign.setColor(DyeColor.LIME);
            } else {
                if (recipe == null && !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty()) {
                    sign.setMessage(1, new StringTextComponent("Invalid Recipe"));
                    sign.setColor(DyeColor.RED);
                } else {
                    sign.setMessage(1, new StringTextComponent("Inactive"));
                    sign.setColor(DyeColor.ORANGE);
                }
            }
        }
    }

    private void recipeProgress() {
        Inventory inventory = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        if (this.recipe == null || !this.recipe.matches(inventory, this.level)) {

            this.recipe = this.level.getRecipeManager()
                    .getRecipeFor(CardInfuserRecipe.Type.INSTANCE, inventory, this.level)
                    .orElse(null);
        }

        active = !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty() && itemHandler.getStackInSlot(2).isEmpty() && this.recipe != null;
        if (progress < 12 && active) {
            progress++;
            ModMessages.sendToClients(new CardInfuserProgressSyncS2CPacket(progress, this.getBlockPos()));
        }
        if (progress == 11) {
            LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(this.level);
            lightning.setDamage(1);
            lightning.setPos(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.5, this.getBlockPos().getZ() + 0.5);
            level.addFreshEntity(lightning);
        }
        if (progress == 12 && this.recipe != null) {
            this.active = false;
            this.progress = 0;
            ModMessages.sendToClients(new CardInfuserProgressSyncS2CPacket(progress, this.getBlockPos()));
            extractItemForce(0);
            extractItemForce(1);
            insertItemForce(2, new ItemStack(recipe.getResult().getItems()[0].getItem(), 1));
            this.recipe = null;
        }
    }

    public int getProgress() {
        return progress;
    }

    public void tick(World level, BlockPos pos, BlockState state) {
        tick++;
        if (tick == 20){
            tick = 0;
            second(level,pos,state);
        }
        if (active) {
            Vector3d bpos = Vector3d.atCenterOf(this.getBlockPos()).add(0, -0.5, 0);

            //TO BRANCHS
            if (progress <= 5) {
                switch (state.getValue(BlockCardInfuser.FACING)) {
                    case SOUTH:
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        break;
                    case NORTH:
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        break;
                    case WEST:
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        break;
                    case EAST:
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(-0.8, 1.8, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, -0.8), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 0.8, 0), bpos.add(0, 1.8, 0.8), itemHandler.getStackInSlot(0));
                        break;
                    default:
                        break;
                }

                //spawnItemParticles(bpos.add(0,0.8,0),bpos.add(0,1.9,0.8), itemHandler.getStackInSlot(0));
            }
            //TO CORE
            if (progress > 5 && progress <= 10) {
                switch (state.getValue(BlockCardInfuser.FACING)) {
                    case NORTH:
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        break;
                    case SOUTH:
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        break;
                    case EAST:
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(-0.8, 1.2, 0), bpos.add(-0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        break;
                    case WEST:
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0.8, 1.2, 0), bpos.add(0.4, 0.1, 0), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, 0.8), bpos.add(0, 0.1, 0.4), itemHandler.getStackInSlot(0));

                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        spawnItemParticles(bpos.add(0, 1.2, -0.8), bpos.add(0, 0.1, -0.4), itemHandler.getStackInSlot(0));
                        break;
                    default:
                        break;
                }
            }
        }
    }
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public @Nonnull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot == 2) return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 || slot == 1) return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }
    };

        public ItemStack extractItemForce(int slot) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
            setChanged();
            return stack;
        }

        public void insertItemForce(int slot, ItemStack stack) {
            itemHandler.setStackInSlot(slot, stack);
            setChanged();
        }

        @Override
        public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
            return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyItemHandler.cast() : super.getCapability(cap, side);
        }

        @Override
        public void invalidateCaps() {
            super.invalidateCaps();
            lazyItemHandler.invalidate();
        }

        @Override
        public void setChanged() {
            super.setChanged();

        }

        @Override
        public void onLoad() {
            super.onLoad();
            lazyItemHandler = LazyOptional.of(() -> itemHandler);
        }

        @Override
        public void load(BlockState state,CompoundNBT tag) {
            tick = tag.getInt("tick");
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
            active = tag.getBoolean("active");
            progress = tag.getInt("progress");
            if (tag.contains("recipeId"))
                recipe = this.level.getRecipeManager().getAllRecipesFor(CardInfuserRecipe.Type.INSTANCE).stream().filter(r -> Objects.equals(r.getId(), new ResourceLocation(tag.getString("recipeId")))).findFirst().orElse(null);
            super.load(state,tag);
        }

        @Override
        public CompoundNBT save(CompoundNBT tag) {
            tag.putInt("tick",tick);
            tag.put("inventory", itemHandler.serializeNBT());
            tag.putBoolean("active", active);
            tag.putInt("progress", progress);
            if (recipe != null) tag.putString("recipeId", recipe.getId().toString());
            return super.save(tag);
        }

        public void setHandler(ItemStackHandler itemStackHandler) {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
            }
        }

        private void spawnItemParticles(Vector3d fromPos, Vector3d goPos, ItemStack stack) {
            if (this.getLevel() == null || this.getLevel().isClientSide() || stack.isEmpty())
                return;

            ServerWorld level = (ServerWorld) this.getLevel();

            double x = fromPos.x();
            double y = fromPos.y();
            double z = fromPos.z();

            double velX = goPos.x() - fromPos.x();
            double velY = goPos.y() - fromPos.y();
            double velZ = goPos.z() - fromPos.z();

            level.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
        }

        public boolean isActive() {
            return this.active;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public void dropContent() {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemEntity item = new ItemEntity(this.level,worldPosition.getX() + 0.5,worldPosition.getY()+ 1,worldPosition.getZ() + 0.5,itemHandler.getStackInSlot(i));
                item.setDefaultPickUpDelay();
                level.addFreshEntity(item);
            }
        }
    }
