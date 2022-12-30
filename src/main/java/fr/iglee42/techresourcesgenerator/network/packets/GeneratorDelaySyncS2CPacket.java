package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorDelaySyncS2CPacket {
    private final float delay;
    private final BlockPos pos;

    public GeneratorDelaySyncS2CPacket(float delay, BlockPos pos) {
        this.delay = delay;
        this.pos = pos;
    }

    public GeneratorDelaySyncS2CPacket(FriendlyByteBuf buf) {
        this.delay = buf.readFloat();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(delay);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if(Minecraft.getInstance().player.containerMenu instanceof MagmaticGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setDelay(this.delay);
                } else if (Minecraft.getInstance().player.containerMenu instanceof ElectricGeneratorMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)){
                    menu.setDelay(this.delay);
                }
        });
        return true;
    }
}
