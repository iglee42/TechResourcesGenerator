package fr.iglee42.techresourcesgenerator.tiles.generator;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.FluidSyncS2CPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorGenerateReturnS2CPacket;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MagmaticGeneratorTile extends GeneratorTile implements INamedContainerProvider {

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

    public MagmaticGeneratorTile(GeneratorType generatorType) {
        super(ModBlockEntities.MAGMATIC_GENERATOR.get(),generatorType);
    }
    public MagmaticGeneratorTile(){
        this(GeneratorType.IRON);
    }

    @Override
    public boolean hasGessence() {
        return super.hasGessence();
    }

    @Override
    protected void second(World level, BlockPos pos, BlockState state, GeneratorTile tile) {
        this.setGessence(GessenceType.getByItemCanBeNull(itemHandler.getStackInSlot(2).getItem()));
        if (LAVA_TANK.getFluidInTank(0).getAmount() <= 7000){
            if (level.getFluidState(pos.offset(0,1,0)) == Fluids.LAVA.defaultFluidState()){
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
        if (slot1.getItem() == Items.BUCKET){
            if (slot2.isEmpty()){
                if (getFluidStack().getAmount() >= 1000){
                    setFluid(new FluidStack(Fluids.LAVA,LAVA_TANK.getFluidInTank(0).getAmount() - 1000));
                    setChanged();
                    itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET,slot1.getCount() - 1));
                    itemHandler.setStackInSlot(1,new ItemStack(Items.LAVA_BUCKET));
                }
            }
        } else if (slot1.getItem() == Items.LAVA_BUCKET){
            if (slot2.isEmpty() || slot2.getItem() == Items.BUCKET){
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

 

    @Nonnull
    @Override
    public  <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
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
    public void load(BlockState state ,CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        LAVA_TANK.readFromNBT(tag.getCompound("lavatank"));
        this.setGeneratorType(GeneratorType.valueOf(tag.getString("generatorType")));
        super.load(state,tag);
    }

    @Override
    public boolean generateItem() {
        if (ItemGessence.isElectronicGessence(this.itemHandler.getStackInSlot(2).getItem())){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.dontaccept").withStyle(TextFormatting.RED),worldPosition));
            return false;
        }
        if (!this.hasGessence()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.no_gessence").withStyle(TextFormatting.RED),worldPosition));
            return false;
        } else if (getGessence().getMinimumGenerator().getOrder() > getGeneratorType().getOrder()){
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new TranslationTextComponent("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(TextFormatting.RED),worldPosition));
            return false;
        }
        if (itemHandler.getStackInSlot(3).isEmpty()) {
            itemHandler.setStackInSlot(3, new ItemStack(this.getGessence().getItem(), this.getItemsDropped()));
            ModMessages.sendToClients(new GeneratorGenerateReturnS2CPacket(new StringTextComponent(""),worldPosition));
            return true;
        } else if (itemHandler.getStackInSlot(3).getItem() == this.getGessence().getItem()) {
            if (itemHandler.getStackInSlot(3).getMaxStackSize() >= (itemHandler.getStackInSlot(3).getCount() + getItemsDropped())) {
                itemHandler.setStackInSlot(3, new ItemStack(this.getGessence().getItem(), itemHandler.getStackInSlot(3).getCount() + this.getItemsDropped()));
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
        CompoundNBT tank = new CompoundNBT();
        LAVA_TANK.writeToNBT(tank);
        tag.put("lavatank",tank);
        tag.putString("generatorType",getGeneratorType().name());
        return super.save(tag);
    }
    public void drops() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemEntity item = new ItemEntity(this.level,worldPosition.getX() + 0.5,worldPosition.getY()+ 1,worldPosition.getZ() + 0.5,itemHandler.getStackInSlot(i));
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
        }


    }



    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        ModMessages.sendToClients(new FluidSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new MagmaticGeneratorMenu(id,inv,this,getGeneratorType());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Magmatic Generator");
    }
}
