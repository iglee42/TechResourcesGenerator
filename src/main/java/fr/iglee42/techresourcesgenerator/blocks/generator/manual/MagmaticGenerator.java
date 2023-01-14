package fr.iglee42.techresourcesgenerator.blocks.generator.manual;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class MagmaticGenerator extends GeneratorBlock {


    public MagmaticGenerator(Generator generatorType) {
        super(Properties.of(Material.METAL).strength(4.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops().harvestLevel(2).harvestTool(ToolType.PICKAXE),generatorType);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MagmaticGeneratorTile(this.getType());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack it, @Nullable IBlockReader getter, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(it, getter, list, flag);
        list.add(new StringTextComponent("Use lava to generate items").withStyle(TextFormatting.YELLOW));
    }

}
