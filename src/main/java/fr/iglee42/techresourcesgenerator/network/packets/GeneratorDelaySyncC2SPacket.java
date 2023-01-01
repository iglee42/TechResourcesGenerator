package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class GeneratorDelaySyncC2SPacket {

    private final BlockPos pos;

    public GeneratorDelaySyncC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public GeneratorDelaySyncC2SPacket(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if (ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos) instanceof MagmaticGeneratorTile){
                    MagmaticGeneratorTile te = (MagmaticGeneratorTile) ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos);
                    ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(te.getDelay(),pos));
                } else if (ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos) instanceof ElectricGeneratorTile){
                    ElectricGeneratorTile te = (ElectricGeneratorTile) ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos);
                    ModMessages.sendToClients(new GeneratorDelaySyncS2CPacket(te.getProgress(),pos));
                }
        });
        return true;
    }
}
