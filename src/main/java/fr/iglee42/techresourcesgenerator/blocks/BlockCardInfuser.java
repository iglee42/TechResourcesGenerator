package fr.iglee42.techresourcesgenerator.blocks;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.items.ModItem;
import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import fr.iglee42.techresourcesgenerator.tiles.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class BlockCardInfuser extends BaseEntityBlock {

    private static final VoxelShape SHAPE =
            Block.box(0, 0, 0, 16, 11, 16);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");

    public BlockCardInfuser() {
        super(Properties.of(Material.HEAVY_METAL).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.sidedSuccess(level.isClientSide());
        if (!(level.getBlockEntity(pos) instanceof CardInfuserTile)) return InteractionResult.PASS;
        CardInfuserTile tile = (CardInfuserTile) level.getBlockEntity(pos);
        if (tile.isActive()) return InteractionResult.PASS;
        if (hitResult.getDirection() == state.getValue(FACING)) {
            if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                IItemHandler handler = tile.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
                if (!player.getMainHandItem().isEmpty()){
                    if (handler.getStackInSlot(1).isEmpty()){
                        handler.insertItem(1, new ItemStack(player.getMainHandItem().getItem(),1), false);
                        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                    }
                } else if (player.isCrouching()){
                    player.addItem(tile.extractItemForce(1));
                }
            }
        } else if (hitResult.getDirection() == Direction.UP) {
            if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                IItemHandler handler = tile.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
                if (!player.getMainHandItem().isEmpty()) {
                    ItemStack stack = player.getMainHandItem().copy();
                    stack.setCount(1);
                        if (handler.getStackInSlot(0).isEmpty()) {
                            handler.insertItem(0, stack, false);
                            player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);

                        }
                } else {
                    if (player.isCrouching() && !handler.getStackInSlot(0).isEmpty()) {
                        if (player.getMainHandItem().isEmpty())
                            player.setItemInHand(InteractionHand.MAIN_HAND, tile.extractItemForce(0));
                        else
                            player.addItem(tile.extractItemForce(0));

                    }
                }
            }
        } else if (hitResult.getDirection() == Direction.DOWN){
            if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                IItemHandler handler = tile.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
                if (!handler.getStackInSlot(2).isEmpty()){
                    if (player.getMainHandItem().isEmpty())
                        player.setItemInHand(InteractionHand.MAIN_HAND, tile.extractItemForce(2));
                    else
                        player.addItem(tile.extractItemForce(2));
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (level.getBlockEntity(pos) instanceof CardInfuserTile te) te.dropContent();
        super.onRemove(state, level, pos, p_60518_, p_60519_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.6875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.6875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.6875, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.6875, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape,Shapes.box(0,0.190,0,1,0.6875,1),BooleanOp.OR);
        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(HAS_ITEM, false);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_ITEM);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CardInfuserTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.CARD_INFUSER.get(),CardInfuserTile::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}