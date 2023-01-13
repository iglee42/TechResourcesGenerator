package fr.iglee42.techresourcesgenerator.network.packets;

import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorTypeSyncS2C {

    private final Generator type;
    private final BlockPos pos;

    public GeneratorTypeSyncS2C(FriendlyByteBuf buf){
        this.type = Generator.getByOrder(buf.readInt());
        this.pos = buf.readBlockPos();
    }

    public GeneratorTypeSyncS2C(Generator type, BlockPos worldPosition) {
        this.type = type;
        this.pos = worldPosition;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(type.getOrder());
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ElectricGeneratorTile blockEntity) {
                if(Minecraft.getInstance().player.containerMenu instanceof ElectricGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setGeneratorType(type);
                }
            }
        });
        return true;
    }
}
