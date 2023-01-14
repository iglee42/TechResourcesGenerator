package fr.iglee42.techresourcesgenerator.menu;

import fr.iglee42.techresourcesgenerator.blocks.ModBlocks;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.menu.slots.GessenceSlot;
import fr.iglee42.techresourcesgenerator.menu.slots.OutputSlot;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class ElectricGeneratorMenu extends Container {
    public ElectricGeneratorTile blockEntity;
    private final World level;
    private float delay;
    private ITextComponent errorMessage = new StringTextComponent("");

    private Generator generator;


    public ElectricGeneratorMenu(int id, PlayerInventory inv, TileEntity entity, Generator generator) {
        super(ModMenuTypes.ELECTRIC_GENERATOR_MENU.get(), id);
        blockEntity = (ElectricGeneratorTile) entity;
        this.level = inv.player.level;
        this.generator = generator;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            this.addSlot(new GessenceSlot(handler, 0, 67, 37, blockEntity,true));
            this.addSlot(new OutputSlot(handler, 1, 135, 37));
        });
    }


    public ElectricGeneratorTile getBlockEntity() {
        return this.blockEntity;
    }

    public int getScaledProgress() {
        int progress = (int)getDelay();
        int maxProgress = generator.isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(generator.name())).getDelay() : generator.delay();  // Max Progress 3
        int progressArrowSize = 60; // This is the height in pixels of your arrow

        return maxProgress != 0 ? progress * (progressArrowSize / maxProgress) : 0;
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        for (Block block : ModBlocks.getAllGeneratorForType("electric")) {
            if (stillValid(IWorldPosCallable.create(level,blockEntity.getBlockPos()),player,block)) return true;
        }
        return false;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    public World getLevel() {
        return level;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getDelay() {
        return delay;
    }

    public void setErrorMessage(ITextComponent message) {
        this.errorMessage = message;
    }

    public ITextComponent getErrorMessage() {
        return errorMessage;
    }

    public Generator getGeneratorType() {
        return generator;
    }

    public void setGeneratorType(Generator type) {
        this.generator = type;
    }
}
