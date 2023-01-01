package fr.iglee42.techresourcesgenerator.client.renderer.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.render.EnergyInfoArea;
import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorDelaySyncC2SPacket;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class ElectricGeneratorScreen extends AbstractContainerScreen<ElectricGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/electric_generator_gui.png");

    private EnergyInfoArea renderer;

    public ElectricGeneratorScreen(ElectricGeneratorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyRenderer();
    }


    private void assignEnergyRenderer() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderer = new EnergyInfoArea( x + 10, y+14,menu.blockEntity.getEnergyStorage(),16,62);
    }


    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 10, 14,16,62)) {
            renderTooltip(pPoseStack, renderer.getTooltips(),
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

        renderer.draw(pPoseStack);
        renderProgressArrow(pPoseStack,x,y);

    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));

        drawCenteredString(pPoseStack,font,menu.getErrorMessage(),x + 110 , y +20,ChatFormatting.RED.getColor());
    }


    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.getDelay() <= ConfigsForType.getConfigForType(menu.blockEntity.getGeneratorType()).getDelay()) {
            blit(pPoseStack, x + 87 , y + 41, 176, 0, menu.getScaledProgress(), 8);
        }
    }
    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }


}
