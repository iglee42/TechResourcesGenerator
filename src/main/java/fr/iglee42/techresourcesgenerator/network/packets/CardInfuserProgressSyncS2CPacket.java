package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CardInfuserProgressSyncS2CPacket {
    private final int progress;
    private final BlockPos pos;

    public CardInfuserProgressSyncS2CPacket(int progress, BlockPos pos) {
        this.progress = progress;
        this.pos = pos;
    }

    public CardInfuserProgressSyncS2CPacket(PacketBuffer  buf) {
        this.progress = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(progress);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof CardInfuserTile){
                    CardInfuserTile te = (CardInfuserTile) Minecraft.getInstance().level.getBlockEntity(pos);
                    te.setProgress(progress);
                }
        });
        return true;
    }
}
