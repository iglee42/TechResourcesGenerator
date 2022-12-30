package fr.iglee42.techresourcesgenerator.client.renderer.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import fr.iglee42.techresourcesgenerator.blocks.BlockCardInfuser;
import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.concurrent.atomic.AtomicReference;

public class CardInfuserRenderer implements BlockEntityRenderer<CardInfuserTile> {

    public CardInfuserRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CardInfuserTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        AtomicReference<ItemStack> gessence = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> gessence.set(h.getStackInSlot(0)));

        if (gessence.get() != null && !gessence.get().isEmpty() && tile.getProgress() < 6) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.88D, 0.5D);
            float scale = gessence.get().getItem() instanceof BlockItem ? 0.95F : 0.75F;
            poseStack.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            Minecraft.getInstance().getItemRenderer().renderStatic(gessence.get(), ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, poseStack, buffer, 0);
            poseStack.popPose();
        }
        AtomicReference<ItemStack> card = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> card.set(h.getStackInSlot(1)));
        if (card.get() != null  && !card.get().isEmpty()){
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.47D, 0.5D);
            poseStack.scale(0.5f,0.5f,0.5f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            switch (tile.getBlockState().getValue(BlockCardInfuser.FACING).getOpposite()) {
                case NORTH -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(0));
                case SOUTH -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                case EAST -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(-90));
                case WEST -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
                default -> {
                }
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(card.get(), ItemTransforms.TransformType.GUI,combinedLight,combinedOverlay,poseStack,buffer,0);
            poseStack.popPose();
        }
    }
}
