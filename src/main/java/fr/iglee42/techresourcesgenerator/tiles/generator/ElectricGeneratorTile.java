package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.*;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricGeneratorTile extends GeneratorTile implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot == 1 ? stack : (slot==0 && Gessence.isGeneratorValidForGessence(stack,getGeneratorType()) ? super.insertItem(slot,stack,simulate) : stack);
        }

    };


    private int progress = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ElectricGeneratorTile(BlockState state, BlockPos pos, Generator generatorType) {
        super(ModBlockEntities.ELECTRIC_GENERATOR.get(), state, pos,generatorType);
    }
    public ElectricGeneratorTile(BlockPos pos, BlockState state) {
        this( state, pos,Generator.getByName("modium"));
    }

    @Override
    public boolean hasGessence() {
        return super.hasGessence();
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, GeneratorTile tile) {
        if (!level.isClientSide()){
            ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(getProgress(),pos));
            ModMessages.sendToClients(new GeneratorTypeSyncS2C(getGeneratorType(),pos));
            this.setGessence(Gessence.getByItemCanBeNull(itemHandler.getStackInSlot(0).getItem()));
            int consumed = getGeneratorType().isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(getGeneratorType().name())).getConsumeFE() : getGeneratorType().consumed();
            if (!this.enabled){
                this.enabled = hasEnoughtEnergyForAllProcess() && getDelay() > 0;
            }
            if (getDelay() == 0 && progress == getDelayBetweenItem()){
                this.enabled = false;
                if (generateItem()){
                    progress = 0;
                    resetDelay();
                }
            }
            if (isEnabled() && getEnergyStorage().extractEnergy(consumed,true) > 0){
                setDelay(getDelay() - 1);
                progress++;
                getEnergyStorage().extractEnergy(consumed,false);
                setChanged();

            }
        }
        if (level.getBlockEntity(pos.above()) instanceof SignBlockEntity sign && itemHandler.getStackInSlot(0).is(ModItem.getGessenceCard(Gessence.getByName("blazum")))){
            sign.setMessage(1,Component.literal("Code Lyoko"));
        }


    }

    private boolean hasEnoughtEnergyForAllProcess() {
        return (getGeneratorType().isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(getGeneratorType().name())).getConsumeFE() : getGeneratorType().consumed() * getDelayBetweenItem()) <= getEnergyStorage().getEnergyStored();
    }


    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(150000,75000){


        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    @Override
    public void setChanged() {
        super.setChanged();
        if(!level.isClientSide()) {
            ModMessages.sendToClients(new EnergySyncS2CPacket(getEnergyStorage().getEnergyStored(), worldPosition));
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(itemHandler, worldPosition));
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? lazyItemHandler.cast() : (cap == ForgeCapabilities.ENERGY ? lazyEnergyHandler.cast() : super.getCapability(cap,side));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt("energy"));
        this.setGeneratorType(Generator.getByName(tag.getString("generatorType")));
        progress = tag.getInt("progress");
    }

    @Override
    public boolean generateItem() {
        if (!this.hasGessence()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.translatable("tooltip.techresourcesgenerator.no_gessence").withStyle(ChatFormatting.RED),worldPosition));
            return false;
        } else if (getGessence().getMinimumGenerator().getOrder() > getGeneratorType().getOrder()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.translatable("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(ChatFormatting.RED),worldPosition));
            return false;
        }
        if (itemHandler.getStackInSlot(1).isEmpty()) {
            itemHandler.setStackInSlot(1, new ItemStack(this.getGessence().getItem(), this.getItemsDropped()));
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.empty(),worldPosition));
            return true;
        } else if (itemHandler.getStackInSlot(1).is(this.getGessence().getItem())) {
            if (itemHandler.getStackInSlot(1).getMaxStackSize() >= (itemHandler.getStackInSlot(1).getCount() + getItemsDropped())) {
                itemHandler.setStackInSlot(1, new ItemStack(this.getGessence().getItem(), itemHandler.getStackInSlot(1).getCount() + this.getItemsDropped()));
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.empty(),worldPosition));
                return true;
            } else {
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.translatable("tooltip.techresourcesgenerator.output_slot_full").withStyle(ChatFormatting.RED),worldPosition));
                return false;
            }
        } else
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(Component.translatable("tooltip.techresourcesgenerator.output_slot_full").withStyle(ChatFormatting.RED),worldPosition));
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("energy",ENERGY_STORAGE.getEnergyStored());
        tag.putString("generatorType",getGeneratorType().name());
        tag.putInt("progress",progress);
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Electric Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ModMessages.sendToClients(new EnergySyncS2CPacket(getEnergyStorage().getEnergyStored(), worldPosition));
        ModMessages.sendToClients(new GeneratorTypeSyncS2C(getGeneratorType(), worldPosition));
        return new ElectricGeneratorMenu(id,inv,this,getGeneratorType());
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
}
