package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.EnergySyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.FluidSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorGenerateReturnS2CPacket;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import fr.iglee42.techresourcesgenerator.utils.ModEnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricGeneratorTile extends GeneratorTile implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ElectricGeneratorTile(BlockState state, BlockPos pos, GeneratorType generatorType) {
        super(ModBlockEntities.ELECTRIC_GENERATOR.get(), state, pos,generatorType);
    }
    public ElectricGeneratorTile(BlockPos pos, BlockState state) {
        this( state, pos,GeneratorType.IRON);
    }

    @Override
    public boolean hasGessence() {
        return super.hasGessence();
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, GeneratorTile tile) {
        this.setGessence(GessenceType.getByItem(itemHandler.getStackInSlot(0).getItem()));
        if (hasEnoughtEnergyForAllProcess() && getDelay() > 0 && !isEnabled()){
            this.enabled = true;
        }
        if (isEnabled()){
            setDelay(getDelay() - 1);
            getEnergyStorage().extractEnergy(1,false);
            setChanged();
        }
    }

    private boolean hasEnoughtEnergyForAllProcess() {
        return true;
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? lazyItemHandler.cast() : (cap == ForgeCapabilities.FLUID_HANDLER ? lazyEnergyHandler.cast() : super.getCapability(cap));
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
        this.setGeneratorType(GeneratorType.valueOf(tag.getString("generatorType")));
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
        return Component.literal("Electric Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ModMessages.sendToClients(new EnergySyncS2CPacket(getEnergyStorage().getEnergyStored(), worldPosition));
        return new ElectricGeneratorMenu(id,inv,this,getGeneratorType());
    }
}
