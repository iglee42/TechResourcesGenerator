package fr.iglee42.techresourcesgenerator.menu.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class OutputSlot extends SlotItemHandler {
    public OutputSlot(IItemHandler handler, int id, int x, int y) {
        super(handler,id,x,y);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }
}
