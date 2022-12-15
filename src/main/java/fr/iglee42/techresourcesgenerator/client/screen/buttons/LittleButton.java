package fr.iglee42.techresourcesgenerator.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LittleButton extends Button {
    public LittleButton(int x, int y, int width, Component text, OnPress onPress, OnTooltip onTooltip) {
        super(x,y,width,12,text,onPress,onTooltip);
    }

    @Override
    public void renderButton(PoseStack _poseStack, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(TechResourcesGenerator.MODID, "textures/gui/little_button.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(_poseStack, this.x, this.y, 0, i * 12, this.width / 2, this.height,200,36);
        this.blit(_poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2,  i * 12, this.width / 2, this.height,200,36);
        this.renderBg(_poseStack, minecraft, mouseX, mouseY);
        int j = getFGColor();
        drawCenteredString(_poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
        boolean isHovered = this.isHoveredOrFocused();
        if (isHovered) this.renderToolTip(_poseStack, mouseX, mouseY);
    }
}
