package fr.iglee42.techresourcesgenerator.menu.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BucketSlot extends SlotItemHandler {
    public BucketSlot(IItemHandler handler, int id, int x, int y) {
        super(handler, id, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack it) {
        return it.getItem() == Items.BUCKET || it.getItem() == Items.LAVA_BUCKET;
    }

    @Override
    public boolean mayPickup(PlayerEntity player) {
        return this.hasItem() && (getItem().getItem() == Items.BUCKET || getItem().getItem() == Items.LAVA_BUCKET);
    }
}
