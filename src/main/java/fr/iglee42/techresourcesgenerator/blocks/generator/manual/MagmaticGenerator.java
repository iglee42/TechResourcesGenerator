package fr.iglee42.techresourcesgenerator.blocks.generator.manual;

import fr.iglee42.techresourcesgenerator.blocks.generator.GeneratorBlock;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagmaticGenerator extends GeneratorBlock {


    public MagmaticGenerator(Generator generatorType) {
        super(Properties.of(Material.METAL).strength(4.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops(),generatorType);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagmaticGeneratorTile(state,pos,getType());
    }
    @Override
    public void appendHoverText(ItemStack it, @Nullable BlockGetter getter, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(it, getter, list, flag);
        list.add(new TextComponent("Use lava to generate items").withStyle(ChatFormatting.YELLOW));
    }

}
