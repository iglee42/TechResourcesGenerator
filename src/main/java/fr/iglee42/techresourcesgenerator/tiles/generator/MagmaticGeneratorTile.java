package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.FluidSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorGenerateReturnS2CPacket;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagmaticGeneratorTile extends GeneratorTile implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && simulate) return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if ((slot == 1 || slot == 3) && simulate) return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MagmaticGeneratorTile(BlockState state, BlockPos pos,GeneratorType generatorType) {
        super(ModBlockEntities.MAGMATIC_GENERATOR.get(), state, pos,generatorType);
    }
    public MagmaticGeneratorTile(BlockPos pos, BlockState state) {
        this( state, pos,GeneratorType.IRON);
    }

    @Override
    public boolean hasGessence() {
        return super.hasGessence();
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, GeneratorTile tile) {
        this.setGessence(GessenceType.getByItemCanBeNull(itemHandler.getStackInSlot(2).getItem()));
        if (LAVA_TANK.getFluidInTank(0).getAmount() <= 7000){
            if (level.getFluidState(pos.offset(0,1,0)).is(Fluids.LAVA)){
                level.setBlockAndUpdate(pos.offset(0,1,0), Blocks.AIR.defaultBlockState());
                setFluid(new FluidStack(Fluids.LAVA,LAVA_TANK.getFluidInTank(0).getAmount() + 1000));
                setChanged();
            }
        }
        if (hasEnougthLavaForProcess() && getDelay() > 0){
            setDelay(getDelay() - 1);
            setFluid(new FluidStack(Fluids.LAVA,LAVA_TANK.getFluidInTank(0).getAmount() - 100));
            setChanged();
        }
        ItemStack slot1 = itemHandler.getStackInSlot(0);
        ItemStack slot2 = itemHandler.getStackInSlot(1);
        if (slot1.is(Items.BUCKET)){
            if (slot2.isEmpty()){
                if (getFluidStack().getAmount() >= 1000){
                    setFluid(new FluidStack(Fluids.LAVA,LAVA_TANK.getFluidInTank(0).getAmount() - 1000));
                    setChanged();
                    itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET,slot1.getCount() - 1));
                    itemHandler.setStackInSlot(1,new ItemStack(Items.LAVA_BUCKET));
                }
            }
        } else if (slot1.is(Items.LAVA_BUCKET)){
            if (slot2.isEmpty() || slot2.is(Items.BUCKET)){
                if (getFluidStack().getAmount() <= 7000) {
                    if (slot2.isEmpty()) {
                        setFluid(new FluidStack(Fluids.LAVA, LAVA_TANK.getFluidInTank(0).getAmount() + 1000));
                        setChanged();
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                        itemHandler.setStackInSlot(1, new ItemStack(Items.BUCKET));
                    } else if (slot2.getCount() < Items.BUCKET.getMaxStackSize()) {
                        setFluid(new FluidStack(Fluids.LAVA, LAVA_TANK.getFluidInTank(0).getAmount() + 1000));
                        setChanged();
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                        itemHandler.setStackInSlot(1, new ItemStack(Items.BUCKET, slot2.getCount() + 1));
                    }
                }
            }
        }

    }


    public int getTankCapacity(int tank){
        return LAVA_TANK.getTankCapacity(tank);
    }

    public boolean hasEnougthLavaForProcess(){
        return LAVA_TANK.getFluidInTank(0).getAmount() >= 100;
    }

    private final FluidTank LAVA_TANK = new FluidTank(8000, f->f.getFluid() == Fluids.LAVA){
        @Override
        public void onContentsChanged() {
            setChanged();
        }

    };

    @Override
    public void setChanged() {
        super.setChanged();
        if(!level.isClientSide()) {
            ModMessages.sendToClients(new FluidSyncS2CPacket(getFluidStack(), worldPosition));
        }
    }

    public void setFluid(FluidStack stack) {
        this.LAVA_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.LAVA_TANK.getFluid();
    }
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyItemHandler.cast() : (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? lazyFluidHandler.cast() : super.getCapability(cap,side));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> LAVA_TANK);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        LAVA_TANK.readFromNBT(tag.getCompound("lavatank"));
        this.setGeneratorType(GeneratorType.valueOf(tag.getString("generatorType")));
    }

    @Override
    public boolean generateItem() {
        if (ItemGessence.isElectronicGessence(this.itemHandler.getStackInSlot(2).getItem())){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslatableComponent("tooltip.techresourcesgenerator.dontaccept").withStyle(ChatFormatting.RED),worldPosition));
            return false;
        }
        if (!this.hasGessence()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslatableComponent("tooltip.techresourcesgenerator.no_gessence").withStyle(ChatFormatting.RED),worldPosition));
            return false;
        } else if (getGessence().getMinimumGenerator().getOrder() > getGeneratorType().getOrder()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslatableComponent("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(ChatFormatting.RED),worldPosition));
            return false;
        }
        if (itemHandler.getStackInSlot(3).isEmpty()) {
            itemHandler.setStackInSlot(3, new ItemStack(this.getGessence().getItem(), this.getItemsDropped()));
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TextComponent(""),worldPosition));
            return true;
        } else if (itemHandler.getStackInSlot(3).is(this.getGessence().getItem())) {
            if (itemHandler.getStackInSlot(3).getMaxStackSize() >= (itemHandler.getStackInSlot(3).getCount() + getItemsDropped())) {
                itemHandler.setStackInSlot(3, new ItemStack(this.getGessence().getItem(), itemHandler.getStackInSlot(3).getCount() + this.getItemsDropped()));
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TextComponent(""),worldPosition));
                return true;
            } else {
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslatableComponent("tooltip.techresourcesgenerator.output_slot_full").withStyle(ChatFormatting.RED),worldPosition));
                return false;
            }
        } else
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslatableComponent("tooltip.techresourcesgenerator.output_slot_full").withStyle(ChatFormatting.RED),worldPosition));
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        CompoundTag tank = new CompoundTag();
        LAVA_TANK.writeToNBT(tank);
        tag.put("lavatank",tank);
        tag.putString("generatorType",getGeneratorType().name());
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }



    @Override
    public Component getDisplayName() {
        return new TextComponent("Magmatic Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ModMessages.sendToClients(new FluidSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new MagmaticGeneratorMenu(id,inv,this,getGeneratorType());
    }
}
