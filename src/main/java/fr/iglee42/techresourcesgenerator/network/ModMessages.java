package fr.iglee42.techresourcesgenerator.network;

import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TechResourcesGenerator.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidSyncS2CPacket::new)
                .encoder(FluidSyncS2CPacket::toBytes)
                .consumer(FluidSyncS2CPacket::handle)
                .add();
        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumer(EnergySyncS2CPacket::handle)
                .add();
        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumer(ItemStackSyncS2CPacket::handle)
                .add();
        net.messageBuilder(CardInfuserProgressSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CardInfuserProgressSyncS2CPacket::new)
                .encoder(CardInfuserProgressSyncS2CPacket::toBytes)
                .consumer(CardInfuserProgressSyncS2CPacket::handle)
                .add();
        net.messageBuilder(GeneratorTypeSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratorTypeSyncS2C::new)
                .encoder(GeneratorTypeSyncS2C::toBytes)
                .consumer(GeneratorTypeSyncS2C::handle)
                .add();
        net.messageBuilder(GeneratorDelaySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratorDelaySyncS2CPacket::new)
                .encoder(GeneratorDelaySyncS2CPacket::toBytes)
                .consumer(GeneratorDelaySyncS2CPacket::handle)
                .add();
        net.messageBuilder(GeneratorDelaySyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GeneratorDelaySyncC2SPacket::new)
                .encoder(GeneratorDelaySyncC2SPacket::toBytes)
                .consumer(GeneratorDelaySyncC2SPacket::handle)
                .add();
        net.messageBuilder(GeneratorGenerateActionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GeneratorGenerateActionC2SPacket::new)
                .encoder(GeneratorGenerateActionC2SPacket::toBytes)
                .consumer(GeneratorGenerateActionC2SPacket::handle)
                .add();
        net.messageBuilder(GeneratorGenerateReturnS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratorGenerateReturnS2CPacket::new)
                .encoder(GeneratorGenerateReturnS2CPacket::toBytes)
                .consumer(GeneratorGenerateReturnS2CPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
