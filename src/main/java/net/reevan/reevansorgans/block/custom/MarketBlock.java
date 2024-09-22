package net.reevan.reevansorgans.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.reevan.reevansorgans.block.entity.MarketBlockEntity;
import net.reevan.reevansorgans.screen.MarketMenu;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MarketBlock extends  Block implements EntityBlock {
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,12,16);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final MapCodec<MarketBlock> CODEC = simpleCodec(MarketBlock::new);

    public MarketBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

/*
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
*/

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MarketBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MarketBlockEntity) {
                player.openMenu((MarketBlockEntity)blockEntity, pos);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {  // Ensure that the block is actually being removed
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MarketBlockEntity) {
                MarketBlockEntity marketBE = (MarketBlockEntity) blockEntity;
                Containers.dropContents(level, pos, marketBE.getItemDrops()); // Drop the inventory contents
                level.updateNeighbourForOutputSignal(pos, this);  // Notify neighbors
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
