package fr.iglee42.techresourcesgenerator.client.renderer.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.render.EnergyInfoArea;
import fr.iglee42.techresourcesgenerator.menu.ElectricGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorDelaySyncC2SPacket;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.MouseUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ElectricGeneratorScreen extends ContainerScreen<ElectricGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/electric_generator_gui.png");

    private EnergyInfoArea renderer;

    public ElectricGeneratorScreen(ElectricGeneratorMenu menu, PlayerInventory inventory, ITextComponent component) {
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
    protected void renderLabels(MatrixStack pMatrixStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pMatrixStack, pMouseX, pMouseY, x, y);
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        minecraft.getTextureManager().bind(TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pMatrixStack, x, y, 0, 0, imageWidth, imageHeight);

        renderer.draw(pMatrixStack);
        renderProgressArrow(pMatrixStack,x,y);
    }

    private void renderEnergyAreaTooltips(MatrixStack pMatrixStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 10, 14,16,62)) {
            renderTooltip(pMatrixStack, renderer.getTooltips().get(0), pMouseX - x - 30, pMouseY - y);

        }
    }



    @Override
    public void render(MatrixStack pMatrixStack, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(pMatrixStack);
        super.render(pMatrixStack, mouseX, mouseY, delta);
        renderTooltip(pMatrixStack, mouseX, mouseY);
        ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));

        drawCenteredString(pMatrixStack,font,menu.getErrorMessage(),x + 110 , y +20, TextFormatting.RED.getColor());
    }


    private void renderProgressArrow(MatrixStack pMatrixStack, int x, int y) {
        if(menu.getDelay() <= ConfigsForType.getConfigForType(menu.blockEntity.getGeneratorType()).getDelay()) {
            blit(pMatrixStack, x + 87 , y + 41, 176, 0, menu.getScaledProgress(), 8);
        }
    }
    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }


}
