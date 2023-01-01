package fr.iglee42.techresourcesgenerator.blocks.generator;

import fr.iglee42.techresourcesgenerator.blocks.generator.automatic.ElectricGenerator;
import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.GeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.MagmaticGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.ManualGeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.ConfigsForType;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneratorBlock extends BaseEntityBlock {
    private GeneratorType type;
    public GeneratorBlock(Properties properties,GeneratorType generatorType) {
        super(properties);
        this.type = generatorType;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (level, pos, state, be) -> {
            if (be instanceof GeneratorTile tile)
                tile.tick(level, pos, state, (GeneratorTile) be);
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (level.getBlockEntity(pos) instanceof ManualGeneratorTile te) {
            if (player.isCrouching()) {
                if (te.hasGessence()) {
                    Block.popResource(level, pos.offset(0, 1, 0), new ItemStack(ModItem.getGessence(te.getGessence())));
                    te.setGessence(null);
                    return InteractionResult.SUCCESS;
                }
            }
            if (ItemGessence.isGessence(player.getMainHandItem().getItem())) {
                GessenceType type = GessenceType.getByItem(player.getMainHandItem().getItem());
                if (type.getMinimumGenerator().getOrder() <= GeneratorType.BASIC.getOrder()) {
                    if (te.hasGessence()) {
                        player.displayClientMessage(new TranslatableComponent("tooltip.techresourcesgenerator.has_gessence").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    } else {
                        te.setGessence(type);
                        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                        return InteractionResult.CONSUME;
                    }
                } else {
                    player.displayClientMessage(new TranslatableComponent("tooltip.techresourcesgenerator.gessence_not_compatible").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                }
            }
            if (te.getDelay() > 0){
                te.setDelay(te.getDelay() - 1);
                player.displayClientMessage(new TextComponent("Delay : " + te.getDelay()).withStyle(ChatFormatting.RED), true);
            }
            if (te.getDelay() == 0) {
                if (!te.hasGessence()){
                    player.displayClientMessage(new TranslatableComponent("tooltip.techresourcesgenerator.no_gessence").withStyle(ChatFormatting.RED),true);
                    return InteractionResult.FAIL;
                }
                if (te.generateItem()) te.resetDelay();
            }

            return InteractionResult.SUCCESS;
        } else if (level.getBlockEntity(pos) instanceof MagmaticGeneratorTile te) {
            if (player.getMainHandItem().is(Items.LAVA_BUCKET) && hit.getDirection() == Direction.UP)
                return InteractionResult.PASS;
            else NetworkHooks.openGui(((ServerPlayer) player), te, pos);
        } else if (level.getBlockEntity(pos) instanceof ElectricGeneratorTile te) {
            if (ItemGessence.isElectronicGessence(player.getMainHandItem().getItem())){
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itH->{
                    if (itH.getStackInSlot(0).isEmpty()) {
                        ItemStack stack = player.getMainHandItem().copy();
                        stack.setCount(1);
                        itH.insertItem(0, stack, false);
                        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                    }
                });
            } else
                NetworkHooks.openGui(((ServerPlayer) player), te, pos);
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.0625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.0625, 0.9375, 0.8125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0.5625, 0.6875, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.8125, 0.5, 0.9375, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0.0625, 0.9375, 0.9375, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0.5, 0.3125, 0.9375, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0.0625, 0.0625, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.9375, 0.5625, 0.6875, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.9375, 0.5, 0.9375, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.9375, 0.0625, 0.9375, 1, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.9375, 0.5, 0.3125, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 1, 1, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.9375, 0.9375, 0.9375, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0.9375, 0.0625, 1, 1, 1), BooleanOp.OR);

        return this instanceof ElectricGenerator ? shape :Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
                Block.box(1, 1, 1, 15, 15, 15),
                Block.box(0, 15, 0, 16, 16, 16)
        );
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MagmaticGeneratorTile te) {
                te.drops();
            } else if (blockEntity instanceof ElectricGeneratorTile te) te.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    public void appendHoverText(ItemStack it, @Nullable BlockGetter getter, List<Component> components, TooltipFlag p_49819_) {
        super.appendHoverText(it, getter, components, p_49819_);
        ConfigsForType config = ConfigsForType.getConfigForType(type);
        components.add(new TextComponent("Items dropped : ").withStyle(ChatFormatting.YELLOW).append(""+config.getItemCount()).withStyle(ChatFormatting.GOLD));
        components.add(new TextComponent("Delay : ").withStyle(ChatFormatting.YELLOW).append(""+ config.getDelay()).withStyle(ChatFormatting.GOLD));
        if (type == GeneratorType.BASIC){
            components.add(new TextComponent("The gessence is consume when the delay is 0").withStyle(ChatFormatting.GOLD));
        }
    }

    public GeneratorType getType() {
        return type;
    }
}
