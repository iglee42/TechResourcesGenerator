package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergySyncS2CPacket {
    private final int energy;
    private final BlockPos pos;

    public EnergySyncS2CPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncS2CPacket(PacketBuffer buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ElectricGeneratorTile) {
                ElectricGeneratorTile te = (ElectricGeneratorTile) Minecraft.getInstance().level.getBlockEntity(pos);
                te.setEnergyLevel(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof ElectricGeneratorMenu  ) {
                    ElectricGeneratorMenu menu = (ElectricGeneratorMenu) Minecraft.getInstance().player.containerMenu;
                    te.setEnergyLevel(energy);
                }
            }
        });
        return true;
    }
}