package fr.iglee42.techresourcesgenerator.client.renderer.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorDelaySyncC2SPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorGenerateActionC2SPacket;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.buttons.LittleButton;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.render.FluidTankRenderer;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public class MagmaticGeneratorScreen extends AbstractContainerScreen<MagmaticGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/magmatic_generator_gui.png");

    private FluidTankRenderer renderer;
    private Button b;
    public MagmaticGeneratorScreen(MagmaticGeneratorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        //Create Little Button for the generate button
        b = this.addRenderableWidget(new LittleButton(x + 80, y + 60, 60, Component.literal("Generate"), b -> {
            MagmaticGeneratorTile tile = menu.getBlockEntity();
            ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(tile.getBlockPos()));
            if (menu.getDelay() == 0){
                ModMessages.sendToServer(new GeneratorGenerateActionC2SPacket(tile.getBlockPos()));
            }
        }, (b, poseStack, mouseX, mouseY) -> {
            ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));
            drawCenteredString(poseStack,font,"Delay : " + menu.getDelay(),b.x + 30, b.y + 15, ChatFormatting.GOLD.getColor());
        }));
        assignFluidRenderer();
    }


    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(8000, true, 16, 61);
    }


    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x - 30, y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 40, 15)) {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x - 30, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderer.render(pPoseStack, x + 10, y + 15, menu.getFluidStack());
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));
        b.active = menu.getDelay() == 0;
        this.b.render(pPoseStack,mouseX,mouseY,delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (menu.getErrorMessage().getString().equals(Component.translatable("tooltip.techresourcesgenerator.dontaccept").getString())){
            drawCenteredString(pPoseStack,font,menu.getErrorMessage(),x + 110 , y +15,ChatFormatting.RED.getColor());
            drawCenteredString(pPoseStack,font,Component.translatable("tooltip.techresourcesgenerator.electronicgessence"),x + 110 , y +25,ChatFormatting.RED.getColor());
        } else {
            drawCenteredString(pPoseStack,font,menu.getErrorMessage(),x + 110 , y +20,ChatFormatting.RED.getColor());
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }


}
