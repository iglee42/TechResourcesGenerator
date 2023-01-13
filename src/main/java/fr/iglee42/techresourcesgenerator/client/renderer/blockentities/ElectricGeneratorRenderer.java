package fr.iglee42.techresourcesgenerator.client.renderer.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.concurrent.atomic.AtomicReference;

public class ElectricGeneratorRenderer implements BlockEntityRenderer<ElectricGeneratorTile> {

    public ElectricGeneratorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ElectricGeneratorTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        AtomicReference<ItemStack> stack = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(ForgeCapabilities.ITEM_HANDLER,null).ifPresent(h-> stack.set(h.getStackInSlot(0)));
        if (tile.getGessence() != null && !stack.get().isEmpty()){
            poseStack.pushPose();
            poseStack.translate(0.5,1.13,0.53);
            poseStack.scale(0.5f,0.5f,0.5f);
            itemRenderer.renderStatic(stack.get(), ItemTransforms.TransformType.GUI,getLightLevel(tile.getLevel(),tile.getBlockPos()), OverlayTexture.NO_OVERLAY,poseStack,buffer,1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
