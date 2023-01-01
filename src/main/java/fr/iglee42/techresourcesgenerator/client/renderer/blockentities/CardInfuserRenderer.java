package fr.iglee42.techresourcesgenerator.client.renderer.blockentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.iglee42.techresourcesgenerator.blocks.BlockCardInfuser;
import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.concurrent.atomic.AtomicReference;

public class CardInfuserRenderer extends TileEntityRenderer<CardInfuserTile> {

    public CardInfuserRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CardInfuserTile tile, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        AtomicReference<ItemStack> gessence = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> gessence.set(h.getStackInSlot(0)));

        if (gessence.get() != null && !gessence.get().isEmpty() && tile.getProgress() < 6) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.88D, 0.5D);
            float scale = gessence.get().getItem() instanceof BlockItem ? 0.95F : 0.75F;
            poseStack.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            Minecraft.getInstance().getItemRenderer().renderStatic(gessence.get(), ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, poseStack, buffer);
            poseStack.popPose();
        }
        AtomicReference<ItemStack> card = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> card.set(h.getStackInSlot(1)));
        if (card.get() != null  && !card.get().isEmpty()){
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.47D, 0.5D);
            poseStack.scale(0.5f,0.5f,0.5f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            switch (tile.getBlockState().getValue(BlockCardInfuser.FACING).getOpposite()) {
                case NORTH : poseStack.mulPose(Vector3f.ZP.rotationDegrees(0)); break;
                case SOUTH : poseStack.mulPose(Vector3f.ZP.rotationDegrees(180)); break;
                case EAST : poseStack.mulPose(Vector3f.ZP.rotationDegrees(-90)); break;
                case WEST : poseStack.mulPose(Vector3f.ZP.rotationDegrees(90)); break;
                default : break;
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(card.get(), ItemCameraTransforms.TransformType.GUI,combinedLight,combinedOverlay,poseStack,buffer);
            poseStack.popPose();
        }
    }
}
