package fr.iglee42.techresourcesgenerator.blocks.generator.automatic;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricGenerator extends GeneratorBlock {


    public ElectricGenerator(Generator generatorType) {
        super(Properties.of(Material.METAL).strength(4.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops(),generatorType);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricGeneratorTile(state,pos,getType());
    }
    @Override
    public void appendHoverText(ItemStack it, @Nullable BlockGetter getter, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(it, getter, list, flag);
        int consumed = getType().isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(getType().name().toLowerCase())).getConsumeFE() : getType().consumed();
        list.add(Component.literal("Cosumed Energy : ").withStyle(ChatFormatting.YELLOW).append(Component.literal(""+consumed).withStyle(ChatFormatting.GOLD)).append("/second").withStyle(ChatFormatting.YELLOW));
    }

}
