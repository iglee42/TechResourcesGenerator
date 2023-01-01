package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.tiles.generator.GeneratorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class GeneratorGenerateActionC2SPacket {

    private final BlockPos pos;

    public GeneratorGenerateActionC2SPacket(BlockPos pos) {

        this.pos = pos;
    }

    public GeneratorGenerateActionC2SPacket(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if(ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos) instanceof GeneratorTile) {
                    GeneratorTile te = (GeneratorTile) ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getBlockEntity(pos);
                    if (te.generateItem()) te.resetDelay();
                }
        });
        return true;
    }
}
