package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorGenerateReturnS2CPacket {
    private final ITextComponent message;
    private final BlockPos pos;

    public GeneratorGenerateReturnS2CPacket(ITextComponent message, BlockPos pos) {
        this.message = message;
        this.pos = pos;
    }

    public GeneratorGenerateReturnS2CPacket(PacketBuffer buf) {
        this.message = buf.readComponent();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeComponent(message);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
                if(Minecraft.getInstance().player.containerMenu instanceof MagmaticGeneratorMenu) {
                    ((MagmaticGeneratorMenu)Minecraft.getInstance().player.containerMenu).setErrorMessage(message);
                } else if(Minecraft.getInstance().player.containerMenu instanceof ElectricGeneratorMenu) {
                    ((ElectricGeneratorMenu)Minecraft.getInstance().player.containerMenu).setErrorMessage(message);
                }
        });
        return true;
    }
}
