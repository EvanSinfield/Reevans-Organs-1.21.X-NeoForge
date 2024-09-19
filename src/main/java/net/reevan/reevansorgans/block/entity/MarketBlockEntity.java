package net.reevan.reevansorgans.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.reevan.reevansorgans.block.entity.inventory.MarketBlockItemHandler;
import net.reevan.reevansorgans.market.MarketSaveData;
import net.reevan.reevansorgans.screen.MarketMenu;
import org.jetbrains.annotations.Nullable;


public class MarketBlockEntity extends BlockEntity implements MenuProvider {
    public static final int IO_SLOT = 0;
    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private final IItemHandler itemHandler;
    private final ContainerData containerData;

    public MarketBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MARKET_BE.get(), pos, blockState);
        this.itemHandler = new MarketBlockItemHandler(this.inventory, null);
        this.containerData = new ContainerData() {
            // XXX: this is useless but i need it
            @Override
            public int get(int i) {
                return 0;
            }

            @Override
            public void set(int i, int i1) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.reevansorgans.market_block");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", this.inventory.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MarketMenu(i, inventory, this, this.containerData);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }
}
