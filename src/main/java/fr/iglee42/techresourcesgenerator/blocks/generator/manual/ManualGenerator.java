package fr.iglee42.techresourcesgenerator.blocks.generator.manual;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.blocks.ModBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
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

public class ManualGenerator extends GeneratorBlock {
    public ManualGenerator(Generator type) {
        super(Properties.of(Material.METAL).strength(2.0F, 6.0F).sound(SoundType.METAL).noOcclusion(),type);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManualGeneratorTile(state,pos,getType());
    }

}
