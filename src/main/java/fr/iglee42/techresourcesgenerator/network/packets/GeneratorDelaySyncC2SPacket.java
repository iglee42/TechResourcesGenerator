package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.GeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class GeneratorDelaySyncC2SPacket {

    private final BlockPos pos;

    public GeneratorDelaySyncC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public GeneratorDelaySyncC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if (ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getBlockEntity(pos) instanceof MagmaticGeneratorTile te){
                    ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(te.getDelay(),pos));
                } else if (ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getBlockEntity(pos) instanceof ElectricGeneratorTile te){
                    ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(te.getProgress(),pos));
                }
        });
        return true;
    }
}
