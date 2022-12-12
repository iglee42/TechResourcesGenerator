package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorGenerateReturnS2CPacket {
    private final Component message;
    private final BlockPos pos;

    public GeneratorGenerateReturnS2CPacket(Component message, BlockPos pos) {
        this.message = message;
        this.pos = pos;
    }

    public GeneratorGenerateReturnS2CPacket(FriendlyByteBuf buf) {
        this.message = buf.readComponent();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(message);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if(Minecraft.getInstance().player.containerMenu instanceof MagmaticGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setErrorMessage(message);
                }
        });
        return true;
    }
}
