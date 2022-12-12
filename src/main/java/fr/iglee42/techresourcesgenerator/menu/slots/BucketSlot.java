package fr.iglee42.techresourcesgenerator.menu.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BucketSlot extends SlotItemHandler {
    public BucketSlot(IItemHandler handler, int id, int x, int y) {
        super(handler, id, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack it) {
        return it.is(Items.BUCKET) || it.is(Items.LAVA_BUCKET);
    }

    @Override
    public boolean mayPickup(Player player) {
        return this.hasItem() && (this.getItem().is(Items.BUCKET) || this.getItem().is(Items.LAVA_BUCKET));
    }
}
