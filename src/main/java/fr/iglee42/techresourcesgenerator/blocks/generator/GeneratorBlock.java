package fr.iglee42.techresourcesgenerator.blocks.generator;

import fr.iglee42.techresourcesgenerator.blocks.generator.automatic.ElectricGenerator;
import fr.iglee42.techresourcesgenerator.customize.Generator;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class GeneratorBlock extends Block {
    private Generator type;
    public GeneratorBlock(AbstractBlock.Properties properties, Generator generatorType) {
        super(properties);
        this.type = generatorType;
    }



    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (level.isClientSide) return ActionResultType.sidedSuccess(true);
        if (level.getBlockEntity(pos) instanceof ManualGeneratorTile) {
            ManualGeneratorTile te = (ManualGeneratorTile) level.getBlockEntity(pos);
            if (player.isCrouching()) {
                if (te.hasGessence()) {
                    Block.popResource(level, pos.offset(0, 1, 0), new ItemStack(ModItem.getGessence(te.getGessence())));
                    te.setGessence(null);
                    return ActionResultType.SUCCESS;
                }
            }
            if (ItemGessence.isGessence(player.getMainHandItem().getItem())) {
                Gessence type = Gessence.getByItem(player.getMainHandItem().getItem());
                if (type.getMinimumGenerator().getOrder() <= this.type.getOrder()) {
                    if (te.hasGessence()) {
                        player.displayClientMessage(new TranslationTextComponent("tooltip.techresourcesgenerator.has_gessence").withStyle(TextFormatting.RED), true);
                        return ActionResultType.FAIL;
                    } else {
                        te.setGessence(type);
                        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                        return ActionResultType.CONSUME;
                    }
                } else {
                    player.displayClientMessage(new TranslationTextComponent("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(TextFormatting.RED), true);
                    return ActionResultType.FAIL;
                }
            }
            if (te.getDelay() > 0){
                te.setDelay(te.getDelay() - 1);
                player.displayClientMessage(new StringTextComponent("Delay : " + te.getDelay()).withStyle(TextFormatting.RED), true);
            }
            if (te.getDelay() == 0) {
                if (!te.hasGessence()){
                    player.displayClientMessage(new TranslationTextComponent("tooltip.techresourcesgenerator.no_gessence").withStyle(TextFormatting.RED),true);
                    return ActionResultType.FAIL;
                }
                if (te.generateItem()) te.resetDelay();
            }

            return ActionResultType.SUCCESS;
        } else if (level.getBlockEntity(pos) instanceof MagmaticGeneratorTile) {
            MagmaticGeneratorTile te = (MagmaticGeneratorTile) level.getBlockEntity(pos);
            if (player.getMainHandItem().getItem() == Items.LAVA_BUCKET && hit.getDirection() == Direction.UP)
                return ActionResultType.PASS;
            else NetworkHooks.openGui(((ServerPlayerEntity) player), te, pos);
        } else if (level.getBlockEntity(pos) instanceof ElectricGeneratorTile) {
            ElectricGeneratorTile te = (ElectricGeneratorTile) level.getBlockEntity(pos);
            if (ItemGessence.isElectronicGessence(player.getMainHandItem().getItem())){
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itH->{
                    if (itH.getStackInSlot(0).isEmpty()) {
                        ItemStack stack = player.getMainHandItem().copy();
                        stack.setCount(1);
                        if (itH.insertItem(0, stack, false) == ItemStack.EMPTY)
                            player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                    }
                });
            } else
                NetworkHooks.openGui(((ServerPlayerEntity) player), te, pos);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, IBlockReader p_60556_, BlockPos p_60557_, ISelectionContext p_60558_) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 0.0625, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.0625, 0.0625, 0.9375, 0.8125, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.8125, 0.5625, 0.6875, 0.9375, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.8125, 0.5, 0.9375, 0.9375, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.8125, 0.0625, 0.9375, 0.9375, 0.5), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.8125, 0.5, 0.3125, 0.9375, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.9375, 0.0625, 0.0625, 1, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.9375, 0.5625, 0.6875, 1, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.9375, 0.5, 0.9375, 1, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.9375, 0.0625, 0.9375, 1, 0.5), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.9375, 0.5, 0.3125, 1, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.9375, 0, 1, 1, 0.0625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0.9375, 0.9375, 0.9375, 1, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.9375, 0.9375, 0.0625, 1, 1, 1), IBooleanFunction.OR);

        return this instanceof ElectricGenerator ? shape :VoxelShapes.or(Block.box(0, 0, 0, 16, 1, 16),
                Block.box(1, 1, 1, 15, 15, 15),
                Block.box(0, 15, 0, 16, 16, 16)
        );
    }
    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            TileEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MagmaticGeneratorTile ) {
                MagmaticGeneratorTile te = (MagmaticGeneratorTile) pLevel.getBlockEntity(pPos);
                te.drops();
            } else if (blockEntity instanceof ElectricGeneratorTile){
                ElectricGeneratorTile te = (ElectricGeneratorTile) pLevel.getBlockEntity(pPos);
                te.drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    public void appendHoverText(ItemStack it, @Nullable IBlockReader getter, List<ITextComponent> components, ITooltipFlag p_49819_) {
        super.appendHoverText(it, getter, components, p_49819_);
        components.add(new StringTextComponent("Items dropped : ").withStyle(TextFormatting.YELLOW).append(""+(type.isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(type.name())).getItemCount() : type.itemCount())).withStyle(TextFormatting.GOLD));
        components.add(new StringTextComponent("Delay : ").withStyle(TextFormatting.YELLOW).append(""+ (type.isInModBase() ? ConfigsForType.getConfigForType(GeneratorType.getByName(type.name())).getDelay() : type.delay())).withStyle(TextFormatting.GOLD));
        if (Objects.equals(type.generatorType(), "basic")){
            components.add(new StringTextComponent("The gessence is consume when the delay is 0").withStyle(TextFormatting.GOLD));
        }
    }

    public Generator getType() {
        return type;
    }
}
