package fr.iglee42.techresourcesgenerator.client.renderer.screen.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.vector.Matrix4f;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */
public abstract class InfoArea {
    protected final Rectangle2d area;

    protected InfoArea(Rectangle2d area) {
        this.area = area;
    }

    public abstract void draw(MatrixStack transform);

    protected void fillGradient(MatrixStack p_93180_, int p_93181_, int p_93182_, int p_93183_, int p_93184_, int p_93185_, int p_93186_) {
        fillGradient(p_93180_, p_93181_, p_93182_, p_93183_, p_93184_, p_93185_, p_93186_, 0);
    }

    protected static void fillGradient(MatrixStack p_168741_, int p_168742_, int p_168743_, int p_168744_, int p_168745_, int p_168746_, int p_168747_, int p_168748_) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(4, DefaultVertexFormats.POSITION_COLOR);
        fillGradient(p_168741_.last().pose(), bufferbuilder, p_168742_, p_168743_, p_168744_, p_168745_, p_168748_, p_168746_, p_168747_);
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    protected static void fillGradient(Matrix4f p_93124_, BufferBuilder p_93125_, int p_93126_, int p_93127_, int p_93128_, int p_93129_, int p_93130_, int p_93131_, int p_93132_) {
        float f = (float)(p_93131_ >> 24 & 255) / 255.0F;
        float f1 = (float)(p_93131_ >> 16 & 255) / 255.0F;
        float f2 = (float)(p_93131_ >> 8 & 255) / 255.0F;
        float f3 = (float)(p_93131_ & 255) / 255.0F;
        float f4 = (float)(p_93132_ >> 24 & 255) / 255.0F;
        float f5 = (float)(p_93132_ >> 16 & 255) / 255.0F;
        float f6 = (float)(p_93132_ >> 8 & 255) / 255.0F;
        float f7 = (float)(p_93132_ & 255) / 255.0F;
        p_93125_.vertex(p_93124_, (float)p_93128_, (float)p_93127_, (float)p_93130_).color(f1, f2, f3, f).endVertex();
        p_93125_.vertex(p_93124_, (float)p_93126_, (float)p_93127_, (float)p_93130_).color(f1, f2, f3, f).endVertex();
        p_93125_.vertex(p_93124_, (float)p_93126_, (float)p_93129_, (float)p_93130_).color(f5, f6, f7, f4).endVertex();
        p_93125_.vertex(p_93124_, (float)p_93128_, (float)p_93129_, (float)p_93130_).color(f5, f6, f7, f4).endVertex();
    }
}