package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private final ItemStackHandler itemStackHandler;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(ItemStackHandler itemStackHandler, BlockPos pos) {
        this.itemStackHandler = itemStackHandler;
        this.pos = pos;
    }

    public ItemStackSyncS2CPacket(PacketBuffer buf) {
        int size = buf.readInt();
        itemStackHandler = new ItemStackHandler(size);
        for (int i = 0; i < size; i++) {
            itemStackHandler.insertItem(i, buf.readItem(), false);
        }

        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i++) {
            buf.writeItem(itemStackHandler.getStackInSlot(i));
        }

        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ElectricGeneratorTile) {
                ElectricGeneratorTile tile = (ElectricGeneratorTile) Minecraft.getInstance().level.getBlockEntity(pos);
                tile.setHandler(this.itemStackHandler);
            }
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof CardInfuserTile) {
                CardInfuserTile tile = (CardInfuserTile) Minecraft.getInstance().level.getBlockEntity(pos);
                tile.setHandler(this.itemStackHandler);
            }
        });
        return true;
    }
}