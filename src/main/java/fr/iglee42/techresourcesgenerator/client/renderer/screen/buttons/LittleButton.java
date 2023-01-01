package fr.iglee42.techresourcesgenerator.client.renderer.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class LittleButton extends Button {
    public LittleButton(int x, int y, int width, ITextComponent text, IPressable onPress, ITooltip onTooltip) {
        super(x,y,width,12,text,onPress,onTooltip);
    }

    @Override
    public void renderButton(MatrixStack _poseStack, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer font = minecraft.font;
        minecraft.getTextureManager().bind(new ResourceLocation(TechResourcesGenerator.MODID, "textures/gui/little_button.png"));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(_poseStack, this.x, this.y, 0, i * 12, this.width / 2, this.height,200,36);
        this.blit(_poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2,  i * 12, this.width / 2, this.height,200,36);
        this.renderBg(_poseStack, minecraft, mouseX, mouseY);
        int j = getFGColor();
        drawCenteredString(_poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        boolean isHovered = this.isHovered();
        if (isHovered) this.renderToolTip(_poseStack, mouseX, mouseY);
    }
}
