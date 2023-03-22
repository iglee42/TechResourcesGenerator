package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.*;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import fr.iglee42.techresourcesgenerator.utils.ModEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectricGeneratorTile extends GeneratorTile implements INamedContainerProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public @Nonnull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return slot == 1 ? stack : (slot==0 && Gessence.isGeneratorValidForGessence(stack,getGeneratorType()) ? super.insertItem(slot,stack,simulate) : stack);
        }

    };

    private int progress = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ElectricGeneratorTile( Generator generatorType) {
        super(ModBlockEntities.ELECTRIC_GENERATOR.get(),generatorType);
    }
    public ElectricGeneratorTile(){
        this(Generator.getByName("modium"));
    }

    @Override
    public boolean hasGessence() {
        return super.hasGessence();
    }

    @Override
    protected void second(World level, BlockPos pos, BlockState state, GeneratorTile tile) {
        if (!level.isClientSide()) {
            ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(getProgress(), pos));
            ModMessages.sendToClients(new GeneratorTypeSyncS2C(getGeneratorType(), pos));
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
        if (level.getBlockEntity(pos.above()) instanceof SignTileEntity && itemHandler.getStackInSlot(0).getItem() == ModItem.getGessenceCard(Gessence.getByName("blazum"))){
            ((SignTileEntity)level.getBlockEntity(pos.above())).setMessage(1,new StringTextComponent("Code Lyoko"));
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
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyItemHandler.cast() : (cap == CapabilityEnergy.ENERGY ? lazyEnergyHandler.cast() : super.getCapability(cap,side));
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
    public void load(BlockState state,CompoundNBT tag) {
        
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt("energy"));
        this.setGeneratorType(Generator.getByName(tag.getString("generatorType")));
        progress = tag.getInt("progress");
        super.load(state,tag);
    }

    @Override
    public boolean generateItem() {
        if (!this.hasGessence()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.no_gessence").withStyle(TextFormatting.RED),worldPosition));
            return false;
        } else if (getGessence().getMinimumGenerator().getOrder() > getGeneratorType().getOrder()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(TextFormatting.RED),worldPosition));
            return false;
        }
        if (itemHandler.getStackInSlot(1).isEmpty()) {
            itemHandler.setStackInSlot(1, new ItemStack(this.getGessence().getItem(), this.getItemsDropped()));
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new StringTextComponent(""),worldPosition));
            return true;
        } else if (itemHandler.getStackInSlot(1).getItem() == this.getGessence().getItem()) {
            if (itemHandler.getStackInSlot(1).getMaxStackSize() >= (itemHandler.getStackInSlot(1).getCount() + getItemsDropped())) {
                itemHandler.setStackInSlot(1, new ItemStack(this.getGessence().getItem(), itemHandler.getStackInSlot(1).getCount() + this.getItemsDropped()));
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new StringTextComponent(""),worldPosition));
                return true;
            } else {
                ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.output_slot_full").withStyle(TextFormatting.RED),worldPosition));
                return false;
            }
        } else
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.output_slot_full").withStyle(TextFormatting.RED),worldPosition));
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("energy",ENERGY_STORAGE.getEnergyStored());
        tag.putString("generatorType",getGeneratorType().name());
        tag.putInt("progress",progress);
        return super.save(tag);
    }
    public void drops() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemEntity item = new ItemEntity(this.level,worldPosition.getX() + 0.5,worldPosition.getY()+ 1,worldPosition.getZ() + 0.5,itemHandler.getStackInSlot(i));
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
        }
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Electric Generator");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
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
