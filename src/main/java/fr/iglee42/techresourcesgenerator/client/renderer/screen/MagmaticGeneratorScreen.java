package fr.iglee42.techresourcesgenerator.client.renderer.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.buttons.LittleButton;
import fr.iglee42.techresourcesgenerator.client.renderer.screen.render.FluidTankRenderer;
import fr.iglee42.techresourcesgenerator.menu.MagmaticGeneratorMenu;
import fr.iglee42.techresourcesgenerator.network.ModMessages;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorDelaySyncC2SPacket;
import fr.iglee42.techresourcesgenerator.network.packets.GeneratorGenerateActionC2SPacket;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class MagmaticGeneratorScreen extends ContainerScreen<MagmaticGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TechResourcesGenerator.MODID,"textures/gui/magmatic_generator_gui.png");

    private FluidTankRenderer renderer;
    private Button b;
    public MagmaticGeneratorScreen(MagmaticGeneratorMenu menu, PlayerInventory inventory, ITextComponent component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        //Create Little Button for the generate button
        b = this.addWidget(new LittleButton(x + 80, y + 60, 60, new StringTextComponent("Generate"), b -> {
            MagmaticGeneratorTile tile = menu.getBlockEntity();
            ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(tile.getBlockPos()));
            if (menu.getDelay() == 0){
                ModMessages.sendToServer(new GeneratorGenerateActionC2SPacket(tile.getBlockPos()));
            }
        }, (b, poseStack, mouseX, mouseY) -> {
            ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));
            drawCenteredString(poseStack,font,"Delay : " + menu.getDelay(),b.x + 30, b.y + 15, TextFormatting.GOLD.getColor());
        }));
        assignFluidRenderer();
    }


    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(8000, true, 16, 61);
    }


    @Override
    protected void renderLabels(MatrixStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x - 30, y);
    }

    private void renderFluidAreaTooltips(MatrixStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 40, 15) && !renderer.getTooltip(menu.getFluidStack(), ITooltipFlag.TooltipFlags.NORMAL).isEmpty()) {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), ITooltipFlag.TooltipFlags.NORMAL).get(0), pMouseX - x - 30, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(MatrixStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderer.render(pPoseStack, x + 10, y + 15, menu.getFluidStack());
    }

    @Override
    public void render(MatrixStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        ModMessages.sendToServer(new GeneratorDelaySyncC2SPacket(menu.blockEntity.getBlockPos()));
        b.active = menu.getDelay() == 0;
        this.b.render(pPoseStack,mouseX,mouseY,delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (menu.getErrorMessage().getString().equals(new TranslationTextComponent("tooltip.techresourcesgenerator.dontaccept").getString())){
            drawCenteredString(pPoseStack,font,menu.getErrorMessage(),x + 110 , y +15,TextFormatting.RED.getColor());
            drawCenteredString(pPoseStack,font,new TranslationTextComponent("tooltip.techresourcesgenerator.electronicgessence"),x + 110 , y +25,TextFormatting.RED.getColor());
        } else {
            drawCenteredString(pPoseStack,font,menu.getErrorMessage(),x + 110 , y +20,TextFormatting.RED.getColor());
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }


}
