package net.reevan.reevansorgans.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reevan.reevansorgans.screen.MarketMenu;
import org.jetbrains.annotations.Nullable;

public class MarketBlockEntity extends BlockEntity implements MenuProvider {
    public MarketBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntites.MARKET_BE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.revansorgans.market_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MarketMenu(i, inventory);
    }
}
