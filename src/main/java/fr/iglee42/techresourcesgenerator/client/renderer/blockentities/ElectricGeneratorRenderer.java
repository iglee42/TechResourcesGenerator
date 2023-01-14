package fr.iglee42.techresourcesgenerator.client.renderer.blockentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.concurrent.atomic.AtomicReference;

public class ElectricGeneratorRenderer extends TileEntityRenderer<ElectricGeneratorTile> {

    public ElectricGeneratorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ElectricGeneratorTile tile, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        AtomicReference<ItemStack> stack = new AtomicReference<>(ItemStack.EMPTY);
        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).ifPresent(h-> stack.set(h.getStackInSlot(0)));
        if (tile.getGessence() != null && !stack.get().isEmpty()){
            poseStack.pushPose();
            poseStack.translate(0.5,1.13,0.53);
            poseStack.scale(0.5f,0.5f,0.5f);
            itemRenderer.renderStatic(stack.get(), ItemCameraTransforms.TransformType.GUI,getLightLevel(tile.getLevel(),tile.getBlockPos()), OverlayTexture.NO_OVERLAY,poseStack,buffer);
            poseStack.popPose();
        }
    }

    private int getLightLevel(World level, BlockPos pos) {
        int bLight = level.getBrightness(LightType.BLOCK, pos);
        int sLight = level.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
