package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidSyncS2CPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket(PacketBuffer buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof MagmaticGeneratorTile) {
                MagmaticGeneratorTile te = (MagmaticGeneratorTile) Minecraft.getInstance().level.getBlockEntity(pos);
                te.setFluid(this.fluidStack);

                if(Minecraft.getInstance().player.containerMenu instanceof MagmaticGeneratorMenu) {
                    MagmaticGeneratorMenu menu = (MagmaticGeneratorMenu) Minecraft.getInstance().player.containerMenu;

                    menu.setFluid(this.fluidStack);
                }
            }
        });
        return true;
    }
}