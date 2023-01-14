package fr.iglee42.techresourcesgenerator.blocks.generator.automatic;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
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

public class ElectricGenerator extends GeneratorBlock {


    public ElectricGenerator(Generator generatorType) {
        super(Properties.of(Material.METAL).strength(4.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(3),generatorType);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElectricGeneratorTile(this.getType());
    }

    @Override
    public void appendHoverText(ItemStack it, @Nullable IBlockReader getter, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(it, getter, list, flag);
        int consumed = getType().isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(getType().name().toLowerCase())).getConsumeFE() : getType().consumed();
        list.add(new StringTextComponent("Cosumed Energy : ").withStyle(TextFormatting.YELLOW).append(new StringTextComponent(""+consumed).withStyle(TextFormatting.GOLD)).append("/second").withStyle(TextFormatting.YELLOW));    }

}
