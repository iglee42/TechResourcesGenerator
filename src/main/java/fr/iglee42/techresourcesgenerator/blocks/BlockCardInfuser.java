package fr.iglee42.techresourcesgenerator.blocks;

import fr.iglee42.techresourcesgenerator.tiles.CardInfuserTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockCardInfuser extends Block {


    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");

    public BlockCardInfuser() {
        super(Properties.of(Material.HEAVY_METAL).noOcclusion());
    }



    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult) {
        if (level.isClientSide()) return ActionResultType.sidedSuccess(level.isClientSide());
        if (!(level.getBlockEntity(pos) instanceof CardInfuserTile)) return ActionResultType.PASS;
        CardInfuserTile tile = (CardInfuserTile) level.getBlockEntity(pos);
        if (tile.isActive()) return ActionResultType.PASS;
        if (hitResult.getDirection() == state.getValue(FACING)) {
            if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
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
            if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
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
                            player.setItemInHand(Hand.MAIN_HAND, tile.extractItemForce(0));
                        else
                            player.addItem(tile.extractItemForce(0));

                    }
                }
            }
        } else if (hitResult.getDirection() == Direction.DOWN){
            if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
                if (!handler.getStackInSlot(2).isEmpty()){
                    if (player.getMainHandItem().isEmpty())
                        player.setItemInHand(Hand.MAIN_HAND, tile.extractItemForce(2));
                    else
                        player.addItem(tile.extractItemForce(2));
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (level.getBlockEntity(pos) instanceof CardInfuserTile ){
            CardInfuserTile te = (CardInfuserTile) level.getBlockEntity(pos);
            te.dropContent();
        }
        super.onRemove(state, level, pos, p_60518_, p_60519_);
    }



    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_60556_, BlockPos p_60557_, ISelectionContext p_60558_) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.875, 0, 0.875, 1, 0.6875, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0.875, 0.125, 0.6875, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.875, 0, 0, 1, 0.6875, 0.125), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 0.125, 0.6875, 0.125), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape,VoxelShapes.box(0,0.190,0,1,0.6875,1),IBooleanFunction.OR);
        return shape;
    }
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_ITEM);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CardInfuserTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}