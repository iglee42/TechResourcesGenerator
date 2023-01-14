package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorDelaySyncS2CPacket {
    private final float delay;
    private final BlockPos pos;

    public GeneratorDelaySyncS2CPacket(float delay, BlockPos pos) {
        this.delay = delay;
        this.pos = pos;
    }

    public GeneratorDelaySyncS2CPacket(PacketBuffer buf) {
        this.delay = buf.readFloat();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeFloat(delay);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if(Minecraft.getInstance().player.containerMenu instanceof MagmaticGeneratorMenu) {
                    ((MagmaticGeneratorMenu)Minecraft.getInstance().player.containerMenu).setDelay(this.delay);
                } else if (Minecraft.getInstance().player.containerMenu instanceof ElectricGeneratorMenu ){
                    ((ElectricGeneratorMenu)Minecraft.getInstance().player.containerMenu).setDelay(this.delay);
                }
        });
        return true;
    }
}
