package fr.iglee42.techresourcesgenerator.menu.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class OutputSlot extends SlotItemHandler {
    public OutputSlot(IItemHandler handler, int id, int x, int y) {
        super(handler,id,x,y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
